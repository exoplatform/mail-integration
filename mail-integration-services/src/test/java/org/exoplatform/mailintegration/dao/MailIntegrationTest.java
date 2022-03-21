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
import org.exoplatform.services.naming.InitialContextInitializer;

public class MailIntegrationTest extends TestCase {
  private PortalContainer    container;

  private String             emailName  = "user1@exo.tn";

  private String             imapUrl    = "imap.gmail.com";

  private long               port       = 123;

  private String             encryption = "SSL";

  private String             account    = "user1@exo.tn";

  private String             password   = "123456";

  private long               creatorId  = 1L;

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

  public void testCreatePoll() {
    // When
    MailIntegrationSettingEntity connectionInformationEntity = createMailIntegrationSettingEntityy();

    // Then
    assertNotNull(connectionInformationEntity);
    assertNotNull(connectionInformationEntity.getId());
    assertEquals(emailName, connectionInformationEntity.getEmailName());
    assertEquals(imapUrl, connectionInformationEntity.getImapUrl());
    assertEquals(port, connectionInformationEntity.getPort());
    assertEquals(encryption, connectionInformationEntity.getEncryption());
    assertEquals(account, connectionInformationEntity.getAccount());
    assertEquals(password, connectionInformationEntity.getPassword());
    assertEquals(creatorId, connectionInformationEntity.getCreatorId());
  }

  protected MailIntegrationSettingEntity createMailIntegrationSettingEntityy() {
    MailIntegrationSettingEntity mailIntegrationSettingEntity = new MailIntegrationSettingEntity();
    mailIntegrationSettingEntity.setEmailName(emailName);
    mailIntegrationSettingEntity.setImapUrl(imapUrl);
    mailIntegrationSettingEntity.setPort(port);
    mailIntegrationSettingEntity.setEncryption(encryption);
    mailIntegrationSettingEntity.setAccount(account);
    mailIntegrationSettingEntity.setPassword(password);
    mailIntegrationSettingEntity.setCreatorId(creatorId);
    return mailIntegrationDAO.create(mailIntegrationSettingEntity);
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
