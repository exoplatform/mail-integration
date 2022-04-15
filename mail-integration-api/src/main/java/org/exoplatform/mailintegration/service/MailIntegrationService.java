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

import org.exoplatform.mailintegration.model.MailIntegrationSetting;
import org.exoplatform.mailintegration.rest.model.MessageRestEntity;

import javax.mail.Store;
import java.util.List;

public interface MailIntegrationService {
  /**
   * Creates a new mail integration setting
   *
   * @param mailIntegrationSetting {@link MailIntegrationSetting} object to create
   * @return created {@link MailIntegrationSetting} with generated technical identifier
   */
  MailIntegrationSetting createMailIntegrationSetting(MailIntegrationSetting mailIntegrationSetting);

  /**
   * Retrieves mail integration setting by its technical user identity identifier.
   *
   * @param userIdentityId User identity getting the mail integration setting
   * @return A {@link List} of {@link MailIntegrationSetting} objects
   */
  List<MailIntegrationSetting> getMailIntegrationSettingsByUserId(long userIdentityId);
  
  /**
   * Retrieves mail integration setting by its technical identify and user identity identifier.
   *
   * @param mailIntegrationSettingId {@link MailIntegrationSetting} technical identifier to retrieve
   * @return {@link MailIntegrationSetting} object
   */
  MailIntegrationSetting getMailIntegrationSetting(long mailIntegrationSettingId);
  
  /**
   * Deletes a mail integration setting
   * 
   * @param mailIntegrationSettingId {@link MailIntegrationSetting} technical identifier to delete
   * @param userIdentityId User identity deleting the mail integration setting
   * @throws IllegalAccessException when the user is not authorized to delete mail integration setting
   */
  void deleteMailIntegrationSetting(long mailIntegrationSettingId, long userIdentityId) throws IllegalAccessException;

  /**
   * Connect to a mail integration setting
   *
   * @param mailIntegrationSetting {@link MailIntegrationSetting} object to connect
   * @return connected store
   */
  Store connect(MailIntegrationSetting mailIntegrationSetting);

  /**
   * Send mail integration notification
   * 
   */
  void sendMailIntegrationNotifications();

  /**
   * Retrieves message object by its technical identifier and the mail integration setting technical identifier
   *
   * @param mailIntegrationSettingId {@link MailIntegrationSetting} technical identifier
   * @param messageId {@link Message} technical identifier
   * @param userIdentityId User identity getting the message
   * @return {@link Message} object
   * @throws IllegalAccessException when the user is not authorized to get message
   */
  MessageRestEntity getMessageById(long mailIntegrationSettingId,
                                   String messageId,
                                   long userIdentityId) throws IllegalAccessException;

  /**
   * Update mail integration setting
   *
   * @param mailIntegrationSetting {@link MailIntegrationSetting} object to update
   * @param userIdentityId User identity updating the mail integration setting
   * @return updated {@link MailIntegrationSetting} object
   * @throws IllegalAccessException when the user is not authorized to update mail integration setting
   */
  MailIntegrationSetting updateMailIntegrationSetting(MailIntegrationSetting mailIntegrationSetting, long userIdentityId) throws IllegalAccessException;
}
