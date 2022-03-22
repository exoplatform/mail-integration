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
export function checkMailConnection(connectionInformationEntity) {
  return fetch(`${eXo.env.portal.context}/${eXo.env.portal.rest}/v1/mailIntegration/checkMailConnection`, {
    headers: {
      'Content-Type': 'application/json'
    },
    credentials: 'include',
    method: 'POST',
    body: JSON.stringify(connectionInformationEntity)
  }).then(resp => {
    if (resp && resp.ok) {
      return resp.text();
    } else {
      throw new Error('Response code indicates a server error', resp);
    }
  });
}

export function createMailIntegrationSettings(mailIntegrationSettings) {
  return fetch(`${eXo.env.portal.context}/${eXo.env.portal.rest}/v1/mailIntegration`, {
    headers: {
      'Content-Type': 'application/json'
    },
    credentials: 'include',
    method: 'POST',
    body: JSON.stringify(mailIntegrationSettings)
  }).then(resp => {
    if (resp && resp.ok) {
      return resp.text();
    } else {
      throw new Error('Response code indicates a server error', resp);
    }
  });
}

export function getMailIntegrationSettingsByUserId() {
  return fetch(`${eXo.env.portal.context}/${eXo.env.portal.rest}/v1/mailIntegration/mailIntegrationSettings`, {
    credentials: 'include',
    method: 'GET',
  }).then(resp => {
    if (resp && resp.ok) {
      return resp.json();
    } else {
      throw new Error('Response code indicates a server error', resp);
    }
  });
}

export function getMessageById(mailntegrationSettingId, messageId) {
  return fetch(`${eXo.env.portal.context}/${eXo.env.portal.rest}/v1/mailIntegration/${mailntegrationSettingId}/message/${messageId}`, {
    credentials: 'include',
    method: 'GET',
  }).then(resp => {
    if (!resp || !resp.ok) {
      throw new Error('Response code indicates a server error', resp);
    } else {
      return resp.json();
    }
  });
}