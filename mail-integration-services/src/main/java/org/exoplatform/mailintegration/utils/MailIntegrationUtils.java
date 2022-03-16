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
package org.exoplatform.mailintegration.utils;

import org.apache.commons.lang.StringUtils;
import org.exoplatform.mailintegration.model.MailIntegrationSetting;
import org.exoplatform.mailintegration.rest.model.ConnectionInformationEntity;
import org.exoplatform.services.log.ExoLogger;
import org.exoplatform.services.log.Log;
import org.exoplatform.services.security.ConversationState;
import org.exoplatform.social.core.identity.model.Identity;
import org.exoplatform.social.core.identity.provider.OrganizationIdentityProvider;
import org.exoplatform.social.core.manager.IdentityManager;
import org.exoplatform.web.security.codec.CodecInitializer;
import org.exoplatform.web.security.security.TokenServiceInitializationException;

public class MailIntegrationUtils {
  private static Log          LOG       = ExoLogger.getLogger(MailIntegrationUtils.class);

  public MailIntegrationUtils() {
  }

  public static String generateEncryptedToken(CodecInitializer codecInitializer, String tokenPlain, String remoteId) {
    if (StringUtils.isBlank(remoteId)) {
      throw new IllegalArgumentException("user remote id is mandatory");
    }
    try {
      return codecInitializer.getCodec().encode(tokenPlain);
    } catch (TokenServiceInitializationException e) {
      LOG.warn("Error generating Token", e);
      return null;
    }
  }

  public static String decryptUserIdentity(CodecInitializer codecInitializer, String token) {
    String tokenFlat;
    try {
      tokenFlat = codecInitializer.getCodec().decode(token);
    } catch (TokenServiceInitializationException e) {
      LOG.warn("Error decrypting Token", e);
      return null;
    }
    return tokenFlat;
  }

  public static final long getCurrentUserIdentityId(IdentityManager identityManager) {
    String currentUser = getCurrentUser();
    Identity identity = identityManager.getOrCreateIdentity(OrganizationIdentityProvider.NAME, currentUser);
    return identity == null ? 0 : Long.parseLong(identity.getId());
  }

  public static final String getCurrentUser() {
    return ConversationState.getCurrent().getIdentity().getUserId();
  }

  public static final MailIntegrationSetting toConnectionInformation(ConnectionInformationEntity connectionInformationEntity,
                                                                     long userIdentityId) {
    MailIntegrationSetting connectionInformation = new MailIntegrationSetting();
    connectionInformation.setEmailName(connectionInformationEntity.getEmailName());
    connectionInformation.setImapUrl(connectionInformationEntity.getImapUrl());
    connectionInformation.setPort(connectionInformationEntity.getPort());
    connectionInformation.setEncryption(connectionInformationEntity.getEncryption());
    connectionInformation.setAccount(connectionInformationEntity.getAccount());
    connectionInformation.setPassword(connectionInformationEntity.getPassword());
    connectionInformation.setCreatorId(userIdentityId);
    return connectionInformation;
  }
}
