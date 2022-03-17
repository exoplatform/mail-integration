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

import java.time.ZoneOffset;
import java.time.ZonedDateTime;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Properties;

import javax.mail.Flags.Flag;
import javax.mail.Folder;
import javax.mail.Message;
import javax.mail.MessagingException;
import javax.mail.NoSuchProviderException;
import javax.mail.Session;
import javax.mail.Store;
import javax.mail.UIDFolder;

import org.exoplatform.commons.api.notification.NotificationContext;
import org.exoplatform.commons.api.notification.model.PluginKey;
import org.exoplatform.commons.notification.impl.NotificationContextImpl;
import org.exoplatform.mailintegration.model.MailIntegrationSetting;
import org.exoplatform.mailintegration.model.MailIntegrationUserSetting;
import org.exoplatform.mailintegration.notification.plugin.MailIntegrationNotificationPlugin;
import org.exoplatform.mailintegration.notification.utils.NotificationConstants;
import org.exoplatform.mailintegration.rest.model.MessageRestEntity;
import org.exoplatform.mailintegration.utils.RestEntityBuilder;
import org.exoplatform.services.log.ExoLogger;
import org.exoplatform.services.log.Log;
import org.exoplatform.services.user.UserStateService;
import org.exoplatform.social.core.identity.model.Identity;
import org.exoplatform.social.core.manager.IdentityManager;
import org.exoplatform.web.security.codec.CodecInitializer;

public class MailIntegrationServiceImpl implements MailIntegrationService {
  
  private CodecInitializer codecInitializer;// NOSONAR
  
  private IdentityManager identityManager;
  
  private UserStateService userService;
  
  private static final Log LOG = ExoLogger.getLogger(MailIntegrationServiceImpl.class);
  
  public MailIntegrationServiceImpl(CodecInitializer codecInitializer, IdentityManager identityManager, UserStateService userService) {
    this.userService = userService;
    this.identityManager = identityManager;
    this.codecInitializer = codecInitializer;
  }
  
  @Override
  public Store imapConnect(MailIntegrationSetting mailIntegrationSetting) {
    Properties props = new Properties();
    props.setProperty("mail.store.protocol", "imaps");
    String provider = "imaps";
    // Connect to the server
    Session session = Session.getDefaultInstance(props, null);
    Store store = null;
    try {
      store = session.getStore(provider);
      //TODO to be added // NOSONAR
      //String password = codecInitializer.getCodec().decode(mailIntegrationSetting.getPassword()); // NOSONAR
      store.connect(mailIntegrationSetting.getHost(), mailIntegrationSetting.getUserName(), mailIntegrationSetting.getPassword());
    } catch (NoSuchProviderException noSuchProviderException) {
      LOG.error("Invalid provider name", noSuchProviderException);
    } catch (MessagingException messagingException) {
      LOG.error("Unable to connect to the store", messagingException);
    }
    return store;
  }

  @Override
  public void sendMailIntegrationNotifications() {
    List<MailIntegrationUserSetting> mailIntegrationUserSettings = getMailIntegrationUserSettings();
    // Send Notifications
    for (MailIntegrationUserSetting mailIntegrationUserSetting : mailIntegrationUserSettings) {
      Identity userIdentity = identityManager.getIdentity(String.valueOf(mailIntegrationUserSetting.getUserId()));
      if (userIdentity != null && userService.isOnline(userIdentity.getRemoteId())) {
        MailIntegrationSetting mailIntegrationSetting = getMailIntegrationSettingById(String.valueOf(mailIntegrationUserSetting.getMailntegrationSettingId()));
        List<String> newMessages = new ArrayList<>();
        try {
          if (mailIntegrationSetting != null) {
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
    //TODO call MailIntegrationUserSettingStorage // NOSONAR
    MailIntegrationUserSetting mailIntegrationUserSetting = new MailIntegrationUserSetting(1, 2, 1);
    List<MailIntegrationUserSetting> mailIntegrationUserSettings = new ArrayList<>();
    mailIntegrationUserSettings.add(mailIntegrationUserSetting);
    return mailIntegrationUserSettings;
  }
  
  public MailIntegrationSetting getMailIntegrationSettingById(String mailntegrationSettingId) {
    String host = System.getProperty("exo.mailIntegration.MailIntegrationSetting.host");
    String userName = System.getProperty("exo.mailIntegration.MailIntegrationSetting.userName");
    String password = System.getProperty("exo.mailIntegration.MailIntegrationSetting.password");
    MailIntegrationSetting mailIntegrationSetting = null;
    if (host != null && userName != null && password != null) {
    //TODO call MailIntegrationSettingStorage // NOSONAR
      mailIntegrationSetting = new MailIntegrationSetting(Long.parseLong(mailntegrationSettingId), "setting1", host, "666", userName, password, "ssl");
    }
    return mailIntegrationSetting;
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
      if (!messages[i].isSet(Flag.SEEN)) {
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
  public MessageRestEntity getMessageById(String mailntegrationSettingId, String messageId, org.exoplatform.services.security.Identity currentIdentity) throws IllegalAccessException {
    if (!hasMailIntegrationUserSetting()) {
      throw new IllegalAccessException("User " + currentIdentity.getUserId() + "is not allowed to get a message with id "
          + messageId);
    }
    MailIntegrationSetting mailIntegrationSetting = getMailIntegrationSettingById(mailntegrationSettingId);
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
  
  private boolean hasMailIntegrationUserSetting() {
    //TODO check from storage  if there is a MailIntegrationUserSetting with userId and mailntegrationSettingId
    return true;
  }
  
  
}
