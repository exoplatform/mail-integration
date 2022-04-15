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
package org.exoplatform.mailintegration.dao;

import java.util.List;

import org.exoplatform.container.ExoContainerContext;
import org.exoplatform.container.PortalContainer;
import org.exoplatform.container.RootContainer;
import org.exoplatform.container.component.RequestLifeCycle;
import org.exoplatform.mailintegration.entity.MailIntegrationSettingEntity;
import org.exoplatform.services.naming.InitialContextInitializer;

import junit.framework.TestCase;

public class MailIntegrationDaoTest extends TestCase {
  private PortalContainer    container;

  private String             emailName  = "user1@exo.tn";

  private String             imapUrl    = "imap.gmail.com";

  private long               port       = 123;

  private String             encryption = "SSL";

  private String             account    = "user1@exo.tn";

  private String             password   = "123456";

  private long               userId     = 1L;

  private MailIntegrationDAO mailIntegrationDAO;

  @Override
  protected void setUp() throws Exception {
    RootContainer rootContainer = RootContainer.getInstance();
    rootContainer.getComponentInstanceOfType(InitialContextInitializer.class);
    container = PortalContainer.getInstance();
    mailIntegrationDAO = container.getComponentInstanceOfType(MailIntegrationDAO.class);
    ExoContainerContext.setCurrentContainer(container);
    begin();
  }

  public void testCreateMailIntegrationSetting() {
    //Given
    MailIntegrationSettingEntity createdMailIntegrationSettingEntity = createMailIntegrationSettingEntity();
    
    // When
    createdMailIntegrationSettingEntity = mailIntegrationDAO.create(createdMailIntegrationSettingEntity);
    
    // Then
    assertNotNull(createdMailIntegrationSettingEntity);
    assertNotNull(createdMailIntegrationSettingEntity.getId());
    assertEquals(emailName, createdMailIntegrationSettingEntity.getEmailName());
    assertEquals(imapUrl, createdMailIntegrationSettingEntity.getImapUrl());
    assertEquals(port, createdMailIntegrationSettingEntity.getPort());
    assertEquals(encryption, createdMailIntegrationSettingEntity.getEncryption());
    assertEquals(account, createdMailIntegrationSettingEntity.getAccount());
    assertEquals(password, createdMailIntegrationSettingEntity.getPassword());
    assertEquals(userId, createdMailIntegrationSettingEntity.getUserId());
  }

  public void testGetMailIntegrationSettingsByUserId() {
    //Given
    MailIntegrationSettingEntity createdMailIntegrationSetting = createMailIntegrationSettingEntity();
    createdMailIntegrationSetting = mailIntegrationDAO.create(createdMailIntegrationSetting);

    // When
    List<MailIntegrationSettingEntity> retrievedMailIntegrationSettingEntities = mailIntegrationDAO.findMailIntegrationSettingsByUserId(createdMailIntegrationSetting.getUserId());
    
    // Then
    MailIntegrationSettingEntity retrievedMailIntegrationSetting = retrievedMailIntegrationSettingEntities.get(0);
    assertNotNull(retrievedMailIntegrationSetting);
    assertEquals(emailName, retrievedMailIntegrationSetting.getEmailName());
    assertEquals(imapUrl, retrievedMailIntegrationSetting.getImapUrl());
    assertEquals(port, retrievedMailIntegrationSetting.getPort());
    assertEquals(encryption, retrievedMailIntegrationSetting.getEncryption());
    assertEquals(account, retrievedMailIntegrationSetting.getAccount());
    assertEquals(userId, retrievedMailIntegrationSetting.getUserId());

  }

  public void testUpdateMailIntegrationSetting() {
    //Given
    MailIntegrationSettingEntity createdMailIntegrationSettingEntity = createMailIntegrationSettingEntity();
    createdMailIntegrationSettingEntity = mailIntegrationDAO.create(createdMailIntegrationSettingEntity);
    createdMailIntegrationSettingEntity.setAccount("updatedAccount");

    // When
    MailIntegrationSettingEntity updatedMailIntegrationSetting = mailIntegrationDAO.update(createdMailIntegrationSettingEntity);


    // Then
    assertNotNull(updatedMailIntegrationSetting);
    assertNotNull(updatedMailIntegrationSetting.getId());
    assertEquals(emailName, updatedMailIntegrationSetting.getEmailName());
    assertEquals(imapUrl, updatedMailIntegrationSetting.getImapUrl());
    assertEquals(port, updatedMailIntegrationSetting.getPort());
    assertEquals(encryption, updatedMailIntegrationSetting.getEncryption());
    assertEquals("updatedAccount", updatedMailIntegrationSetting.getAccount());
    assertEquals(userId, updatedMailIntegrationSetting.getUserId());
  }

  protected MailIntegrationSettingEntity createMailIntegrationSettingEntity() {
    MailIntegrationSettingEntity mailIntegrationSettingEntity = new MailIntegrationSettingEntity();
    mailIntegrationSettingEntity.setEmailName(emailName);
    mailIntegrationSettingEntity.setImapUrl(imapUrl);
    mailIntegrationSettingEntity.setPort(port);
    mailIntegrationSettingEntity.setEncryption(encryption);
    mailIntegrationSettingEntity.setAccount(account);
    mailIntegrationSettingEntity.setPassword(password);
    mailIntegrationSettingEntity.setUserId(userId);
    return mailIntegrationSettingEntity;
  }

  @Override
  protected void tearDown() throws Exception {
    end();
  }

  private void begin() {
    RequestLifeCycle.begin(container);
  }

  private void end() {
    RequestLifeCycle.end();
  }
}
