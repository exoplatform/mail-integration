package org.exoplatform.mailintegration.service;

import org.apache.commons.lang.StringUtils;
import org.exoplatform.commons.api.notification.NotificationContext;
import org.exoplatform.commons.api.notification.model.PluginKey;
import org.exoplatform.commons.notification.impl.NotificationContextImpl;
import org.exoplatform.mailintegration.entity.MailIntegrationSettingEntity;
import org.exoplatform.mailintegration.model.MailIntegrationSetting;
import org.exoplatform.mailintegration.model.MailIntegrationUserSetting;
import org.exoplatform.mailintegration.notification.plugin.MailIntegrationNotificationPlugin;
import org.exoplatform.mailintegration.notification.utils.NotificationConstants;
import org.exoplatform.mailintegration.rest.model.MessageRestEntity;
import org.exoplatform.mailintegration.storage.MailIntegrationStorage;
import org.exoplatform.mailintegration.utils.EntityMapper;
import org.exoplatform.mailintegration.utils.MailIntegrationUtils;
import org.exoplatform.mailintegration.utils.RestEntityBuilder;
import org.exoplatform.services.log.ExoLogger;
import org.exoplatform.services.log.Log;
import org.exoplatform.services.user.UserStateService;
import org.exoplatform.social.core.identity.model.Identity;
import org.exoplatform.social.core.manager.IdentityManager;
import org.exoplatform.web.security.codec.CodecInitializer;

import javax.mail.*;
import java.time.ZoneOffset;
import java.time.ZonedDateTime;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Properties;
import java.util.stream.Collectors;

public class MailIntegrationServiceImpl implements MailIntegrationService {
  private static final Log LOG = ExoLogger.getLogger(MailIntegrationServiceImpl.class);

  private MailIntegrationStorage mailIntegrationStorage;

  private IdentityManager        identityManager;

  private CodecInitializer       codecInitializer;

  private UserStateService       userService;


  public MailIntegrationServiceImpl(MailIntegrationStorage mailIntegrationStorage, IdentityManager identityManager, CodecInitializer codecInitializer, UserStateService userService) {
    this.mailIntegrationStorage = mailIntegrationStorage;
    this.identityManager = identityManager;
    this.codecInitializer = codecInitializer;
    this.userService = userService;
  }

  @Override
  public MailIntegrationSetting createMailIntegration(MailIntegrationSetting mailIntegrationSetting,
                                                      long userIdentityId) throws IllegalAccessException {
    if (userIdentityId <= 0) {
      throw new IllegalArgumentException("userIdentityId is mandatory");
    }

    Identity userIdentity = identityManager.getIdentity(String.valueOf(userIdentityId));
    if (userIdentity == null) {
      throw new IllegalAccessException("User '" + userIdentityId + "' doesn't exist");
    }
    if (mailIntegrationSetting == null) {
      throw new IllegalArgumentException("Mail integration setting is mandatory");
    }
    String tokenPlain = mailIntegrationSetting.getPassword();
    if (tokenPlain.isBlank()) {
      throw new IllegalArgumentException("Mail password setting is mandatory");
    }
    String token = MailIntegrationUtils.generateEncryptedToken(codecInitializer, tokenPlain, userIdentity.getRemoteId());
    mailIntegrationSetting.setPassword(token);
    return mailIntegrationStorage.createMailIntegration(mailIntegrationSetting);
  }

  @Override
  public Store imapConnect(MailIntegrationSetting mailIntegrationSetting) {
    Properties props = new Properties();
    String usedPort = String.valueOf(mailIntegrationSetting.getPort());
    if (StringUtils.equals(mailIntegrationSetting.getEncryption(), "SSL")) {
      props.setProperty("mail.imap.ssl.enable", "true");
    }
    props.setProperty("mail.store.protocol", "imaps");
    props.setProperty("mail.imaps.port", usedPort);
    String provider = "imaps";
    // Connect to the server
    Session session = Session.getDefaultInstance(props, null);
    Store store = null;
    try {
      store = session.getStore(provider);
      store.connect(mailIntegrationSetting.getImapUrl(), (int) mailIntegrationSetting.getPort(), mailIntegrationSetting.getAccount(), mailIntegrationSetting.getPassword());
    } catch (NoSuchProviderException nspe) {
      throw new IllegalArgumentException("invalid provider name");
    } catch (MessagingException me) {
      throw new IllegalStateException("messaging exception");
    }
    return store;
  }

  @Override
  public List<MailIntegrationSetting> getMailIntegrationSettingsByUserId(long userIdentityId) throws IllegalAccessException {
    if (userIdentityId <= 0) {
      throw new IllegalArgumentException("userIdentityId is mandatory");
    }

    Identity userIdentity = identityManager.getIdentity(String.valueOf(userIdentityId));
    if (userIdentity == null) {
      throw new IllegalAccessException("User '" + userIdentityId + "' doesn't exist");
    }
    List<MailIntegrationSetting> mailIntegrationSettings =
                                                         mailIntegrationStorage.getMailIntegrationSettingsByUserId(userIdentityId);
    return mailIntegrationSettings.stream()
                                  .peek(m -> m.setPassword(MailIntegrationUtils.decryptUserIdentity(codecInitializer,
                                                                                                    m.getPassword())))
                                  .collect(Collectors.toList());
  }

