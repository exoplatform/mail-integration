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
package org.exoplatform.mailIntegration.storage;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;
import static org.mockito.ArgumentMatchers.anyObject;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

import org.exoplatform.mailIntegration.dao.MailIntegrationDAO;
import org.exoplatform.mailIntegration.entity.ConnectionInformationEntity;
import org.exoplatform.mailIntegration.model.ConnectionInformation;
import org.exoplatform.mailIntegration.utils.EntityMapper;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.powermock.api.mockito.PowerMockito;
import org.powermock.core.classloader.annotations.PowerMockIgnore;
import org.powermock.core.classloader.annotations.PrepareForTest;
import org.powermock.modules.junit4.PowerMockRunner;

@RunWith(PowerMockRunner.class)
@PowerMockIgnore({ "javax.management.*", "javax.xml.*", "org.xml.*" })
public class MailIntegrationStorageTest {
  private MailIntegrationStorage mailIntegrationStorage;

  private MailIntegrationDAO     mailIntegrationDAO;

  @Before
  public void setUp() throws Exception { // NOSONAR
    mailIntegrationDAO = mock(MailIntegrationDAO.class);
    mailIntegrationStorage = new MailIntegrationStorage(mailIntegrationDAO);
  }

  @PrepareForTest({ EntityMapper.class })
  @Test
  public void testCreatePoll() throws Exception { // NOSONAR
    // Given
    ConnectionInformation connectionInformation = createConnectionInformation();
    ConnectionInformationEntity connectionInformationEntity = createConnectionInformationEntity();

    // When
    when(mailIntegrationDAO.create(anyObject())).thenReturn(connectionInformationEntity);
    PowerMockito.mockStatic(EntityMapper.class);
    when(EntityMapper.toConnectionInformationEntity(connectionInformation)).thenReturn(connectionInformationEntity);
    when(EntityMapper.fromConnectionInformationEntity(connectionInformationEntity)).thenReturn(connectionInformation);

    // Then
    ConnectionInformation connectionCreated = mailIntegrationStorage.createMailIntegration(connectionInformation);
    assertNotNull(connectionCreated);
    assertEquals(1L, connectionCreated.getId());
    assertEquals(connectionInformation, connectionCreated);
  }

  protected ConnectionInformation createConnectionInformation() {
    ConnectionInformation connectionInformation = new ConnectionInformation();
    connectionInformation.setId(1L);
    connectionInformation.setEmailName("emailName");
    connectionInformation.setImapUrl("imapUrl");
    connectionInformation.setPort(123);
    connectionInformation.setEncryption("encryption");
    connectionInformation.setAccount("account");
    connectionInformation.setPassword("password");
    connectionInformation.setCreatorId(1L);
    return connectionInformation;
  }

  protected ConnectionInformationEntity createConnectionInformationEntity() {
    ConnectionInformationEntity connectionInformationEntity = new ConnectionInformationEntity();
    connectionInformationEntity.setId(1L);
    connectionInformationEntity.setEmailName("emailName");
    connectionInformationEntity.setImapUrl("imapUrl");
    connectionInformationEntity.setPort(123);
    connectionInformationEntity.setEncryption("encryption");
    connectionInformationEntity.setAccount("account");
    connectionInformationEntity.setPassword("password");
    connectionInformationEntity.setCreatorId(1L);
    return mailIntegrationDAO.create(connectionInformationEntity);
  }

}
