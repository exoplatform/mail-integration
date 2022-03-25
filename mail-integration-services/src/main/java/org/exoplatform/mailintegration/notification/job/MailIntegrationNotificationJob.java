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
package org.exoplatform.mailintegration.notification.job;

import org.exoplatform.mailintegration.service.MailIntegrationService;
import org.quartz.Job;
import org.quartz.JobExecutionContext;
import org.quartz.JobExecutionException;

import org.exoplatform.commons.api.settings.ExoFeatureService;
import org.exoplatform.commons.utils.CommonsUtils;
import org.exoplatform.services.log.ExoLogger;
import org.exoplatform.services.log.Log;

public class MailIntegrationNotificationJob implements Job {
  private static final Log LOG = ExoLogger.getLogger(MailIntegrationNotificationJob.class);

  @Override
  public void execute(JobExecutionContext context) throws JobExecutionException {
    ExoFeatureService featureService = CommonsUtils.getService(ExoFeatureService.class);
    if(featureService.isActiveFeature("mailIntegration")) {
      MailIntegrationService mailIntegrationService = CommonsUtils.getService(MailIntegrationService.class);
      LOG.info("Start mail integration notification job");
      mailIntegrationService.sendMailIntegrationNotifications();
      LOG.info("End mail integration notification job");
    }
  }
}