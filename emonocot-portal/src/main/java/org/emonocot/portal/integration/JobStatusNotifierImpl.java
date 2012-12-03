package org.emonocot.portal.integration;

import org.emonocot.api.ResourceService;
import org.emonocot.api.job.JobExecutionInfo;
import org.emonocot.api.job.JobStatusNotifier;
import org.emonocot.model.registry.Resource;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
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
     * @param newJobService
     *            the jobService to set
     */
    @Autowired
    public final void setJobService(final ResourceService newJobService) {
        this.service = newJobService;
    }

    /**
     * @param jobExecutionInfo
     *            Set the job execution info
     */
    public final void notify(final JobExecutionInfo jobExecutionInfo) {
        logger.debug("In notify " + jobExecutionInfo.getId());

        Resource job = service.findByJobId(jobExecutionInfo.getId());
		if (job != null) {
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

			service.saveOrUpdate(job);
		}
        logger.debug("Returning");
    }

}
