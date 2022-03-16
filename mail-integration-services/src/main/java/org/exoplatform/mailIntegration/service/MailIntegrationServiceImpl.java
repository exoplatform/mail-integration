package org.exoplatform.mailIntegration.service;

import org.exoplatform.mailIntegration.model.ConnectionInformation;
import org.exoplatform.mailIntegration.storage.MailIntegrationStorage;
import org.exoplatform.mailIntegration.utils.MailIntegrationUtils;
import org.exoplatform.social.core.identity.model.Identity;
import org.exoplatform.social.core.manager.IdentityManager;
import org.exoplatform.web.security.codec.CodecInitializer;

import javax.mail.MessagingException;
import javax.mail.NoSuchProviderException;
import javax.mail.Session;
import javax.mail.Store;
import java.util.Properties;

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
    String token = MailIntegrationUtils.generateEncryptedToken(codecInitializer, tokenPlain, userIdentity.getRemoteId());
    connectionInformation.setPassword(token);
    return mailIntegrationStorage.createMailIntegration(connectionInformation);
  }

  @Override
  public Store imapConnect(ConnectionInformation connectionInformation) {
    Properties props = new Properties();
    props.setProperty("mail.store.protocol", "imaps");
    String provider = "imaps";
    // Connect to the server
    Session session = Session.getDefaultInstance(props, null);
    Store store = null;
    try {
      store = session.getStore(provider);
      store.connect(connectionInformation.getImapUrl(), connectionInformation.getAccount(), connectionInformation.getPassword());
    } catch (NoSuchProviderException nspe) {
      throw new IllegalArgumentException("invalid provider name");
    } catch (MessagingException me) {
      throw new IllegalStateException("messaging exception");
    }
    return store;
  }
}
