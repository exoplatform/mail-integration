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
package org.exoplatform.mailIntegration.dao;

import junit.framework.TestCase;
import org.exoplatform.container.ExoContainerContext;
import org.exoplatform.container.PortalContainer;
import org.exoplatform.container.RootContainer;
import org.exoplatform.container.component.RequestLifeCycle;
import org.exoplatform.mailIntegration.entity.ConnectionInformationEntity;
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
    ConnectionInformationEntity connectionInformationEntity = createConnectionInformationEntity();

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

  protected ConnectionInformationEntity createConnectionInformationEntity() {
    ConnectionInformationEntity connectionInformationEntity = new ConnectionInformationEntity();
    connectionInformationEntity.setEmailName(emailName);
    connectionInformationEntity.setImapUrl(imapUrl);
    connectionInformationEntity.setPort(port);
    connectionInformationEntity.setEncryption(encryption);
    connectionInformationEntity.setAccount(account);
    connectionInformationEntity.setPassword(password);
    connectionInformationEntity.setCreatorId(creatorId);
    return mailIntegrationDAO.create(connectionInformationEntity);
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
