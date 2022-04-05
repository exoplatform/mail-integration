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
import MailIntegrationNotification from './components/MailIntegrationNotification.vue';
import MailIntegrationNotificationContentItem from './components/MailIntegrationNotificationContentItem.vue';
import MailIntegrationNotificationContentItemDetails from './components/MailIntegrationNotificationContentItemDetails.vue';
import MailIntegrationNotificationAttachments from './components/MailIntegrationNotificationAttachments.vue';

const components = {
  'mail-integration-notification': MailIntegrationNotification,
  'mail-integration-notification-content-item': MailIntegrationNotificationContentItem,
  'mail-integration-notification-content-item-details': MailIntegrationNotificationContentItemDetails,
  'mail-integration-notification-attachments': MailIntegrationNotificationAttachments,
};

for (const key in components) {
  Vue.component(key, components[key]);
}