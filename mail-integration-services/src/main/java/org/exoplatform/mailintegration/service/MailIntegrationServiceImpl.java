/*
 * Copyright (C) 2022 eXo Platform SAS.
 *
 * This program is free software: you can redistribute it and/or modify
 * it under the terms of the GNU Affero General Public License as published by
 * the Free Software Foundation, either version 3 of the License, or
 * (at your option) any later version.
 *
 * This program is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 * GNU Affero General Public License for more details.
 *
 * You should have received a copy of the GNU Affero General Public License
 * along with this program. If not, see <http://www.gnu.org/licenses/>.
 */
package org.exoplatform.mailintegration.service;

import java.io.IOException;
import java.time.ZoneOffset;
import java.time.ZonedDateTime;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Properties;

import javax.mail.Flags;
import javax.mail.Folder;
import javax.mail.Message;
import javax.mail.MessagingException;
import javax.mail.NoSuchProviderException;
import javax.mail.Session;
import javax.mail.Store;
import javax.mail.UIDFolder;

import org.apache.commons.lang.StringUtils;

import org.exoplatform.commons.api.notification.NotificationContext;
import org.exoplatform.commons.api.notification.model.PluginKey;
import org.exoplatform.commons.notification.impl.NotificationContextImpl;
import org.exoplatform.mailintegration.model.MailIntegrationSetting;
import org.exoplatform.mailintegration.notification.plugin.MailIntegrationNotificationPlugin;
import org.exoplatform.mailintegration.notification.utils.NotificationConstants;
import org.exoplatform.mailintegration.rest.model.MessageRestEntity;
import org.exoplatform.mailintegration.storage.MailIntegrationStorage;
import org.exoplatform.mailintegration.utils.MailIntegrationUtils;
import org.exoplatform.mailintegration.utils.RestEntityBuilder;
import org.exoplatform.services.log.ExoLogger;
import org.exoplatform.services.log.Log;
import org.exoplatform.services.user.UserStateService;
import org.exoplatform.social.core.identity.model.Identity;
import org.exoplatform.social.core.manager.IdentityManager;

public class MailIntegrationServiceImpl implements MailIntegrationService {
  private static final Log       LOG        = ExoLogger.getLogger(MailIntegrationServiceImpl.class);

  public static final String     MAIL_IMAPS = "imaps";

  public static final String     MAIL_SSL   = "SSL";

  private MailIntegrationStorage mailIntegrationStorage;

  private IdentityManager        identityManager;

  private UserStateService       userStateService;

  public MailIntegrationServiceImpl(MailIntegrationStorage mailIntegrationStorage,
                                    IdentityManager identityManager,
                                    UserStateService userStateService) {
    this.mailIntegrationStorage = mailIntegrationStorage;
    this.identityManager = identityManager;
    this.userStateService = userStateService;
  }

  @Override
  public MailIntegrationSetting createMailIntegrationSetting(MailIntegrationSetting mailIntegrationSetting) {
    String encodedPassword = MailIntegrationUtils.encode(mailIntegrationSetting.getPassword());
    mailIntegrationSetting.setPassword(encodedPassword);
    return mailIntegrationStorage.createMailIntegrationSetting(mailIntegrationSetting);
  }

  @Override
  public List<MailIntegrationSetting> getMailIntegrationSettingsByUserId(long userIdentityId) {
    return mailIntegrationStorage.getMailIntegrationSettingsByUserId(userIdentityId);
  }
  
  @Override
  public MailIntegrationSetting getMailIntegrationSetting(long mailIntegrationSettingId) {
    return mailIntegrationStorage.getMailIntegrationSettingById(mailIntegrationSettingId);
  }
  
  
  @Override
  public void deleteMailIntegrationSetting(long mailIntegrationSettingId, long currentUserIdentityId) throws IllegalAccessException {
    List<MailIntegrationSetting> mailIntegrationSettings = mailIntegrationStorage.getMailIntegrationSettingByMailIntegrationSettingIdAndUserId(mailIntegrationSettingId, currentUserIdentityId);
    if (mailIntegrationSettings.isEmpty()) {
      throw new IllegalAccessException("User " + currentUserIdentityId + " is not allowed to delete mail integration settings with id " + mailIntegrationSettingId);
    }
    mailIntegrationStorage.deleteMailIntegrationSetting(mailIntegrationSettingId);
  }

