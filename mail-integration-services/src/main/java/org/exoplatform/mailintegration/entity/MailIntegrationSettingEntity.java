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
package org.exoplatform.mailintegration.entity;

import org.exoplatform.commons.api.persistence.ExoEntity;

import javax.persistence.*;
import java.io.Serializable;

@Entity(name = "MailIntegrationSetting")
@ExoEntity
@Table(name = "MAIL_INTEGRATION_SETTING")
@NamedQuery(name = "ConnectionInformation.findMailIntegrationSettingsByUserId", query = "SELECT mailIntegrationSetting FROM MailIntegrationSetting mailIntegrationSetting where mailIntegrationSetting.creatorId = :creatorId")
public class MailIntegrationSettingEntity implements Serializable {
  private static final long serialVersionUID = 3783783343530708418L;

  @Id
  @SequenceGenerator(name = "SEQ_MAIL_INTEGRATION_SETTING_ID", sequenceName = "SEQ_MAIL_INTEGRATION_SETTING_ID", allocationSize = 1)
  @GeneratedValue(strategy = GenerationType.AUTO, generator = "SEQ_MAIL_INTEGRATION_SETTING_ID")
  @Column(name = "MAIL_INTEGRATION_SETTING_ID", nullable = false)
  private Long              id;

  @Column(name = "EMAIL_NAME", nullable = false)
  private String            emailName;

  @Column(name = "IMAP_URL", nullable = false)
  private String            imapUrl;

  @Column(name = "PORT", nullable = false)
  private long              port;

  @Column(name = "ENCRYPTION", nullable = false)
  private String            encryption;

  @Column(name = "ACCOUNT", nullable = false)
  private String            account;

  @Column(name = "PASSWORD", nullable = false)
  private String            password;

  @Column(name = "CREATOR_ID", nullable = false)
  private long              creatorId;

  public Long getId() {
    return id;
  }

  public void setId(Long id) {
    this.id = id;
  }

  public String getEmailName() {
    return emailName;
  }

  public void setEmailName(String emailName) {
    this.emailName = emailName;
  }

  public String getImapUrl() {
    return imapUrl;
  }

  public void setImapUrl(String imapUrl) {
    this.imapUrl = imapUrl;
  }

  public long getPort() {
    return port;
  }

  public void setPort(long port) {
    this.port = port;
  }

  public String getEncryption() {
    return encryption;
  }

  public void setEncryption(String encryption) {
    this.encryption = encryption;
  }

  public String getAccount() {
    return account;
  }

  public void setAccount(String account) {
    this.account = account;
  }

  public String getPassword() {
    return password;
  }

  public void setPassword(String password) {
    this.password = password;
  }

  public long getCreatorId() {
    return creatorId;
  }

  public void setCreatorId(long creatorId) {
    this.creatorId = creatorId;
  }
}
