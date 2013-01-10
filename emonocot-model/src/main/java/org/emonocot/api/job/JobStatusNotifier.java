package org.emonocot.api.job;

/**
 *
 * @author ben
 *
 */
public interface JobStatusNotifier {

    /**
     *
     * @param jobExecutionInfo Set the job execution information
     */
    void notify(JobExecutionInfo jobExecutionInfo);

	void notify(JobExecutionException jobExecutionException, String resourceIdentifier);

}
