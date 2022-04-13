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
package org.exoplatform.mailintegration.storage;

import org.exoplatform.mailintegration.dao.MailIntegrationDAO;
import org.exoplatform.mailintegration.entity.MailIntegrationSettingEntity;
import org.exoplatform.mailintegration.model.MailIntegrationSetting;
import org.exoplatform.mailintegration.utils.EntityMapper;

import java.util.List;
import java.util.stream.Collectors;

public class MailIntegrationStorage {
  private MailIntegrationDAO mailIntegrationDAO;

  public MailIntegrationStorage(MailIntegrationDAO mailIntegrationDAO) {
    this.mailIntegrationDAO = mailIntegrationDAO;
  }

  public MailIntegrationSetting createMailIntegrationSetting(MailIntegrationSetting mailIntegrationSetting) {
    MailIntegrationSettingEntity mailIntegrationSettingEntity =
                                                              EntityMapper.toMailIntegrationSettingEntity(mailIntegrationSetting);
    mailIntegrationSettingEntity = mailIntegrationDAO.create(mailIntegrationSettingEntity);
    return EntityMapper.fromMailIntegrationSettingEntity(mailIntegrationSettingEntity);
  }

  public List<MailIntegrationSetting> getMailIntegrationSettingsByUserId(long userIdentityId) {
    List<MailIntegrationSettingEntity> mailIntegrationSettingEntities =
                                                                      mailIntegrationDAO.findMailIntegrationSettingsByUserId(userIdentityId);
    return mailIntegrationSettingEntities.stream()
                                         .map(EntityMapper::fromMailIntegrationSettingEntity)
                                         .collect(Collectors.toList());
  }
  
  public void deleteMailIntegrationSetting(long mailIntegrationSettingId) {
    MailIntegrationSettingEntity mailIntegrationSettingEntity = mailIntegrationDAO.find(mailIntegrationSettingId);
    mailIntegrationDAO.delete(mailIntegrationSettingEntity);
  }

  public List<MailIntegrationSetting> getMailIntegrationSettings() {
    List<MailIntegrationSettingEntity> mailIntegrationSettingEntities = mailIntegrationDAO.findAll();
    return mailIntegrationSettingEntities.stream()
                                         .map(EntityMapper::fromMailIntegrationSettingEntity)
                                         .collect(Collectors.toList());
  }

  public MailIntegrationSetting getMailIntegrationSettingById(long mailIntegrationSettingId) {
    return EntityMapper.fromMailIntegrationSettingEntity(mailIntegrationDAO.find(mailIntegrationSettingId));
  }

  public List<MailIntegrationSetting> getMailIntegrationSettingByMailIntegrationSettingIdAndUserId(long mailIntegrationSettingId,
                                                                                                   long userId) {
    List<MailIntegrationSettingEntity> mailIntegrationSettingEntities =
                                                                      mailIntegrationDAO.findMailIntegrationSettingsByIdAndUserId(mailIntegrationSettingId,
                                                                                                                                  userId);
    return mailIntegrationSettingEntities.stream()
                                         .map(EntityMapper::fromMailIntegrationSettingEntity)
                                         .collect(Collectors.toList());
  }

  public MailIntegrationSetting updateMailIntegrationSetting(MailIntegrationSetting mailIntegrationSetting) {
    MailIntegrationSettingEntity mailIntegrationSettingEntity =
                                                              EntityMapper.toMailIntegrationSettingEntity(mailIntegrationSetting);
    mailIntegrationSettingEntity = mailIntegrationDAO.update(mailIntegrationSettingEntity);
    return EntityMapper.fromMailIntegrationSettingEntity(mailIntegrationSettingEntity);
  }

}