  @Override
  public void sendMailIntegrationNotifications() {
    List<MailIntegrationUserSetting> mailIntegrationUserSettings = getMailIntegrationUserSettings();
    // Send Notifications
    for (MailIntegrationUserSetting mailIntegrationUserSetting : mailIntegrationUserSettings) {
      Identity userIdentity = identityManager.getIdentity(String.valueOf(mailIntegrationUserSetting.getUserId()));
      if (userIdentity != null && userService.isOnline(userIdentity.getRemoteId())) {
        MailIntegrationSetting mailIntegrationSetting = getMailIntegrationSettingById(mailIntegrationUserSetting.getMailIntegrationSettingId());
        List<String> newMessages = new ArrayList<>();
        try {
          if (mailIntegrationSetting != null) {
            mailIntegrationSetting.setPassword(MailIntegrationUtils.decryptUserIdentity(codecInitializer,
                                                                                        mailIntegrationSetting.getPassword()));
            newMessages = getNewMessages(mailIntegrationSetting);
            if (!newMessages.isEmpty()) {
              NotificationContext ctx = NotificationContextImpl.cloneInstance()
                      .append(MailIntegrationNotificationPlugin.CONTEXT, NotificationConstants.NOTIFICATION_CONTEXT.NEW_EMAILS_RECIEVED)
                      .append(MailIntegrationNotificationPlugin.RECEIVER, userIdentity.getRemoteId())
                      .append(MailIntegrationNotificationPlugin.NEW_MESSAGES, String.valueOf(mailIntegrationSetting.getId()).concat(";").concat(String.join(",", newMessages)));
              ctx.getNotificationExecutor().with(ctx.makeCommand(PluginKey.key(MailIntegrationNotificationPlugin.ID))).execute(ctx);
            }
          }

        } catch (MessagingException messagingException) {
          LOG.error("unable to get new messages", messagingException);
        }
      }
    }
  }

  public List<MailIntegrationUserSetting> getMailIntegrationUserSettings() {
    return mailIntegrationStorage.getMailIntegrationUserSettings();
  }

  public MailIntegrationSetting getMailIntegrationSettingById(long mailIntegrationSettingId) {
    return mailIntegrationStorage.getMailIntegrationSettingById(mailIntegrationSettingId);
  }

  public List<String> getNewMessages(MailIntegrationSetting mailIntegrationSetting) throws MessagingException {

    Store store = imapConnect(mailIntegrationSetting);
    List<String> newMessages = new ArrayList<>();

    Folder inbox = store.getFolder("INBOX");
    inbox.open(Folder.READ_ONLY);

    // get a list of javamail messages as an array of messages
    Date now = new Date();
    UIDFolder uidInbox = (UIDFolder) inbox;
    Message[] messages = inbox.getMessages();
    for (int i = inbox.getMessageCount() - 1; i > 0; i--) {
      if (!isNewMessage(messages[i].getSentDate(), now)) {
        break;
      }
      if (!messages[i].isSet(Flags.Flag.SEEN)) {
        newMessages.add(String.valueOf(uidInbox.getUID(messages[i])));
      }
    }
    // close the inbox folder but do not remove the messages from the server
    inbox.close(false);
    store.close();
    return newMessages;
  }

  public boolean isNewMessage(Date messageSentDate, Date now) {
    String mailIntegrationNotificationJobPeriod = System.getProperty("exo.mailIntegration.MailIntegrationNotificationJob.period", "120000");
    return ZonedDateTime.ofInstant(messageSentDate.toInstant(), ZoneOffset.UTC).isAfter(ZonedDateTime.ofInstant(now.toInstant(), ZoneOffset.UTC).minusMinutes(Long.parseLong(mailIntegrationNotificationJobPeriod) / 60000));
  }


  @Override
  public MessageRestEntity getMessageById(long mailIntegrationSettingId, String messageId, long identityId) throws IllegalAccessException {
    if (!hasMailIntegrationUserSetting(mailIntegrationSettingId, identityId)) {
      throw new IllegalAccessException("User " + identityId + "is not allowed to get a message with id "
              + messageId);
    }
    MailIntegrationSetting mailIntegrationSetting = getMailIntegrationSettingById(mailIntegrationSettingId);
    Store store = imapConnect(mailIntegrationSetting);
    Folder inbox;
    MessageRestEntity messageRestEntity = null;
    try {
      inbox = store.getFolder("INBOX");
      inbox.open(Folder.READ_ONLY);
      UIDFolder uidInbox = (UIDFolder) inbox;
      Message message = uidInbox.getMessageByUID(Long.parseLong(messageId));
      messageRestEntity = RestEntityBuilder.fromMessage(message);
      inbox.close(false);
      store.close();
    } catch (MessagingException messagingException) {
      LOG.error("unable to get or open folder", messagingException);
    }
    return messageRestEntity;
  }

  @Override
  public boolean isConnected(MailIntegrationSetting mailIntegrationSetting) {
    Properties props = new Properties();
    props.setProperty("mail.store.protocol", "imaps");
    String provider = "imaps";
    // Connect to the server
    Session session = Session.getDefaultInstance(props, null);
    Store store = null;
    boolean connectionStatus = false;
    try {
      store = session.getStore(provider);
      connectionStatus = store.isConnected();
    } catch (NoSuchProviderException nspe) {
      throw new IllegalArgumentException("invalid provider name");
    } catch (MessagingException me) {
      throw new IllegalStateException("messaging exception");
    }
    return connectionStatus;
  }

  private boolean hasMailIntegrationUserSetting(long mailIntegrationSettingId, long userId) {
    List<MailIntegrationSetting> mailIntegrationSettings =
                                                         mailIntegrationStorage.getMailIntegrationSettingByMailIntegrationSettingIdAndUserId(mailIntegrationSettingId,
                                                                                                                                             userId);
    return !mailIntegrationSettings.isEmpty();
  }

}
