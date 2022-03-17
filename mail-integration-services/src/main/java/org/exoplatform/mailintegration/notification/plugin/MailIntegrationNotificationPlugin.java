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
package org.exoplatform.mailintegration.notification.plugin;

import org.exoplatform.commons.api.notification.NotificationContext;
import org.exoplatform.commons.api.notification.model.ArgumentLiteral;
import org.exoplatform.commons.api.notification.model.NotificationInfo;
import org.exoplatform.commons.api.notification.plugin.BaseNotificationPlugin;
import org.exoplatform.container.xml.InitParams;
import org.exoplatform.mailintegration.notification.utils.NotificationConstants;

public class MailIntegrationNotificationPlugin extends BaseNotificationPlugin {

  public static final  String                  ID               = "MailIntegrationNotificationPlugin";

  public static final ArgumentLiteral<NotificationConstants.NOTIFICATION_CONTEXT> CONTEXT          = new ArgumentLiteral<>(NotificationConstants.NOTIFICATION_CONTEXT.class, "CONTEXT");
  
  public static final ArgumentLiteral<String> RECEIVER    = new ArgumentLiteral<>(String.class, NotificationConstants.RECEIVER);
  
  public static final ArgumentLiteral<String> NEW_MESSAGES    = new ArgumentLiteral<>(String.class, NotificationConstants.NEW_MESSAGES);
  
  public MailIntegrationNotificationPlugin(InitParams initParams) {
    super(initParams);
  }

  @Override
  public String getId() {
    return ID;
  }

  @Override
  public boolean isValid(NotificationContext ctx) {
    return true;
  }

  @Override
  protected NotificationInfo makeNotification(NotificationContext ctx) {
    NotificationConstants.NOTIFICATION_CONTEXT context = ctx.value(CONTEXT);
    String receiver = ctx.value(RECEIVER);
    String newMessages = ctx.value(NEW_MESSAGES);
    return NotificationInfo.instance()
                           .setFrom("")
                           .to(receiver)
                           .with(NotificationConstants.CONTEXT, context.getContext())
                           .with(NotificationConstants.NEW_MESSAGES, newMessages)
                           .key(getKey())
                           .end();

  }
}
