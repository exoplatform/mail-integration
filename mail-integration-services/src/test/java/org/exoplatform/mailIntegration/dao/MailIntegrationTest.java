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
