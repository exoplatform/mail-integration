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
package org.exoplatform.mailintegration.utils;

import org.apache.commons.lang.StringUtils;
import org.exoplatform.mailintegration.model.MailIntegrationSetting;
import org.exoplatform.mailintegration.rest.model.MailIntegrationSettingEntity;
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

  private MailIntegrationUtils() {
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

  public static final MailIntegrationSetting toConnectionInformation(MailIntegrationSettingEntity mailIntegrationSettingEntity,
                                                                     long userIdentityId) {
    MailIntegrationSetting connectionInformation = new MailIntegrationSetting();
    connectionInformation.setEmailName(mailIntegrationSettingEntity.getEmailName());
    connectionInformation.setImapUrl(mailIntegrationSettingEntity.getImapUrl());
    connectionInformation.setPort(mailIntegrationSettingEntity.getPort());
    connectionInformation.setEncryption(mailIntegrationSettingEntity.getEncryption());
    connectionInformation.setAccount(mailIntegrationSettingEntity.getAccount());
    connectionInformation.setPassword(mailIntegrationSettingEntity.getPassword());
    connectionInformation.setCreatorId(userIdentityId);
    return connectionInformation;
  }
}
