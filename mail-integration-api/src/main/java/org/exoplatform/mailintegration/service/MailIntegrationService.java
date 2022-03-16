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
package org.exoplatform.mailintegration.service;

import org.exoplatform.mailintegration.model.MailIntegrationSetting;
import org.exoplatform.mailintegration.rest.model.MessageRestEntity;
import org.exoplatform.services.security.Identity;

import javax.mail.Store;
import java.util.List;

public interface MailIntegrationService {
  /**
   * @param connectionInformation {@link MailIntegrationSetting} object to create
   * @param identityId User identity creating the poll
   * @return created {@link MailIntegrationSetting} with generated technical identifier
   * @throws IllegalAccessException
   */
  MailIntegrationSetting createMailIntegration(MailIntegrationSetting connectionInformation,
                                               long identityId) throws IllegalAccessException;

  /**
   *
   * @param mailIntegrationSetting {@link MailIntegrationSetting} object to connect
   * @return
   */
  Store imapConnect(MailIntegrationSetting mailIntegrationSetting);

  /**
   * Retrieves connection information by its technical user identity identifier.
   *
   * @param currentIdentityId User identity creating the poll
   * @return A {@link MailIntegrationSetting} object
   * @throws IllegalAccessException when user is not authorized to get a poll options
   */
  List<MailIntegrationSetting> getMailIntegrationSettingsByUserId(long currentIdentityId) throws IllegalAccessException;

  void sendMailIntegrationNotifications();

  MessageRestEntity getMessageById(long mailIntegrationSettingId, String messageId, Identity currentIdentity) throws IllegalAccessException;

  /**
   * Check if user already connected
   * 
   * @param mailIntegrationSetting
   * @return flag to identify connection status
   */
  boolean isConnected(MailIntegrationSetting mailIntegrationSetting);
}
