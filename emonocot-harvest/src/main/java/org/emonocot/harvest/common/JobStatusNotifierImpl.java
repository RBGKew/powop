package org.emonocot.harvest.common;

import org.emonocot.api.ResourceService;
import org.emonocot.api.job.JobExecutionException;
import org.emonocot.api.job.JobExecutionInfo;
import org.emonocot.api.job.JobStatusNotifier;
import org.emonocot.model.registry.Resource;
import org.joda.time.DateTime;
import org.joda.time.Duration;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.batch.core.BatchStatus;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

/**
 *
 * @author ben
 *
 */
@Service
public class JobStatusNotifierImpl implements JobStatusNotifier {
    /**
    *
    */
    private static Logger logger = LoggerFactory
            .getLogger(JobStatusNotifierImpl.class);

    /**
    *
    */
    private ResourceService service;

    /**
     * @param resourceService
     *            the jobService to set
     */
    @Autowired
    public final void setResourceService(final ResourceService resourceService) {
        this.service = resourceService;
    }

    /**
     * @param jobExecutionInfo
     *            Set the job execution info
     */
    public final void notify(final JobExecutionInfo jobExecutionInfo) {
        logger.debug("In notify " + jobExecutionInfo.getId());

        Resource resource = service.find(jobExecutionInfo.getResourceIdentifier());
		if (resource != null) {
			resource.setJobId(jobExecutionInfo.getId());
			resource.setDuration(new Duration(new DateTime(0), jobExecutionInfo.getDuration()));
			resource.setExitCode(jobExecutionInfo.getExitCode());
			resource.setExitDescription(jobExecutionInfo.getExitDescription());
			if (jobExecutionInfo.getJobInstance() != null) {
				resource.setJobInstance(jobExecutionInfo.getJobInstance());
			}
			resource.setBaseUrl(jobExecutionInfo.getBaseUrl());
			resource.setResource(jobExecutionInfo.getResource());
			resource.setStartTime(jobExecutionInfo.getStartTime());
			resource.setStatus(jobExecutionInfo.getStatus());
			resource.setProcessSkip(jobExecutionInfo.getProcessSkip());
			resource.setRecordsRead(jobExecutionInfo.getRecordsRead());
			resource.setReadSkip(jobExecutionInfo.getReadSkip());
			resource.setWriteSkip(jobExecutionInfo.getWriteSkip());
			resource.setWritten(jobExecutionInfo.getWritten());
			if(resource.getStatus().equals(BatchStatus.COMPLETED)) {
				resource.setLastHarvested(jobExecutionInfo.getStartTime());
				resource.updateNextAvailableDate();
				
			} else if(resource.getStatus().equals(BatchStatus.FAILED)) {
				resource.setNextAvailableDate(null);				
			}

			service.saveOrUpdate(resource);
		}
    }

	@Override
	public void notify(JobExecutionException jobExecutionException, String resourceIdentifier) {
		if(resourceIdentifier != null) {
		    Resource job = service.find(resourceIdentifier);
		    job.setJobId(null);
			job.setDuration(null);
			job.setExitCode("FAILED");
			job.setExitDescription(jobExecutionException.getLocalizedMessage());
			job.setJobInstance(null);
			job.setResource(null);
			job.setStartTime(null);
			job.setStatus(BatchStatus.FAILED);
			job.setProcessSkip(0);
			job.setRecordsRead(0);
			job.setReadSkip(0);
			job.setWriteSkip(0);
			job.setWritten(0);

			service.saveOrUpdate(job);
		}
		
	}

}
