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