  @Override
  public Store connect(MailIntegrationSetting mailIntegrationSetting) {
    Properties props = new Properties();
    String usedPort = String.valueOf(mailIntegrationSetting.getPort());
    if (StringUtils.equals(mailIntegrationSetting.getEncryption(), MAIL_SSL)) {
      props.setProperty("mail.imaps.ssl.enable", "true");
      props.setProperty("mail.store.protocol", MAIL_IMAPS);
      props.setProperty("mail.imaps.port", usedPort);
    }
    // Connect to the server
    Session session = Session.getDefaultInstance(props, null);
    Store store = null;
    try {
      store = session.getStore();
      store.connect(mailIntegrationSetting.getImapUrl(),
                    (int) mailIntegrationSetting.getPort(),
                    mailIntegrationSetting.getAccount(),
                    mailIntegrationSetting.getPassword());
    } catch (NoSuchProviderException noSuchProviderException) {
      throw new IllegalArgumentException("Invalid provider name", noSuchProviderException);
    } catch (MessagingException messagingException) {
      throw new IllegalStateException("Messaging exception", messagingException);
    }
    return store;
  }

  @Override
  public void sendMailIntegrationNotifications() {
    List<MailIntegrationSetting> mailIntegrationSettings = mailIntegrationStorage.getMailIntegrationSettings();
    // Send Notifications
    for (MailIntegrationSetting mailIntegrationSetting : mailIntegrationSettings) {
      Identity userIdentity = identityManager.getIdentity(String.valueOf(mailIntegrationSetting.getUserId()));
      if (userIdentity != null && userStateService.isOnline(userIdentity.getRemoteId())) {
        try {
          mailIntegrationSetting.setPassword(MailIntegrationUtils.decode(mailIntegrationSetting.getPassword()));
          List<String> newMessages = getNewMessages(mailIntegrationSetting);
          if (!newMessages.isEmpty()) {
            NotificationContext ctx = NotificationContextImpl.cloneInstance()
                                                           .append(MailIntegrationNotificationPlugin.CONTEXT,
                                                                   NotificationConstants.NOTIFICATION_CONTEXT.NEW_EMAILS_RECIEVED)
                                                           .append(MailIntegrationNotificationPlugin.RECEIVER,
                                                                   userIdentity.getRemoteId())
                                                           .append(MailIntegrationNotificationPlugin.NEW_MESSAGES,
                                                                   String.valueOf(mailIntegrationSetting.getId())
                                                                         .concat(";")
                                                                         .concat(String.join(",", newMessages)));
            ctx.getNotificationExecutor().with(ctx.makeCommand(PluginKey.key(MailIntegrationNotificationPlugin.ID))).execute(ctx);
          }

        } catch (MessagingException messagingException) {
          LOG.error("unable to get new messages", messagingException);
        }
      }
    }
  }

  @Override
  public MessageRestEntity getMessageById(long mailIntegrationSettingId,
                                          String messageId,
                                          long currentUserIdentityId) throws IllegalAccessException {
    List<MailIntegrationSetting> mailIntegrationSettings = mailIntegrationStorage.getMailIntegrationSettingByMailIntegrationSettingIdAndUserId(mailIntegrationSettingId, currentUserIdentityId);
    if (mailIntegrationSettings.isEmpty()) {
      throw new IllegalAccessException("User " + currentUserIdentityId + " is not allowed to get mail integration settings with id " + mailIntegrationSettingId);
    }
    MailIntegrationSetting mailIntegrationSetting = getMailIntegrationSetting(mailIntegrationSettingId);
    mailIntegrationSetting.setPassword(MailIntegrationUtils.decode(mailIntegrationSetting.getPassword()));
    Store store = connect(mailIntegrationSetting);
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
    } catch (IOException e) {
      LOG.error("Error when getting attachments", e);
    }
    return messageRestEntity;
  }
  
  private List<String> getNewMessages(MailIntegrationSetting mailIntegrationSetting) throws MessagingException {

    Store store = connect(mailIntegrationSetting);
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

  private boolean isNewMessage(Date messageSentDate, Date now) {
    String mailIntegrationNotificationJobPeriod = System.getProperty("exo.mailIntegration.MailIntegrationNotificationJob.period",
                                                                     "120000");
    return ZonedDateTime.ofInstant(messageSentDate.toInstant(), ZoneOffset.UTC)
                        .isAfter(ZonedDateTime.ofInstant(now.toInstant(), ZoneOffset.UTC)
                                              .minusMinutes(Long.parseLong(mailIntegrationNotificationJobPeriod) / 60000));
  }
}
