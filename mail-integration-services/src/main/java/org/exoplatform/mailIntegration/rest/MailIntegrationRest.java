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
package org.exoplatform.mailIntegration.rest;

import javax.annotation.security.RolesAllowed;
import javax.ws.rs.GET;
import javax.ws.rs.Path;
import javax.ws.rs.PathParam;
import javax.ws.rs.Produces;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;

import org.apache.commons.lang3.StringUtils;

import org.exoplatform.common.http.HTTPStatus;
import org.exoplatform.mailIntegration.rest.model.MessageRestEntity;
import org.exoplatform.mailIntegration.service.MailIntegrationService;
import org.exoplatform.services.log.ExoLogger;
import org.exoplatform.services.log.Log;
import org.exoplatform.services.rest.resource.ResourceContainer;
import org.exoplatform.services.security.ConversationState;
import org.exoplatform.services.security.Identity;

import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import io.swagger.annotations.ApiParam;
import io.swagger.annotations.ApiResponse;
import io.swagger.annotations.ApiResponses;

@Path("/v1/mailIntegration")
@Api(value = "/v1/mailIntegration", description = "Managing mail integration") // NOSONAR
public class MailIntegrationRest implements ResourceContainer {
  private static final Log LOG = ExoLogger.getLogger(MailIntegrationRest.class);

  private MailIntegrationService      mailIntegrationService;

  public MailIntegrationRest(MailIntegrationService mailIntegrationService) {
    this.mailIntegrationService = mailIntegrationService;
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
                                 @ApiParam(value = "Message integration setting id", required = true) @PathParam("mailntegrationSettingId") String mailntegrationSettingId) {
    Identity currentIdentity = ConversationState.getCurrent().getIdentity();
    try {
      if (StringUtils.isBlank(messageId)) {
        return Response.status(Response.Status.BAD_REQUEST).build();
      }
      MessageRestEntity message = mailIntegrationService.getMessageById(mailntegrationSettingId, messageId, currentIdentity);
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
