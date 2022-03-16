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