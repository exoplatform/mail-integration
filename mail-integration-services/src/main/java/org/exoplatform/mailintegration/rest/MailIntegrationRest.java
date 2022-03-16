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
package org.exoplatform.mailintegration.rest;

import io.swagger.annotations.*;
import org.apache.commons.lang.StringUtils;
import org.exoplatform.common.http.HTTPStatus;
import org.exoplatform.mailintegration.model.MailIntegrationSetting;
import org.exoplatform.mailintegration.rest.model.ConnectionInformationEntity;
import org.exoplatform.mailintegration.rest.model.MessageRestEntity;
import org.exoplatform.mailintegration.service.MailIntegrationService;
import org.exoplatform.mailintegration.utils.MailIntegrationUtils;
import org.exoplatform.services.log.ExoLogger;
import org.exoplatform.services.log.Log;
import org.exoplatform.services.rest.resource.ResourceContainer;
import org.exoplatform.services.security.ConversationState;
import org.exoplatform.services.security.Identity;
import org.exoplatform.social.core.manager.IdentityManager;

import javax.annotation.security.RolesAllowed;
import javax.mail.Store;
import javax.ws.rs.*;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;
import java.util.List;

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
      MailIntegrationSetting mailIntegrationSetting = MailIntegrationUtils.toConnectionInformation(connectionInformationEntity, userIdentityId);
      MailIntegrationSetting createdMailIntegrationSetting = mailIntegrationService.createMailIntegration(mailIntegrationSetting, userIdentityId);
      return Response.ok(createdMailIntegrationSetting).build();
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
  @Produces(MediaType.TEXT_PLAIN)
  @RolesAllowed("users")
  @ApiOperation(value = "Check mail connection", httpMethod = "POST", response = Response.class, consumes = "application/json")
  @ApiResponses(value = {
          @ApiResponse(code = HTTPStatus.OK, message = "Request fulfilled"),
          @ApiResponse(code = HTTPStatus.NO_CONTENT, message = "Request fulfilled"),
          @ApiResponse(code = HTTPStatus.INTERNAL_ERROR, message = "Internal server error"),})
  public Response checkMailConnection(@ApiParam(value = "Connection information object to create", required = true)
                                      ConnectionInformationEntity connectionInformationEntity) {
    if (connectionInformationEntity == null) {
      return Response.status(Response.Status.BAD_REQUEST).build();
    }
    long userIdentityId = MailIntegrationUtils.getCurrentUserIdentityId(identityManager);

    try {
      MailIntegrationSetting mailIntegrationSetting = MailIntegrationUtils.toConnectionInformation(connectionInformationEntity, userIdentityId);
      boolean connectionStatus = mailIntegrationService.isConnected(mailIntegrationSetting);
      String storedUserEmail = null;
      if (!connectionStatus) {
        Store connection = mailIntegrationService.imapConnect(mailIntegrationSetting);
        storedUserEmail = connection.getURLName().getUsername();
      } else {
        storedUserEmail = "already connected";
      }
      return Response.ok(storedUserEmail).type(MediaType.TEXT_PLAIN).build();
    } catch (IllegalArgumentException e) {
      LOG.warn("User '{}' attempts to create a non authorized mail integration", e);
      return Response.status(Response.Status.NO_CONTENT).build();
    } catch (Exception e) {
      return Response.status(Response.Status.INTERNAL_SERVER_ERROR).build();
    }
  }

  @GET
  @Path("mailIntegrationSettings")
  @RolesAllowed("users")
  @Produces(MediaType.APPLICATION_JSON)
  @ApiOperation(value = "Get a poll", httpMethod = "GET", response = Response.class, notes = "This gets the poll with the given id if the authenticated user is a member of the space.")
  @ApiResponses(value = { @ApiResponse(code = HTTPStatus.NO_CONTENT, message = "Request fulfilled"),
          @ApiResponse(code = HTTPStatus.BAD_REQUEST, message = "Invalid query input"),
          @ApiResponse(code = HTTPStatus.UNAUTHORIZED, message = "Unauthorized operation"),
          @ApiResponse(code = HTTPStatus.INTERNAL_ERROR, message = "Internal server error"), })
  public Response getMailIntegrationSettingsByUserId() {
    try {
      long userIdentityId = MailIntegrationUtils.getCurrentUserIdentityId(identityManager);
      List<MailIntegrationSetting> connectionInformationList = mailIntegrationService.getMailIntegrationSettingsByUserId(userIdentityId);
      if (connectionInformationList == null) {
        return Response.status(Response.Status.NOT_FOUND).build();
      }
      return Response.ok(connectionInformationList).build();
    } catch (IllegalAccessException e) {
      LOG.warn("User '{}' attempts to get a non authorized poll", e);
      return Response.status(Response.Status.UNAUTHORIZED).build();
    } catch (Exception e) {
      return Response.status(Response.Status.INTERNAL_SERVER_ERROR).build();
    }
  }


  @GET
  @Path("/{mailntegrationSettingId}/message/{id}")
  @RolesAllowed("users")
  @Produces(MediaType.APPLICATION_JSON)
  @ApiOperation(value = "Get a message", httpMethod = "GET", response = Response.class, notes = "This gets the message with the given id")
  @ApiResponses(value = { @ApiResponse(code = HTTPStatus.NO_CONTENT, message = "Request fulfilled"),
          @ApiResponse(code = HTTPStatus.BAD_REQUEST, message = "Invalid query input"),
          @ApiResponse(code = HTTPStatus.UNAUTHORIZED, message = "Unauthorized operation"),
          @ApiResponse(code = HTTPStatus.INTERNAL_ERROR, message = "Internal server error"), })
  public Response getMessageById(@ApiParam(value = "Message id", required = true) @PathParam("id") String messageId,
                                 @ApiParam(value = "Message integration setting id", required = true) @PathParam("mailntegrationSettingId") long mailIntegrationSettingId) {
    Identity currentIdentity = ConversationState.getCurrent().getIdentity();
    try {
      if (StringUtils.isBlank(messageId)) {
        return Response.status(Response.Status.BAD_REQUEST).build();
      }
      MessageRestEntity message = mailIntegrationService.getMessageById(mailIntegrationSettingId, messageId, currentIdentity);
      if (message == null) {
        return Response.status(Response.Status.NOT_FOUND).build();
      }
      return Response.ok(message).build();
    } catch (IllegalAccessException e) {
      LOG.warn("User '{}' attempts to get a non authorized message", currentIdentity.getUserId(), e);
      return Response.status(Response.Status.UNAUTHORIZED).build();
    } catch (Exception e) {
      return Response.status(Response.Status.INTERNAL_SERVER_ERROR).build();
    }
  }
}
