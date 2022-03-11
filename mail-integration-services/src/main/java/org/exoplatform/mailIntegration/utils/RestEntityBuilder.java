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
package org.exoplatform.mailIntegration.utils;

import javax.mail.Message;
import javax.mail.MessagingException;

import org.exoplatform.mailIntegration.rest.model.MessageRestEntity;

public class RestEntityBuilder {
  
  private RestEntityBuilder() {
  }
  
  public static final MessageRestEntity fromMessage(Message message) throws MessagingException {
    if (message != null) {
      return new MessageRestEntity(message.getSubject(), message.getSentDate(), message.getFrom()[0].toString());
    }
    return null;
  }
}
