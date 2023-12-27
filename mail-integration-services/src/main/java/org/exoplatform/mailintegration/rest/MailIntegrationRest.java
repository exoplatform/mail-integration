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
import javax.ws.rs.*;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;

import org.apache.commons.lang3.StringUtils;

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

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.parameters.RequestBody;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.tags.Tag;

@Path("/v1/mailIntegration")
@Tag(name = "/v1/mailIntegration", description = "Managing mail Integration") // NOSONAR
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
  @Operation(summary = "Create a mail integration setting", method = "POST", description = "This creates a mail integration setting")
  @ApiResponses(value = {@ApiResponse(responseCode = "400", description = "Invalid query input"),
          @ApiResponse(responseCode = "500", description = "Internal server error")})
  public Response createMailIntegrationSetting(@Parameter(description = "Mail integration setting object to create", required = true)
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
  @Operation(summary = "Get mail integration settings", method = "GET", description = "This gets the mail integration settings of the authenticated user")
  @ApiResponses(value = {@ApiResponse(responseCode = "500", description = "Internal server error")})
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
  
  @DELETE
  @Path("{id}")
  @RolesAllowed("users")
  @Produces(MediaType.APPLICATION_JSON)
  @Operation(summary = "Delete mail integration setting", method = "DELETE", description = "This deletes the mail integration setting of the authenticated user by its id")
  @ApiResponses(value = {@ApiResponse(responseCode = "404", description = "Mail Integration Setting not found"),
          @ApiResponse(responseCode = "401", description = "Unauthorized operation"),
          @ApiResponse(responseCode = "500", description = "Internal server error")})
  public Response deleteMailIntegrationSetting( @Parameter(description = "Mail Integration Setting id", required = true) @PathParam("id") String mailIntegrationSettingId) {
    String currentUser = MailIntegrationUtils.getCurrentUser();
    try { 
      MailIntegrationSetting mailIntegrationSetting = mailIntegrationService.getMailIntegrationSetting(Long.parseLong(mailIntegrationSettingId));
      if (mailIntegrationSetting == null) {
        return Response.status(Response.Status.NOT_FOUND).build();
      }
      long currentUserIdentityId = MailIntegrationUtils.getCurrentUserIdentityId(identityManager);
      mailIntegrationService.deleteMailIntegrationSetting(Long.parseLong(mailIntegrationSettingId), currentUserIdentityId);
      return Response.ok().build();
    } catch (IllegalAccessException e) {
      LOG.warn("User '{}' is not autorized to delete mail integration setting", currentUser, e);
      return Response.status(Response.Status.UNAUTHORIZED).entity(e.getMessage()).build();
    } catch (Exception e) {
      LOG.error("Error when deleting the mail integration setting with id " + mailIntegrationSettingId, e);
      return Response.status(Response.Status.INTERNAL_SERVER_ERROR).build();
    }
  }

  @Path("connect")
  @POST
  @Consumes(MediaType.APPLICATION_JSON)
  @Produces(MediaType.APPLICATION_JSON)
  @RolesAllowed("users")
  @Operation(summary = "Check mail integration connection", method = "POST")
  @ApiResponses(value = {@ApiResponse(responseCode = "400", description = "Invalid query input"),
          @ApiResponse(responseCode = "500", description = "Internal server error")})
  public Response connect(@Parameter(description = "Connection information object to create", required = true)
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
  @Operation(summary = "Get a message", method = "GET", description = "This gets the message with the given id")
  @ApiResponses(value = { @ApiResponse(responseCode = "400", description = "Invalid query input"),
          @ApiResponse(responseCode = "404", description = "Message not found"),
          @ApiResponse(responseCode = "401", description = "Unauthorized operation"),
          @ApiResponse(responseCode = "500", description = "Internal server error")})
  public Response getMessageById(@Parameter(description = "Message id", required = true) @PathParam("id") String messageId,
                                 @Parameter(description = "Message integration setting id", required = true) @PathParam("mailntegrationSettingId") long mailIntegrationSettingId) {
    String currentUser = MailIntegrationUtils.getCurrentUser();
    try {
      if (StringUtils.isBlank(messageId)) {
        return Response.status(Response.Status.BAD_REQUEST).build();
      }
      long currentUserIdentityId = MailIntegrationUtils.getCurrentUserIdentityId(identityManager);
      MessageRestEntity message = mailIntegrationService.getMessageById(mailIntegrationSettingId, messageId, currentUserIdentityId);
      if (message == null) {
        return Response.status(Response.Status.NOT_FOUND).build();
      }
      return Response.ok(message).build();
    } catch (IllegalAccessException e) {
      LOG.warn("User '{}' attempts to get a non authorized message", currentUser, e);
      return Response.status(Response.Status.UNAUTHORIZED).build();
    } catch (Exception e) {
      LOG.error("Error when getting message ", e);
      return Response.status(Response.Status.INTERNAL_SERVER_ERROR).build();
    }
  }

  @PUT
  @Consumes(MediaType.APPLICATION_JSON)
  @Produces(MediaType.APPLICATION_JSON)
  @RolesAllowed("users")
  @Operation(summary = "Update an existing mail integration setting", method = "PUT")
  @ApiResponses(
          value = {@ApiResponse(responseCode = "400", description = "Invalid query input"),
                  @ApiResponse(responseCode = "401", description = "Unauthorized operation"),
                  @ApiResponse(responseCode = "500", description = "Internal server error"),
          }
  )
  public Response updateMailIntegrationSetting(
                                               @Parameter(description = "Mail integration setting object to update", required = true)
                                               MailIntegrationSettingRestEntity mailIntegrationSettingEntity) {
    String currentUser = MailIntegrationUtils.getCurrentUser();
    if (mailIntegrationSettingEntity == null) {
      return Response.status(Response.Status.BAD_REQUEST).build();
    }
    long userIdentityId = MailIntegrationUtils.getCurrentUserIdentityId(identityManager);
    try {
      MailIntegrationSetting mailIntegrationSetting = RestEntityBuilder.toMailIntegrationSetting(mailIntegrationSettingEntity,
                                                                                                 userIdentityId);
      MailIntegrationSetting updatedMailIntegrationSetting =
                                                           mailIntegrationService.updateMailIntegrationSetting(mailIntegrationSetting,
                                                                                                               userIdentityId);
      return Response.ok(updatedMailIntegrationSetting).build();
    } catch (IllegalAccessException e) {
      LOG.warn("User '{}' is not authorized to update mail integration setting", currentUser, e);
      return Response.status(Response.Status.UNAUTHORIZED).entity(e.getMessage()).build();
    } catch (Exception e) {
      LOG.error("Error when updating mail integration setting ", e);
      return Response.status(Response.Status.INTERNAL_SERVER_ERROR).build();
    }
  }
}
