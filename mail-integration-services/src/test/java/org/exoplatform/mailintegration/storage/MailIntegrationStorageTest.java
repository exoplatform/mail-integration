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
package org.exoplatform.mailintegration.storage;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;
import static org.mockito.ArgumentMatchers.anyObject;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

import org.exoplatform.mailintegration.dao.MailIntegrationDAO;
import org.exoplatform.mailintegration.entity.MailIntegrationSettingEntity;
import org.exoplatform.mailintegration.utils.EntityMapper;
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
    org.exoplatform.mailintegration.model.MailIntegrationSetting connectionInformation = createConnectionInformation();
    MailIntegrationSettingEntity mailIntegrationSettingEntity = createMailIntegrationSettingEntity();

    // When
    when(mailIntegrationDAO.create(anyObject())).thenReturn(mailIntegrationSettingEntity);
    PowerMockito.mockStatic(EntityMapper.class);
    when(EntityMapper.toMailIntegrationSettingEntity(connectionInformation)).thenReturn(mailIntegrationSettingEntity);
    when(EntityMapper.fromMailIntegrationSettingEntity(mailIntegrationSettingEntity)).thenReturn(connectionInformation);

    // Then
    org.exoplatform.mailintegration.model.MailIntegrationSetting connectionCreated = mailIntegrationStorage.createMailIntegration(connectionInformation);
    assertNotNull(connectionCreated);
    assertEquals(1L, connectionCreated.getId());
    assertEquals(connectionInformation, connectionCreated);
  }

  protected org.exoplatform.mailintegration.model.MailIntegrationSetting createConnectionInformation() {
    org.exoplatform.mailintegration.model.MailIntegrationSetting connectionInformation = new org.exoplatform.mailintegration.model.MailIntegrationSetting();
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

  protected MailIntegrationSettingEntity createMailIntegrationSettingEntity() {
    MailIntegrationSettingEntity mailIntegrationSettingEntity = new MailIntegrationSettingEntity();
    mailIntegrationSettingEntity.setId(1L);
    mailIntegrationSettingEntity.setEmailName("emailName");
    mailIntegrationSettingEntity.setImapUrl("imapUrl");
    mailIntegrationSettingEntity.setPort(123);
    mailIntegrationSettingEntity.setEncryption("encryption");
    mailIntegrationSettingEntity.setAccount("account");
    mailIntegrationSettingEntity.setPassword("password");
    mailIntegrationSettingEntity.setCreatorId(1L);
    return mailIntegrationDAO.create(mailIntegrationSettingEntity);
  }

}
