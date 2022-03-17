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
package org.exoplatform.mailintegration.notification.utils;

public class NotificationConstants {

  public static final String CONTEXT           = "CONTEXT";

  public static final String LAST_UPDATED_TIME = "LAST_UPDATED_TIME";

  public static final String NEW_MESSAGES      = "NEW_MESSAGES";

  public static final String NOTIFICATION_ID   = "NOTIFICATION_ID";

  public static final String READ              = "READ";

  public static final String RECEIVER          = "RECEIVER";
  
  private NotificationConstants () {
    
  }

  public enum NOTIFICATION_CONTEXT {
    NEW_EMAILS_RECIEVED("NEW EMAILS RECIEVED");

    private String context;

    private NOTIFICATION_CONTEXT(String context) {
      this.context = context;
    }

    public String getContext() {
      return this.context;
    }
  }
}
