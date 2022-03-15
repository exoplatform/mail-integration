package org.exoplatform.mailIntegration.service;

import org.exoplatform.mailIntegration.model.ConnectionInformation;
import org.exoplatform.mailIntegration.storage.MailIntegrationStorage;
import org.exoplatform.mailIntegration.utils.MailIntegrationUtils;
import org.exoplatform.social.core.identity.model.Identity;
import org.exoplatform.social.core.manager.IdentityManager;
import org.exoplatform.web.security.codec.CodecInitializer;

public class MailIntegrationServiceImpl implements MailIntegrationService {
  private MailIntegrationStorage mailIntegrationStorage;

  private IdentityManager        identityManager;
  
  private CodecInitializer       codecInitializer;

  public MailIntegrationServiceImpl(MailIntegrationStorage mailIntegrationStorage, IdentityManager identityManager, CodecInitializer codecInitializer) {
    this.mailIntegrationStorage = mailIntegrationStorage;
    this.identityManager = identityManager;
    this.codecInitializer = codecInitializer;
  }

  @Override
  public ConnectionInformation createMailIntegration(ConnectionInformation connectionInformation,
                                                     long userIdentityId) throws IllegalAccessException {
    if (userIdentityId <= 0) {
      throw new IllegalArgumentException("userIdentityId is mandatory");
    }

    Identity userIdentity = identityManager.getIdentity(String.valueOf(userIdentityId));
    if (userIdentity == null) {
      throw new IllegalAccessException("User '" + userIdentityId + "' doesn't exist");
    }
    if (connectionInformation == null) {
      throw new IllegalArgumentException("Connection information is mandatory");
    }
    String tokenPlain = connectionInformation.getPassword();
    String token = MailIntegrationUtils.generateEncryptedToken(codecInitializer, tokenPlain, userIdentityId);
    connectionInformation.setPassword(token);
    return mailIntegrationStorage.createMailIntegration(connectionInformation);
  }
}
