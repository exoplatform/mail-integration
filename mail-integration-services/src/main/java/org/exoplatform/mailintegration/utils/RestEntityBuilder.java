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

import org.exoplatform.mailintegration.model.MailIntegrationSetting;
import org.exoplatform.mailintegration.rest.model.MailIntegrationSettingRestEntity;
import org.exoplatform.mailintegration.rest.model.MessageRestEntity;

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

  public static final MessageRestEntity fromMessage(Message message) throws MessagingException {
    if (message != null) {
      return new MessageRestEntity(message.getSubject(), message.getSentDate(), message.getFrom()[0].toString());
    }
    return null;
  }

}
