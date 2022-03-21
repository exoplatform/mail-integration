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
package org.exoplatform.mailintegration.dao;

import org.exoplatform.commons.persistence.impl.GenericDAOJPAImpl;
import org.exoplatform.mailintegration.entity.MailIntegrationSettingEntity;

import javax.persistence.TypedQuery;
import java.util.Collections;
import java.util.List;

public class MailIntegrationDAO extends GenericDAOJPAImpl<MailIntegrationSettingEntity, Long> {

  public List<MailIntegrationSettingEntity> findMailIntegrationSettingsByUserId(long userIdentityId) {
    TypedQuery<MailIntegrationSettingEntity> query =
                                                   getEntityManager().createNamedQuery("MailIntegrationSetting.findMailIntegrationSettingsByUserId",
                                                                                       MailIntegrationSettingEntity.class);
    query.setParameter("creatorId", userIdentityId);
    List<MailIntegrationSettingEntity> resultList = query.getResultList();
    return resultList == null ? Collections.emptyList() : resultList;
  }

  public List<MailIntegrationSettingEntity> findMailIntegrationSettingsByIdAndUserId(long mailSettingId, long userIdentityId) {
    TypedQuery<MailIntegrationSettingEntity> query =
            getEntityManager().createNamedQuery("MailIntegrationSetting.findMailIntegrationSettingsByIdAndUserId",
                    MailIntegrationSettingEntity.class);
    query.setParameter("mailSettingId", mailSettingId);
    query.setParameter("creatorId", userIdentityId);
    List<MailIntegrationSettingEntity> resultList = query.getResultList();
    return resultList == null ? Collections.emptyList() : resultList;
  }
}
