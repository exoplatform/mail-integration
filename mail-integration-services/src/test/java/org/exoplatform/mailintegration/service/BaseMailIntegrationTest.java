package org.exoplatform.mailintegration.service;

import org.exoplatform.container.ExoContainerContext;
import org.exoplatform.container.PortalContainer;
import org.exoplatform.container.component.RequestLifeCycle;
import org.exoplatform.social.core.identity.model.Identity;
import org.exoplatform.social.core.identity.provider.OrganizationIdentityProvider;
import org.exoplatform.social.core.manager.IdentityManager;
import org.hibernate.ObjectNotFoundException;
import org.junit.After;
import org.junit.Before;

public abstract class BaseMailIntegrationTest {
  protected IdentityManager        identityManager;

  protected Identity               user1Identity;

  protected PortalContainer        container;

  protected MailIntegrationService mailIntegrationService;


  @Before
  public void setUp() throws ObjectNotFoundException {
    container = PortalContainer.getInstance();
    initServices();
    begin();
    injectData();
  }

  private void initServices() {
    mailIntegrationService = container.getComponentInstanceOfType(MailIntegrationService.class);
    identityManager = container.getComponentInstanceOfType(IdentityManager.class);
  }

  @After
  public void tearDown() throws ObjectNotFoundException {
    end();
  }

  protected void injectData() throws ObjectNotFoundException {
    user1Identity = identityManager.getOrCreateIdentity(OrganizationIdentityProvider.NAME, "testuser1");
  }

  protected void begin() {
    ExoContainerContext.setCurrentContainer(container);
    RequestLifeCycle.begin(container);
  }

  protected void end() {
    RequestLifeCycle.end();
  }

}
