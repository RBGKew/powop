/*
 * This is eMonocot, a global online biodiversity information resource.
 *
 * Copyright © 2011–2015 The Board of Trustees of the Royal Botanic Gardens, Kew and The University of Oxford
 *
 * eMonocot is free software: you can redistribute it and/or modify it under the terms of the
 * GNU Affero General Public License as published by the Free Software Foundation, either version 3
 * of the License, or (at your option) any later version.
 *
 * eMonocot is distributed in the hope that it will be useful, but WITHOUT ANY WARRANTY; without even
 * the implied warranty of MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 * GNU Affero General Public License for more details.
 *
 * The complete text of the GNU Affero General Public License is in the source repository as the file
 * ‘COPYING’.  It is also available from <http://www.gnu.org/licenses/>.
 */
package org.emonocot.harvest.common;

import org.emonocot.api.job.JobExecutionException;
import org.emonocot.api.job.JobExecutionInfo;
import org.emonocot.api.job.JobStatusNotifier;
import org.emonocot.model.JobConfiguration;
import org.emonocot.api.JobConfigurationService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.batch.core.BatchStatus;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class JobStatusNotifierImpl implements JobStatusNotifier {

	private static Logger logger = LoggerFactory.getLogger(JobStatusNotifierImpl.class);

	@Autowired
	private JobConfigurationService service;

	public final void notify(final JobExecutionInfo jobExecutionInfo) {
		logger.debug("In notify: job {}, jobconfiguration id: {}", jobExecutionInfo.getId(), jobExecutionInfo.getJobConfigurationId());

		JobConfiguration job = service.get(Long.parseLong(jobExecutionInfo.getJobConfigurationId()));
		if (job != null) {
			logger.debug("updating jobconfiguration: " + job.getId());
			job.setJobStatus(jobExecutionInfo.getStatus());
			job.setLastJobExecution(jobExecutionInfo.getId());
			service.saveOrUpdate(job);
		}
	}

	@Override
	public void notify(JobExecutionException jobExecutionException, String jobConfigurationId) {
		if(jobConfigurationId != null) {
			logger.error(jobExecutionException.getMessage());
			jobExecutionException.printStackTrace();
			JobConfiguration job = service.get(Long.parseLong(jobConfigurationId));
			job.setJobStatus(BatchStatus.FAILED);
			service.saveOrUpdate(job);
		}
	}
}
