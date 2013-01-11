package org.emonocot.harvest.common;

import org.emonocot.api.ResourceService;
import org.emonocot.api.job.JobExecutionException;
import org.emonocot.api.job.JobExecutionInfo;
import org.emonocot.api.job.JobStatusNotifier;
import org.emonocot.model.registry.Resource;
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

        Resource job = service.find(jobExecutionInfo.getResourceIdentifier());
		if (job != null) {
			job.setJobId(jobExecutionInfo.getId());
			job.setDuration(jobExecutionInfo.getDuration());
			job.setExitCode(jobExecutionInfo.getExitCode());
			job.setExitDescription(jobExecutionInfo.getExitDescription());
			if (jobExecutionInfo.getJobInstance() != null) {
				job.setJobInstance(jobExecutionInfo.getJobInstance()
						.getResource());
			}
			job.setResource(jobExecutionInfo.getResource());
			job.setStartTime(jobExecutionInfo.getStartTime());
			job.setStatus(jobExecutionInfo.getStatus());
			job.setProcessSkip(jobExecutionInfo.getProcessSkip());
			job.setRecordsRead(jobExecutionInfo.getRecordsRead());
			job.setReadSkip(jobExecutionInfo.getReadSkip());
			job.setWriteSkip(jobExecutionInfo.getWriteSkip());
			job.setWritten(jobExecutionInfo.getWritten());
			if(job.getStatus().equals(BatchStatus.COMPLETED)) {
				job.setLastHarvested(jobExecutionInfo.getStartTime());
			}

			service.saveOrUpdate(job);
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
