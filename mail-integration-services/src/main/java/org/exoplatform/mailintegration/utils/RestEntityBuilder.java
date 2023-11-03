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

import org.apache.commons.io.IOUtils;
import org.exoplatform.mailintegration.model.MailIntegrationSetting;
import org.exoplatform.mailintegration.rest.model.MailIntegrationSettingRestEntity;
import org.exoplatform.mailintegration.rest.model.MessageRestEntity;
import org.exoplatform.services.log.ExoLogger;
import org.exoplatform.services.log.Log;

import java.io.IOException;
import java.io.InputStream;
import java.util.Base64;
import java.util.HashMap;
import java.util.Map;

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
    if (mailIntegrationSettingRestEntity.getId() > 0) {
      mailIntegrationSetting.setId(mailIntegrationSettingRestEntity.getId());
    }
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
      String body = "";
      // store attachment file name, separated by comma
      StringBuilder attachedFiles = new StringBuilder();
      Map<String,String> inlineAttachments = new HashMap<>();
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
          } else if (Part.INLINE.equalsIgnoreCase(part.getDisposition())) {
            String contentId = part.getContentID().replaceAll("<","").replaceAll(">","");
            InputStream content = (InputStream) part.getContent();
            byte[] sourceBytes = IOUtils.toByteArray(content);
            String encodedString = part.getContentType()+";base64,"+Base64.getEncoder().encodeToString(sourceBytes);
            inlineAttachments.put(contentId,encodedString);
          }
        }

        if (attachedFiles.length() > 1) {
          attachedFiles = new StringBuilder(attachedFiles.substring(0, attachedFiles.length() - 2));
        }

        MimeMultipart mimeMultipart = (MimeMultipart) message.getContent();
        try {
          body = getTextFromMimeMultipart(mimeMultipart);
        } catch (Exception e) {
          LOG.error("error when getting body mail content", e);
        }
        if (!inlineAttachments.isEmpty()) {
          body = replaceInlineAttachmentsInBody(body,inlineAttachments);
        }
        messageRestEntity.setBody(body);
        messageRestEntity.setAttachedFiles(attachedFiles.toString());
      } else if (contentType.toLowerCase().contains("text/plain") || contentType.toLowerCase().contains("text/html")) {
        body = message.getContent().toString();
        messageRestEntity.setBody(body);
      }
      return messageRestEntity;
    }
    return null;
  }

  private static String replaceInlineAttachmentsInBody(String body, Map<String, String> inlineAttachments) {
    for (Map.Entry<String, String> entry : inlineAttachments.entrySet()) {
      String key = "cid:"+entry.getKey();
      String value = "data:"+entry.getValue();
      body=body.replace(key, value);
    }
    return body;

  }

  private static String getTextFromMimeMultipart(MimeMultipart mimeMultipart) throws Exception {
    StringBuilder resultText = new StringBuilder();
    StringBuilder resultHtml = new StringBuilder();
    StringBuilder result = new StringBuilder();
    int count = mimeMultipart.getCount();
    for (int i = 0; i < count; i++) {
      BodyPart bodyPart = mimeMultipart.getBodyPart(i);
      if (bodyPart.isMimeType("text/plain")) {
        resultText.append(bodyPart.getContent());
      } else if (bodyPart.isMimeType("text/html")) {
        String html = (String) bodyPart.getContent();
        resultHtml.append(html);
      } else if (bodyPart.getContent() instanceof MimeMultipart) {
        result.append(getTextFromMimeMultipart((MimeMultipart) bodyPart.getContent()));
      }
    }
    if (!resultHtml.toString().isEmpty()) {
      return resultHtml.toString();
    } else if (!resultText.toString().isEmpty()) {
      return resultText.toString().replaceAll("\\n|\\r", "<br>");
    } else {
    return result.toString();
    }
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
