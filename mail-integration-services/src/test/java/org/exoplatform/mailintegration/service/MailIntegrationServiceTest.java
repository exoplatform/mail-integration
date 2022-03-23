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

import org.exoplatform.mailintegration.model.MailIntegrationSetting;
import org.junit.Test;

import static org.junit.Assert.*;

public class MailIntegrationServiceTest extends BaseMailIntegrationTest {

  @Test
  public void testCreateConnectionInformation() throws IllegalAccessException {
    MailIntegrationSetting mailIntegrationSetting = new MailIntegrationSetting();
    mailIntegrationSetting.setEmailName("emailName");
    mailIntegrationSetting.setImapUrl("imapUrl");
    mailIntegrationSetting.setPort(123);
    mailIntegrationSetting.setEncryption("encryption");
    mailIntegrationSetting.setAccount("account");
    mailIntegrationSetting.setPassword("password");
    mailIntegrationSetting.setCreatorId(Long.parseLong(user1Identity.getId()));

    // When
    MailIntegrationSetting createdMailIntegrationSetting = mailIntegrationService.createMailIntegrationSetting(mailIntegrationSetting,
                                                                                           Long.parseLong(user1Identity.getId()));

    assertNotNull(createdMailIntegrationSetting);
    assertEquals(mailIntegrationSetting.getEmailName(), createdMailIntegrationSetting.getEmailName());
    assertEquals(mailIntegrationSetting.getImapUrl(), createdMailIntegrationSetting.getImapUrl());
    assertEquals(mailIntegrationSetting.getPort(), createdMailIntegrationSetting.getPort());
    assertEquals(mailIntegrationSetting.getEncryption(), createdMailIntegrationSetting.getEncryption());
    assertEquals(mailIntegrationSetting.getAccount(), createdMailIntegrationSetting.getAccount());
    assertEquals(mailIntegrationSetting.getPassword(), createdMailIntegrationSetting.getPassword());
    assertEquals(mailIntegrationSetting.getCreatorId(), createdMailIntegrationSetting.getCreatorId());

  }
}
