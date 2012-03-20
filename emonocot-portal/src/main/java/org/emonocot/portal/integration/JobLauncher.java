package org.emonocot.portal.integration;

import org.emonocot.api.job.JobExecutionInfo;
import org.springframework.batch.core.JobExecutionException;
import org.springframework.batch.integration.launch.JobLaunchRequest;

/**
 *
 * @author ben
 *
 */
public interface JobLauncher {
    /**
     *
     * @param request
     *            Set the request
     * @return the JobExecutionInfo
     * @throws JobExecutionException
     *             if there is a problem
     */
    JobExecutionInfo launch(JobLaunchRequest request)
            throws JobExecutionException;

}
