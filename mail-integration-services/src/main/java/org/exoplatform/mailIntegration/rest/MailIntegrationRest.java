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
package org.exoplatform.mailIntegration.rest;

import io.swagger.annotations.*;
import org.exoplatform.common.http.HTTPStatus;
import org.exoplatform.mailIntegration.model.ConnectionInformation;
import org.exoplatform.mailIntegration.rest.model.ConnectionInformationEntity;
import org.exoplatform.mailIntegration.service.MailIntegrationService;
import org.exoplatform.mailIntegration.utils.MailIntegrationUtils;
import org.exoplatform.services.log.ExoLogger;
import org.exoplatform.services.log.Log;
import org.exoplatform.services.rest.resource.ResourceContainer;
import org.exoplatform.social.core.manager.IdentityManager;

import javax.annotation.security.RolesAllowed;
import javax.mail.Store;
import javax.ws.rs.*;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;

@Path("/v1/mailIntegration")
@Api(value = "/v1/mailIntegration", description = "Managing mail Integration") // NOSONAR
public class MailIntegrationRest implements ResourceContainer {
  private static final Log LOG = ExoLogger.getLogger(MailIntegrationRest.class);

  MailIntegrationService mailIntegrationService;

  private IdentityManager identityManager;

  public MailIntegrationRest(MailIntegrationService mailIntegrationService, IdentityManager identityManager) {
    this.mailIntegrationService = mailIntegrationService;
    this.identityManager = identityManager;
  }

  @POST
  @Consumes(MediaType.APPLICATION_JSON)
  @Produces(MediaType.APPLICATION_JSON)
  @RolesAllowed("users")
  @ApiOperation(value = "Create a new mail integration", httpMethod = "POST", response = Response.class, consumes = "application/json")
  @ApiResponses(value = {@ApiResponse(code = HTTPStatus.NO_CONTENT, message = "Request fulfilled"),
          @ApiResponse(code = HTTPStatus.UNAUTHORIZED, message = "Unauthorized operation"),
          @ApiResponse(code = HTTPStatus.INTERNAL_ERROR, message = "Internal server error"),})
  public Response createMailIntegrationConnection(@ApiParam(value = "Connection information object to create", required = true)
                                                          ConnectionInformationEntity connectionInformationEntity) {
    if (connectionInformationEntity == null) {
      return Response.status(Response.Status.BAD_REQUEST).build();
    }
    long userIdentityId = MailIntegrationUtils.getCurrentUserIdentityId(identityManager);
    try {
      ConnectionInformation connectionInformation = MailIntegrationUtils.toConnectionInformation(connectionInformationEntity, userIdentityId);
      ConnectionInformation createdConnection = mailIntegrationService.createMailIntegration(connectionInformation, userIdentityId);
      return Response.ok(createdConnection).build();
    } catch (IllegalAccessException e) {
      LOG.warn("User '{}' attempts to create a non authorized mail integration", e);
      return Response.status(Response.Status.UNAUTHORIZED).build();
    } catch (Exception e) {
      return Response.status(Response.Status.INTERNAL_SERVER_ERROR).build();
    }
  }

  @Path("checkMailConnection")
  @POST
  @Consumes(MediaType.APPLICATION_JSON)
  @Produces(MediaType.APPLICATION_JSON)
  @RolesAllowed("users")
  @ApiOperation(value = "Check mail connection", httpMethod = "POST", response = Response.class, consumes = "application/json")
  @ApiResponses(value = {@ApiResponse(code = HTTPStatus.NO_CONTENT, message = "Request fulfilled"),
          @ApiResponse(code = HTTPStatus.INTERNAL_ERROR, message = "Internal server error"),})
  public Response checkMailConnection(@ApiParam(value = "Connection information object to create", required = true)
                                      ConnectionInformationEntity connectionInformationEntity) {
    if (connectionInformationEntity == null) {
      return Response.status(Response.Status.BAD_REQUEST).build();
    }
    long userIdentityId = MailIntegrationUtils.getCurrentUserIdentityId(identityManager);

    try {
      ConnectionInformation connectionInformation = MailIntegrationUtils.toConnectionInformation(connectionInformationEntity, userIdentityId);
      Store connection = mailIntegrationService.imapConnect(connectionInformation);
      return Response.ok(connection).build();
    } catch (IllegalArgumentException e) {
      LOG.warn("User '{}' attempts to create a non authorized mail integration", e);
      return Response.status(Response.Status.NO_CONTENT).build();
    } catch (Exception e) {
      return Response.status(Response.Status.INTERNAL_SERVER_ERROR).build();
    }
  }
}
