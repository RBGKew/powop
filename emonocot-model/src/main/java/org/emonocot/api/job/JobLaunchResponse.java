package org.emonocot.api.job;

import org.codehaus.jackson.map.annotate.JsonDeserialize;
import org.emonocot.model.marshall.json.JobExecutionInfoDeserializer;

/**
 *
 * @author ben
 *
 */
public class JobLaunchResponse {

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
    @JsonDeserialize(using = JobExecutionInfoDeserializer.class)
    public final void setJobExecution(final JobExecutionInfo newJobResponse) {
        this.jobExecution = newJobResponse;
    }

}
