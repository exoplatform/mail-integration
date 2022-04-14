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
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.Mockito;
import org.powermock.api.mockito.PowerMockito;
import org.powermock.core.classloader.annotations.PowerMockIgnore;
import org.powermock.core.classloader.annotations.PrepareForTest;
import org.powermock.modules.junit4.PowerMockRunner;

import org.exoplatform.mailintegration.dao.MailIntegrationDAO;
import org.exoplatform.mailintegration.entity.MailIntegrationSettingEntity;
import org.exoplatform.mailintegration.model.MailIntegrationSetting;
import org.exoplatform.mailintegration.utils.EntityMapper;

import java.util.ArrayList;
import java.util.List;

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
  public void testCreateMailIntegrationSetting() throws Exception { // NOSONAR
    // Given
    MailIntegrationSetting mailIntegrationSetting = createMailIntegrationSetting();
    MailIntegrationSettingEntity mailIntegrationSettingEntity = createMailIntegrationSettingEntity();
    when(mailIntegrationDAO.create(Mockito.any())).thenReturn(mailIntegrationSettingEntity);
    PowerMockito.mockStatic(EntityMapper.class);
    when(EntityMapper.toMailIntegrationSettingEntity(mailIntegrationSetting)).thenReturn(mailIntegrationSettingEntity);
    when(EntityMapper.fromMailIntegrationSettingEntity(mailIntegrationSettingEntity)).thenReturn(mailIntegrationSetting);

    // When
    MailIntegrationSetting createdMailIntegrationSetting = mailIntegrationStorage.createMailIntegrationSetting(mailIntegrationSetting);

    // Then
    assertNotNull(createdMailIntegrationSetting);
    assertEquals(1L, createdMailIntegrationSetting.getId());
  }
  
  @Test
  public void testGetMailIntegrationSettingsByUserId() throws Exception { // NOSONAR
    // Given
    MailIntegrationSettingEntity mailIntegrationSettingEntity = createMailIntegrationSettingEntity();
    List<MailIntegrationSettingEntity> mailIntegrationSettingEntities = new ArrayList<>();
    mailIntegrationSettingEntities.add(mailIntegrationSettingEntity);
    when(mailIntegrationDAO.findMailIntegrationSettingsByUserId(1L)).thenReturn(mailIntegrationSettingEntities);

    // When
    List<MailIntegrationSetting> retrievedMailIntegrationSettings = mailIntegrationStorage.getMailIntegrationSettingsByUserId(1L);

    // Then
    assertNotNull(retrievedMailIntegrationSettings);
    assertEquals(mailIntegrationSettingEntities.size(), retrievedMailIntegrationSettings.size());
  }
  
  @Test
  public void testGetMailIntegrationSettings() throws Exception { // NOSONAR
    // Given
    MailIntegrationSettingEntity mailIntegrationSettingEntity = createMailIntegrationSettingEntity();
    List<MailIntegrationSettingEntity> mailIntegrationSettingEntities = new ArrayList<>();
    mailIntegrationSettingEntities.add(mailIntegrationSettingEntity);
    when(mailIntegrationDAO.findAll()).thenReturn(mailIntegrationSettingEntities);

    // When
    List<MailIntegrationSetting> retrievedMailIntegrationSettings = mailIntegrationStorage.getMailIntegrationSettings();

    // Then
    assertNotNull(retrievedMailIntegrationSettings);
    assertEquals(mailIntegrationSettingEntities.size(), retrievedMailIntegrationSettings.size());
  }

  @PrepareForTest({ EntityMapper.class })
  @Test
  public void testUpdatedMailIntegrationSetting() throws Exception { // NOSONAR
    // Given
    MailIntegrationSetting mailIntegrationSetting = createMailIntegrationSetting();
    MailIntegrationSettingEntity mailIntegrationSettingEntity = createMailIntegrationSettingEntity();
    when(mailIntegrationDAO.create(Mockito.any())).thenReturn(mailIntegrationSettingEntity);
    PowerMockito.mockStatic(EntityMapper.class);
    when(EntityMapper.toMailIntegrationSettingEntity(mailIntegrationSetting)).thenReturn(mailIntegrationSettingEntity);
    when(EntityMapper.fromMailIntegrationSettingEntity(mailIntegrationSettingEntity)).thenReturn(mailIntegrationSetting);
    MailIntegrationSetting createdMailIntegrationSetting = mailIntegrationStorage.createMailIntegrationSetting(mailIntegrationSetting);
    createdMailIntegrationSetting.setAccount("updatedAccount");
    createdMailIntegrationSetting.setEmailName("useraUpdatedMail@exo.tn");

    // When
    MailIntegrationSetting updatedMailIntegrationSetting = mailIntegrationStorage.updateMailIntegrationSetting(mailIntegrationSetting);

    // Then
    assertNotNull(updatedMailIntegrationSetting);
    assertEquals(1L, updatedMailIntegrationSetting.getId());
    assertEquals("updatedAccount", updatedMailIntegrationSetting.getAccount());
    assertEquals("useraUpdatedMail@exo.tn", updatedMailIntegrationSetting.getEmailName());
  }
  
  protected MailIntegrationSetting createMailIntegrationSetting() {
    MailIntegrationSetting mailIntegrationSetting = new MailIntegrationSetting();
    mailIntegrationSetting.setId(1L);
    mailIntegrationSetting.setEmailName("emailName");
    mailIntegrationSetting.setImapUrl("imapUrl");
    mailIntegrationSetting.setPort(123);
    mailIntegrationSetting.setEncryption("encryption");
    mailIntegrationSetting.setAccount("account");
    mailIntegrationSetting.setPassword("password");
    mailIntegrationSetting.setUserId(1L);
    return mailIntegrationSetting;
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
    mailIntegrationSettingEntity.setUserId(1L);
    return mailIntegrationDAO.create(mailIntegrationSettingEntity);
  }

}
