/*
 * This file is part of the Meeds project (https://meeds.io/).
 *
 * Copyright (C) 2022 Meeds Association contact@meeds.io
 *
 * This program is free software; you can redistribute it and/or
 * modify it under the terms of the GNU Lesser General Public
 * License as published by the Free Software Foundation; either
 * version 3 of the License, or (at your option) any later version.
 * This program is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the GNU
 * Lesser General Public License for more details.
 *
 * You should have received a copy of the GNU Lesser General Public License
 * along with this program; if not, write to the Free Software Foundation,
 * Inc., 51 Franklin Street, Fifth Floor, Boston, MA  02110-1301, USA.
 */
package org.exoplatform.mailintegration.utils;

import org.exoplatform.mailintegration.entity.MailIntegrationSettingEntity;
import org.exoplatform.mailintegration.model.MailIntegrationSetting;
import org.exoplatform.mailintegration.model.MailIntegrationUserSetting;

public class EntityMapper {
  public EntityMapper() {
  }
  
  public static MailIntegrationSetting fromMailIntegrationSettingEntity(MailIntegrationSettingEntity connectionInformationEntity) {
      if(connectionInformationEntity == null) {
          return null;
      }
      return new MailIntegrationSetting(connectionInformationEntity.getId(),
                                       connectionInformationEntity.getEmailName(),
                                       connectionInformationEntity.getImapUrl(),
                                       connectionInformationEntity.getPort(),
                                       connectionInformationEntity.getEncryption(),
                                       connectionInformationEntity.getAccount(),
                                       connectionInformationEntity.getPassword(),
                                       connectionInformationEntity.getCreatorId());
      
  }

  public static MailIntegrationSettingEntity toMailIntegrationSettingEntity(MailIntegrationSetting mailIntegrationSetting) {
      if(mailIntegrationSetting == null) {
          return null;
      }
      MailIntegrationSettingEntity mailIntegrationSettingEntity = new MailIntegrationSettingEntity();
      mailIntegrationSettingEntity.setId(mailIntegrationSetting.getId() == 0 ? null : mailIntegrationSetting.getId());
      mailIntegrationSettingEntity.setEmailName(mailIntegrationSetting.getEmailName());
      mailIntegrationSettingEntity.setImapUrl(mailIntegrationSetting.getImapUrl());
      mailIntegrationSettingEntity.setPort(mailIntegrationSetting.getPort());
      mailIntegrationSettingEntity.setEncryption(mailIntegrationSetting.getEncryption());
      mailIntegrationSettingEntity.setAccount(mailIntegrationSetting.getAccount());
      mailIntegrationSettingEntity.setPassword(mailIntegrationSetting.getPassword());
      mailIntegrationSettingEntity.setCreatorId(mailIntegrationSetting.getCreatorId());
      return mailIntegrationSettingEntity;
  }

  public static MailIntegrationUserSetting fromMailIntegrationUserSettingEntity(MailIntegrationSettingEntity mailIntegrationSettingEntity) {
    if (mailIntegrationSettingEntity == null) {
      return null;
    }
    return new MailIntegrationUserSetting(mailIntegrationSettingEntity.getId(),
                                          mailIntegrationSettingEntity.getCreatorId(),
                                          mailIntegrationSettingEntity.getId());

  }

}
