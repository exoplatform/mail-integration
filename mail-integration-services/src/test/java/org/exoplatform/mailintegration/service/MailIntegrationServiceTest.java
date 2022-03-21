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
import org.junit.Test;

import static org.junit.Assert.*;

public class MailIntegrationServiceTest extends BaseMailIntegrationTest {

  @Test
  public void testCreateConnectionInformation() throws IllegalAccessException {
    MailIntegrationSetting connectionInformation = new MailIntegrationSetting();
    connectionInformation.setEmailName("emailName");
    connectionInformation.setImapUrl("imapUrl");
    connectionInformation.setPort(123);
    connectionInformation.setEncryption("encryption");
    connectionInformation.setAccount("account");
    connectionInformation.setPassword("password");
    connectionInformation.setCreatorId(Long.parseLong(user1Identity.getId()));

    // When
    MailIntegrationSetting createdConnection = mailIntegrationService.createMailIntegration(connectionInformation,
                                                                                           Long.parseLong(user1Identity.getId()));

    assertNotNull(createdConnection);
    assertEquals(connectionInformation.getEmailName(), createdConnection.getEmailName());
    assertEquals(connectionInformation.getImapUrl(), createdConnection.getImapUrl());
    assertEquals(connectionInformation.getPort(), createdConnection.getPort());
    assertEquals(connectionInformation.getEncryption(), createdConnection.getEncryption());
    assertEquals(connectionInformation.getAccount(), createdConnection.getAccount());
    assertEquals(connectionInformation.getPassword(), createdConnection.getPassword());
    assertEquals(connectionInformation.getCreatorId(), createdConnection.getCreatorId());

  }
}
