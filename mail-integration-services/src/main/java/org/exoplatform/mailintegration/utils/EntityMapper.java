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

import org.exoplatform.mailintegration.entity.MailIntegrationSettingEntity;
import org.exoplatform.mailintegration.model.MailIntegrationSetting;

public class EntityMapper {
  private EntityMapper() {
  }

  public static MailIntegrationSetting fromMailIntegrationSettingEntity(MailIntegrationSettingEntity connectionInformationEntity) {
    if (connectionInformationEntity == null) {
      return null;
    }
    return new MailIntegrationSetting(connectionInformationEntity.getId(),
                                      connectionInformationEntity.getEmailName(),
                                      connectionInformationEntity.getImapUrl(),
                                      connectionInformationEntity.getPort(),
                                      connectionInformationEntity.getEncryption(),
                                      connectionInformationEntity.getAccount(),
                                      connectionInformationEntity.getPassword(),
                                      connectionInformationEntity.getUserId());

  }

  public static MailIntegrationSettingEntity toMailIntegrationSettingEntity(MailIntegrationSetting mailIntegrationSetting) {
    if (mailIntegrationSetting == null) {
      return null;
    }
    MailIntegrationSettingEntity mailIntegrationSettingEntity = new MailIntegrationSettingEntity();
    if (mailIntegrationSetting.getId() != 0) {
      mailIntegrationSettingEntity.setId(mailIntegrationSetting.getId());
    }
    mailIntegrationSettingEntity.setEmailName(mailIntegrationSetting.getEmailName());
    mailIntegrationSettingEntity.setImapUrl(mailIntegrationSetting.getImapUrl());
    mailIntegrationSettingEntity.setPort(mailIntegrationSetting.getPort());
    mailIntegrationSettingEntity.setEncryption(mailIntegrationSetting.getEncryption());
    mailIntegrationSettingEntity.setAccount(mailIntegrationSetting.getAccount());
    mailIntegrationSettingEntity.setPassword(mailIntegrationSetting.getPassword());
    mailIntegrationSettingEntity.setUserId(mailIntegrationSetting.getUserId());
    return mailIntegrationSettingEntity;
  }
}
