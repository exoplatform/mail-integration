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

import java.util.List;

import static org.junit.Assert.*;

public class MailIntegrationServiceTest extends BaseMailIntegrationTest {

  private String emailName  = "user1@exo.tn";

  private String imapUrl    = "imap.gmail.com";

  private long   port       = 123;

  private String encryption = "SSL";

  private String account    = "user1@exo.tn";

  private String password   = "123456";
  
  @Test
  public void testCreateMailIntegrationSetting() throws IllegalAccessException {
    //Given
    MailIntegrationSetting mailIntegrationSetting = new MailIntegrationSetting();
    mailIntegrationSetting.setEmailName(emailName);
    mailIntegrationSetting.setImapUrl(imapUrl);
    mailIntegrationSetting.setPort(port);
    mailIntegrationSetting.setEncryption(encryption);
    mailIntegrationSetting.setAccount(account);
    mailIntegrationSetting.setPassword(password);
    mailIntegrationSetting.setUserId(Long.parseLong(user1Identity.getId()));

    // When
    MailIntegrationSetting createdMailIntegrationSetting = mailIntegrationService.createMailIntegrationSetting(mailIntegrationSetting);

    // Then
    assertNotNull(createdMailIntegrationSetting);
    assertEquals(mailIntegrationSetting.getEmailName(), createdMailIntegrationSetting.getEmailName());
    assertEquals(mailIntegrationSetting.getImapUrl(), createdMailIntegrationSetting.getImapUrl());
    assertEquals(mailIntegrationSetting.getPort(), createdMailIntegrationSetting.getPort());
    assertEquals(mailIntegrationSetting.getEncryption(), createdMailIntegrationSetting.getEncryption());
    assertEquals(mailIntegrationSetting.getAccount(), createdMailIntegrationSetting.getAccount());
    assertEquals(mailIntegrationSetting.getPassword(), createdMailIntegrationSetting.getPassword());
    assertEquals(mailIntegrationSetting.getUserId(), createdMailIntegrationSetting.getUserId());

  }

  @Test
  public void testGetMailIntegrationSettingsByUserId() {
    //Given
    MailIntegrationSetting mailIntegrationSetting = new MailIntegrationSetting();
    mailIntegrationSetting.setEmailName(emailName);
    mailIntegrationSetting.setImapUrl(imapUrl);
    mailIntegrationSetting.setPort(port);
    mailIntegrationSetting.setEncryption(encryption);
    mailIntegrationSetting.setAccount(account);
    mailIntegrationSetting.setPassword(password);
    mailIntegrationSetting.setUserId(Long.parseLong(user1Identity.getId()));
    MailIntegrationSetting createdMailIntegrationSetting = mailIntegrationService.createMailIntegrationSetting(mailIntegrationSetting);

    // When
    List<MailIntegrationSetting> retrievedMailIntegrationSettings = mailIntegrationService.getMailIntegrationSettingsByUserId(Long.parseLong(user1Identity.getId()));

    // Then
    MailIntegrationSetting retrievedMailIntegrationSetting = retrievedMailIntegrationSettings.get(0);
    assertNotNull(retrievedMailIntegrationSetting);
    assertEquals(createdMailIntegrationSetting.getEmailName(), retrievedMailIntegrationSetting.getEmailName());
    assertEquals(createdMailIntegrationSetting.getImapUrl(), retrievedMailIntegrationSetting.getImapUrl());
    assertEquals(createdMailIntegrationSetting.getPort(), retrievedMailIntegrationSetting.getPort());
    assertEquals(createdMailIntegrationSetting.getEncryption(), retrievedMailIntegrationSetting.getEncryption());
    assertEquals(createdMailIntegrationSetting.getAccount(), retrievedMailIntegrationSetting.getAccount());
    assertEquals(createdMailIntegrationSetting.getUserId(), retrievedMailIntegrationSetting.getUserId());
  }
  
  @Test
  public void testGetMailIntegrationSetting() {
    //Given
    MailIntegrationSetting mailIntegrationSetting = new MailIntegrationSetting();
    mailIntegrationSetting.setEmailName(emailName);
    mailIntegrationSetting.setImapUrl(imapUrl);
    mailIntegrationSetting.setPort(port);
    mailIntegrationSetting.setEncryption(encryption);
    mailIntegrationSetting.setAccount(account);
    mailIntegrationSetting.setPassword(password);
    mailIntegrationSetting.setUserId(Long.parseLong(user1Identity.getId()));
    MailIntegrationSetting createdMailIntegrationSetting = mailIntegrationService.createMailIntegrationSetting(mailIntegrationSetting);

    // When
    MailIntegrationSetting retrievedMailIntegrationSetting = mailIntegrationService.getMailIntegrationSetting(createdMailIntegrationSetting.getId());

    // Then
    assertNotNull(retrievedMailIntegrationSetting);
    assertEquals(createdMailIntegrationSetting.getEmailName(), retrievedMailIntegrationSetting.getEmailName());
    assertEquals(createdMailIntegrationSetting.getImapUrl(), retrievedMailIntegrationSetting.getImapUrl());
    assertEquals(createdMailIntegrationSetting.getPort(), retrievedMailIntegrationSetting.getPort());
    assertEquals(createdMailIntegrationSetting.getEncryption(), retrievedMailIntegrationSetting.getEncryption());
    assertEquals(createdMailIntegrationSetting.getAccount(), retrievedMailIntegrationSetting.getAccount());
    assertEquals(createdMailIntegrationSetting.getUserId(), retrievedMailIntegrationSetting.getUserId());
  }
  
  @Test
  public void testDeleteMailIntegrationSetting() throws IllegalAccessException {
    //Given
    MailIntegrationSetting mailIntegrationSetting = new MailIntegrationSetting();
    mailIntegrationSetting.setEmailName(emailName);
    mailIntegrationSetting.setImapUrl(imapUrl);
    mailIntegrationSetting.setPort(port);
    mailIntegrationSetting.setEncryption(encryption);
    mailIntegrationSetting.setAccount(account);
    mailIntegrationSetting.setPassword(password);
    mailIntegrationSetting.setUserId(Long.parseLong(user1Identity.getId()));
    MailIntegrationSetting createdMailIntegrationSetting = mailIntegrationService.createMailIntegrationSetting(mailIntegrationSetting);

    // When
    mailIntegrationService.deleteMailIntegrationSetting(createdMailIntegrationSetting.getId(), Long.parseLong(user1Identity.getId()));
    
    // Then
    MailIntegrationSetting deletedMailIntegrationSetting = mailIntegrationService.getMailIntegrationSetting(createdMailIntegrationSetting.getId());
    assertNull(deletedMailIntegrationSetting);
  }
}
