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

import junit.framework.TestCase;
import org.exoplatform.container.ExoContainerContext;
import org.exoplatform.container.PortalContainer;
import org.exoplatform.container.RootContainer;
import org.exoplatform.container.component.RequestLifeCycle;
import org.exoplatform.mailintegration.entity.MailIntegrationSettingEntity;
import org.exoplatform.mailintegration.model.MailIntegrationSetting;
import org.exoplatform.services.naming.InitialContextInitializer;
import org.junit.Test;

import java.util.List;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;

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
    MailIntegrationSettingEntity mailIntegrationSettingEntity = createMailIntegrationSettingEntity();
    
    // When
    mailIntegrationSettingEntity = mailIntegrationDAO.create(mailIntegrationSettingEntity);
    
    // Then
    assertNotNull(mailIntegrationSettingEntity);
    assertNotNull(mailIntegrationSettingEntity.getId());
    assertEquals(emailName, mailIntegrationSettingEntity.getEmailName());
    assertEquals(imapUrl, mailIntegrationSettingEntity.getImapUrl());
    assertEquals(port, mailIntegrationSettingEntity.getPort());
    assertEquals(encryption, mailIntegrationSettingEntity.getEncryption());
    assertEquals(account, mailIntegrationSettingEntity.getAccount());
    assertEquals(password, mailIntegrationSettingEntity.getPassword());
    assertEquals(userId, mailIntegrationSettingEntity.getUserId());
  }

  public void testGetMailIntegrationSettingsByUserId() {
    //Given
    MailIntegrationSettingEntity createdMailIntegrationSetting = createMailIntegrationSettingEntity();
    createdMailIntegrationSetting = mailIntegrationDAO.create(createdMailIntegrationSetting);

    // When
    List<MailIntegrationSettingEntity> mailIntegrationSettingEntities = mailIntegrationDAO.findMailIntegrationSettingsByUserId(createdMailIntegrationSetting.getUserId());
    MailIntegrationSettingEntity mailIntegrationSetting1 = mailIntegrationSettingEntities.get(0);
    assertNotNull(mailIntegrationSetting1);
    assertEquals(emailName, createdMailIntegrationSetting.getEmailName());
    assertEquals(imapUrl, createdMailIntegrationSetting.getImapUrl());
    assertEquals(port, createdMailIntegrationSetting.getPort());
    assertEquals(encryption, createdMailIntegrationSetting.getEncryption());
    assertEquals(account, createdMailIntegrationSetting.getAccount());
    assertEquals(password, createdMailIntegrationSetting.getPassword());
    assertEquals(userId, createdMailIntegrationSetting.getUserId());

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
