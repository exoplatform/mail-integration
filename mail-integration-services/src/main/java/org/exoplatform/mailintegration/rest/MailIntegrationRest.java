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
package org.exoplatform.mailintegration.rest;

import java.util.List;
import java.util.stream.Collectors;

import javax.annotation.security.RolesAllowed;
import javax.mail.MessagingException;
import javax.mail.Store;
import javax.ws.rs.Consumes;
import javax.ws.rs.GET;
import javax.ws.rs.POST;
import javax.ws.rs.Path;
import javax.ws.rs.PathParam;
import javax.ws.rs.Produces;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;

import org.apache.commons.lang.StringUtils;

import org.exoplatform.common.http.HTTPStatus;
import org.exoplatform.mailintegration.model.MailIntegrationSetting;
import org.exoplatform.mailintegration.rest.model.MailIntegrationSettingRestEntity;
import org.exoplatform.mailintegration.rest.model.MessageRestEntity;
import org.exoplatform.mailintegration.service.MailIntegrationService;
import org.exoplatform.mailintegration.utils.MailIntegrationUtils;
import org.exoplatform.mailintegration.utils.RestEntityBuilder;
import org.exoplatform.services.log.ExoLogger;
import org.exoplatform.services.log.Log;
import org.exoplatform.services.rest.resource.ResourceContainer;
import org.exoplatform.social.core.manager.IdentityManager;

import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import io.swagger.annotations.ApiParam;
import io.swagger.annotations.ApiResponse;
import io.swagger.annotations.ApiResponses;

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
  @ApiOperation(value = "Create a mail integration setting", httpMethod = "POST", response = Response.class, consumes = "application/json")
  @ApiResponses(value = {@ApiResponse(code = HTTPStatus.NO_CONTENT, message = "Request fulfilled"),
          @ApiResponse(code = HTTPStatus.UNAUTHORIZED, message = "Unauthorized operation"),
          @ApiResponse(code = HTTPStatus.INTERNAL_ERROR, message = "Internal server error"),})
  public Response createMailIntegrationSetting(@ApiParam(value = "Mail integration setting object to create", required = true)
                                                          MailIntegrationSettingRestEntity mailIntegrationSettingEntity) {
    if (mailIntegrationSettingEntity == null) {
      return Response.status(Response.Status.BAD_REQUEST).build();
    }
    long userIdentityId = MailIntegrationUtils.getCurrentUserIdentityId(identityManager);
    try {
      MailIntegrationSetting mailIntegrationSetting = RestEntityBuilder.toMailIntegrationSetting(mailIntegrationSettingEntity, userIdentityId);
      MailIntegrationSetting createdMailIntegrationSetting = mailIntegrationService.createMailIntegrationSetting(mailIntegrationSetting);
      return Response.ok(createdMailIntegrationSetting).build();
    } catch (Exception e) {
      LOG.error("Error when creating mail integration setting ", e);
      return Response.status(Response.Status.INTERNAL_SERVER_ERROR).build();
    }
  }
  
  @GET
  @Path("mailIntegrationSettings")
  @RolesAllowed("users")
  @Produces(MediaType.APPLICATION_JSON)
  @ApiOperation(value = "Get mail integration settings", httpMethod = "GET", response = Response.class, notes = "This gets the mail integration settings of the authenticated user")
  @ApiResponses(value = { @ApiResponse(code = HTTPStatus.NO_CONTENT, message = "Request fulfilled"),
          @ApiResponse(code = HTTPStatus.BAD_REQUEST, message = "Invalid query input"),
          @ApiResponse(code = HTTPStatus.UNAUTHORIZED, message = "Unauthorized operation"),
          @ApiResponse(code = HTTPStatus.INTERNAL_ERROR, message = "Internal server error"), })
  public Response getMailIntegrationSettings() {
    long userIdentityId = MailIntegrationUtils.getCurrentUserIdentityId(identityManager);
    try {
      List<MailIntegrationSetting> mailIntegrationSettings = mailIntegrationService.getMailIntegrationSettingsByUserId(userIdentityId);
      List<MailIntegrationSettingRestEntity> mailIntegrationSettigRestEntities = mailIntegrationSettings.stream().map(RestEntityBuilder::fromMailIntegrationSetting).collect(Collectors.toList());
      return Response.ok(mailIntegrationSettigRestEntities).build();
    } catch (Exception e) {
      LOG.error("Error when getting mail integration settings ", e);
      return Response.status(Response.Status.INTERNAL_SERVER_ERROR).build();
    }
  }

  @Path("connect")
  @POST
  @Consumes(MediaType.APPLICATION_JSON)
  @Produces(MediaType.APPLICATION_JSON)
  @RolesAllowed("users")
  @ApiOperation(value = "Check mail integration connection", httpMethod = "POST", response = Response.class, consumes = "application/json")
  @ApiResponses(value = {
          @ApiResponse(code = HTTPStatus.OK, message = "Request fulfilled"),
          @ApiResponse(code = HTTPStatus.NO_CONTENT, message = "Request fulfilled"),
          @ApiResponse(code = HTTPStatus.INTERNAL_ERROR, message = "Internal server error"),})
  public Response connect(@ApiParam(value = "Connection information object to create", required = true)
                                              MailIntegrationSettingRestEntity mailIntegrationSettingRestEntity) {
    if (mailIntegrationSettingRestEntity == null) {
      return Response.status(Response.Status.BAD_REQUEST).build();
    }
    long userIdentityId = MailIntegrationUtils.getCurrentUserIdentityId(identityManager);

    try {
      MailIntegrationSetting mailIntegrationSetting = RestEntityBuilder.toMailIntegrationSetting(mailIntegrationSettingRestEntity, userIdentityId);
      Store store = mailIntegrationService.connect(mailIntegrationSetting);
      store.close();
      return Response.ok().build();
     } catch (MessagingException messagingException) {
      LOG.error("Error when checking mail connection ", messagingException);
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
    long userIdentityId = MailIntegrationUtils.getCurrentUserIdentityId(identityManager);
    try {
      if (StringUtils.isBlank(messageId)) {
        return Response.status(Response.Status.BAD_REQUEST).build();
      }
      MessageRestEntity message = mailIntegrationService.getMessageById(mailIntegrationSettingId, messageId, userIdentityId);
      if (message == null) {
        return Response.status(Response.Status.NOT_FOUND).build();
      }
      return Response.ok(message).build();
    } catch (IllegalAccessException e) {
      LOG.warn("User '{}' attempts to get a non authorized message", userIdentityId, e);
      return Response.status(Response.Status.UNAUTHORIZED).build();
    } catch (Exception e) {
      LOG.error("Error when getting message ", e);
      return Response.status(Response.Status.INTERNAL_SERVER_ERROR).build();
    }
  }
}
