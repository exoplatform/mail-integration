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
package org.exoplatform.mailIntegration.service;

import org.exoplatform.mailIntegration.model.ConnectionInformation;
import org.junit.Test;

import static org.junit.Assert.*;

public class MailIntegrationServiceTest extends BaseMailIntegrationTest {

  @Test
  public void testCreateConnectionInformation() throws IllegalAccessException {
    ConnectionInformation connectionInformation = new ConnectionInformation();
    connectionInformation.setEmailName("emailName");
    connectionInformation.setImapUrl("imapUrl");
    connectionInformation.setPort(123);
    connectionInformation.setEncryption("encryption");
    connectionInformation.setAccount("account");
    connectionInformation.setPassword("password");
    connectionInformation.setCreatorId(Long.parseLong(user1Identity.getId()));

    // When
    ConnectionInformation createdConnection = mailIntegrationService.createMailIntegration(connectionInformation,
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
