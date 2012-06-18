package org.emonocot.api.job;


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
	JobLaunchRequest launch(JobLaunchRequest request) throws JobExecutionException;

}
