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

import javax.mail.Store;

import org.exoplatform.mailintegration.model.MailIntegrationSetting;
import org.exoplatform.mailintegration.rest.model.MessageRestEntity;
import org.exoplatform.services.security.Identity;

public interface MailIntegrationService {
  
  Store imapConnect(MailIntegrationSetting mailIntegrationSetting);
  
  void sendMailIntegrationNotifications();
  
  MessageRestEntity getMessageById(String mailntegrationSettingId, String messageId, Identity currentIdentity) throws IllegalAccessException;
}
