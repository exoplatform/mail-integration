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
package org.exoplatform.mailIntegration.utils;

import org.exoplatform.mailIntegration.entity.ConnectionInformationEntity;
import org.exoplatform.mailIntegration.model.ConnectionInformation;

public class EntityMapper {
  public EntityMapper() {
  }
  
  public static ConnectionInformation fromConnectionInformationEntity(ConnectionInformationEntity connectionInformationEntity) {
      if(connectionInformationEntity == null) {
          return null;
      }
      return new ConnectionInformation(connectionInformationEntity.getId(),
                                       connectionInformationEntity.getEmailName(),
                                       connectionInformationEntity.getImapUrl(),
                                       connectionInformationEntity.getPort(),
                                       connectionInformationEntity.getEncryption(),
                                       connectionInformationEntity.getAccount(),
                                       connectionInformationEntity.getPassword(),
                                       connectionInformationEntity.getCreatorId());
      
  }

  public static ConnectionInformationEntity toConnectionInformationEntity(ConnectionInformation connectionInformation) {
      if(connectionInformation == null) {
          return null;
      }
      ConnectionInformationEntity connectionInformationEntity = new ConnectionInformationEntity();
      connectionInformationEntity.setId(connectionInformation.getId() == 0 ? null : connectionInformation.getId());
      connectionInformationEntity.setEmailName(connectionInformation.getEmailName());
      connectionInformationEntity.setImapUrl(connectionInformation.getImapUrl());
      connectionInformationEntity.setPort(connectionInformation.getPort());
      connectionInformationEntity.setEncryption(connectionInformation.getEncryption());
      connectionInformationEntity.setAccount(connectionInformation.getAccount());
      connectionInformationEntity.setPassword(connectionInformation.getPassword());
      connectionInformationEntity.setCreatorId(connectionInformation.getCreatorId());
      return connectionInformationEntity;
  }
    
}
