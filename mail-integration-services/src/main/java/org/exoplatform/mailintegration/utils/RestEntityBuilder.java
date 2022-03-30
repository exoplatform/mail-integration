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
package org.exoplatform.mailintegration.utils;

import javax.mail.Message;
import javax.mail.MessagingException;
import javax.mail.Multipart;
import javax.mail.Part;
import javax.mail.internet.MimeBodyPart;

import org.exoplatform.mailintegration.model.MailIntegrationSetting;
import org.exoplatform.mailintegration.rest.model.MailIntegrationSettingRestEntity;
import org.exoplatform.mailintegration.rest.model.MessageRestEntity;

import java.io.IOException;

public class RestEntityBuilder {

  private RestEntityBuilder() {
  }
  
  public static final MailIntegrationSettingRestEntity fromMailIntegrationSetting(MailIntegrationSetting mailIntegrationSetting) {
    MailIntegrationSettingRestEntity mailIntegrationSettingRestEntity = new MailIntegrationSettingRestEntity();
    mailIntegrationSettingRestEntity.setId(mailIntegrationSetting.getId());
    mailIntegrationSettingRestEntity.setEmailName(mailIntegrationSetting.getEmailName());
    mailIntegrationSettingRestEntity.setImapUrl(mailIntegrationSetting.getImapUrl());
    mailIntegrationSettingRestEntity.setPort(mailIntegrationSetting.getPort());
    mailIntegrationSettingRestEntity.setEncryption(mailIntegrationSetting.getEncryption());
    mailIntegrationSettingRestEntity.setAccount(mailIntegrationSetting.getAccount());
    mailIntegrationSettingRestEntity.setPassword(MailIntegrationUtils.decode(mailIntegrationSetting.getPassword()));
    return mailIntegrationSettingRestEntity;
  }

  public static final MailIntegrationSetting toMailIntegrationSetting(MailIntegrationSettingRestEntity mailIntegrationSettingRestEntity,
                                                                      long userIdentityId) {
    MailIntegrationSetting mailIntegrationSetting = new MailIntegrationSetting();
    mailIntegrationSetting.setEmailName(mailIntegrationSettingRestEntity.getEmailName());
    mailIntegrationSetting.setImapUrl(mailIntegrationSettingRestEntity.getImapUrl());
    mailIntegrationSetting.setPort(mailIntegrationSettingRestEntity.getPort());
    mailIntegrationSetting.setEncryption(mailIntegrationSettingRestEntity.getEncryption());
    mailIntegrationSetting.setAccount(mailIntegrationSettingRestEntity.getAccount());
    mailIntegrationSetting.setPassword(mailIntegrationSettingRestEntity.getPassword());
    mailIntegrationSetting.setUserId(userIdentityId);
    return mailIntegrationSetting;
  }

  public static final MessageRestEntity fromMessage(Message message) throws MessagingException, IOException {
    if (message != null) {
      MessageRestEntity messageRestEntity = new MessageRestEntity();
      messageRestEntity.setSubject(message.getSubject());
      messageRestEntity.setSentDate(message.getSentDate());
      messageRestEntity.setFrom(message.getFrom()[0].toString().split("<")[1].split(">")[0]);
      String contentType = message.getContentType();
      // store attachment file name, separated by comma
      StringBuilder attachFiles = new StringBuilder();
      if (contentType.contains("multipart")) {
        // content may contain attachments
        Multipart multiPart = (Multipart) message.getContent();
        int numberOfParts = multiPart.getCount();
        for (int partCount = 0; partCount < numberOfParts; partCount++) {
          MimeBodyPart part = (MimeBodyPart) multiPart.getBodyPart(partCount);
          if (Part.ATTACHMENT.equalsIgnoreCase(part.getDisposition())) {
            // this part is attachment
            String fileName = part.getFileName();
            attachFiles.append(fileName).append(", ");
          }
        }

        if (attachFiles.length() > 1) {
          attachFiles = new StringBuilder(attachFiles.substring(0, attachFiles.length() - 2));
        }
        messageRestEntity.setAttachFiles(attachFiles.toString());
      }
      return messageRestEntity;
    }
    return null;
  }

}
