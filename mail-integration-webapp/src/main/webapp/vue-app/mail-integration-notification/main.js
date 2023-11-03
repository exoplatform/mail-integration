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
import './initComponents.js';
import './extensions.js';
import * as mailIntegrationService from '../service/mailIntegrationService.js';

//getting language of the PLF
const lang = eXo && eXo.env.portal.language || 'en';

//should expose the locale ressources as REST API
const urls = [
  `${eXo.env.portal.context}/${eXo.env.portal.rest}/i18n/bundle/locale.portlet.MailIntegration-${lang}.json`,
  `${eXo.env.portal.context}/${eXo.env.portal.rest}/i18n/bundle/locale.notification.MailIntegrationNotification-${lang}.json`
];

if (!Vue.prototype.$mailIntegrationService) {
  window.Object.defineProperty(Vue.prototype, '$mailIntegrationService', {
    value: mailIntegrationService,
  });
}

export function init() {
  return exoi18n.loadLanguageAsync(lang, urls).then(() => {
    new Vue({
      i18n: exoi18n.i18n,
    });
  });
}
