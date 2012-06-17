package org.emonocot.api.job;

import java.io.Serializable;


/**
 *
 * @author ben
 *
 */
public class JobLaunchResponse implements Serializable {

    /**
     *
     */
    private JobExecutionInfo jobExecution;

    /**
     * @return the job execution
     */
    public final JobExecutionInfo getJobExecution() {
        return jobExecution;
    }

    /**
     * @param newJobResponse the job execution to set
     */
    public final void setJobExecution(final JobExecutionInfo newJobResponse) {
        this.jobExecution = newJobResponse;
    }

}
