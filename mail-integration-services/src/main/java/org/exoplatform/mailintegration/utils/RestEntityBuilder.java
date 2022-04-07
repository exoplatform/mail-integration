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

import javax.mail.*;
import javax.mail.internet.MimeBodyPart;
import javax.mail.internet.MimeMultipart;

import org.exoplatform.mailintegration.model.MailIntegrationSetting;
import org.exoplatform.mailintegration.rest.model.MailIntegrationSettingRestEntity;
import org.exoplatform.mailintegration.rest.model.MessageRestEntity;
import org.exoplatform.services.log.ExoLogger;
import org.exoplatform.services.log.Log;

import java.io.IOException;

public class RestEntityBuilder {
  private static final Log LOG = ExoLogger.getLogger(RestEntityBuilder.class);


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
      if (message.getFrom()[0].toString().contains("<") && message.getFrom()[0].toString().contains(">")) {
        messageRestEntity.setFrom(message.getFrom()[0].toString().split("<")[1].split(">")[0]);
      } else {
        messageRestEntity.setFrom(message.getFrom()[0].toString());
      }
      String contentType = message.getContentType();
      String contentMail = "";
      // store attachment file name, separated by comma
      StringBuilder attachedFiles = new StringBuilder();
      if (contentType.contains("multipart")) {
        // content may contain attachments
        Multipart multiPart = (Multipart) message.getContent();
        int numberOfParts = multiPart.getCount();
        for (int partCount = 0; partCount < numberOfParts; partCount++) {
          MimeBodyPart part = (MimeBodyPart) multiPart.getBodyPart(partCount);
          if (Part.ATTACHMENT.equalsIgnoreCase(part.getDisposition())) {
            // this part is attachment
            String fileName = part.getFileName();
            String fileExtension = part.getContentType();
            attachedFiles.append(fileName).append("*").append(getExtensionFromPartFile(fileExtension)).append(", ");
          }
        }

        if (attachedFiles.length() > 1) {
          attachedFiles = new StringBuilder(attachedFiles.substring(0, attachedFiles.length() - 2));
        }

        MimeMultipart mimeMultipart = (MimeMultipart) message.getContent();
        try {
          contentMail = getTextFromMimeMultipart(mimeMultipart);
        } catch (Exception e) {
          LOG.error("error when getting body mail content", e);
        }
        messageRestEntity.setMessageContent(contentMail);
        messageRestEntity.setAttachedFiles(attachedFiles.toString());
      }
      return messageRestEntity;
    }
    return null;
  }

  private static String getTextFromMimeMultipart(MimeMultipart mimeMultipart) throws Exception {
    StringBuilder result = new StringBuilder();
    int count = mimeMultipart.getCount();
    for (int i = 0; i < count; i++) {
      BodyPart bodyPart = mimeMultipart.getBodyPart(i);
      if (bodyPart.isMimeType("text/plain")) {
        result.append("\n").append(bodyPart.getContent());
        break; // without break same text appears twice in my tests
      } else if (bodyPart.isMimeType("text/html")) {
        String html = (String) bodyPart.getContent();
        result.append("\n").append(org.jsoup.Jsoup.parse(html).text());
      } else if (bodyPart.getContent() instanceof MimeMultipart) {
        result.append(getTextFromMimeMultipart((MimeMultipart) bodyPart.getContent()));
      }
    }
    return result.toString();
  }

  private static String getExtensionFromPartFile(String fileExtension) {
    String fileExtensionLowerCase = fileExtension.toLowerCase();
    if (fileExtensionLowerCase.contains("pdf")) {
      return "pdf";
    } else if(fileExtensionLowerCase.contains("image")) {
      return "image";
    } else if(fileExtensionLowerCase.contains("csv")) {
      return "csv";
    }else if(fileExtensionLowerCase.contains("powerpoint")) {
      return "powerpoint";
    } else if(fileExtensionLowerCase.contains("opendocument") || fileExtension.toLowerCase().contains("officedocument")) {
      return "word";
    } else if(fileExtensionLowerCase.contains("zip")) {
      return "archive";
    } else if(fileExtensionLowerCase.contains("video")) {
      return "video";
    }
    return "";
  }

}
