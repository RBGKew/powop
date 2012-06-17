package org.emonocot.harvest.integration;

import org.emonocot.api.job.JobExecutionInfo;
import org.emonocot.api.job.JobInstanceInfo;
import org.emonocot.api.job.JobLaunchResponse;
import org.joda.time.DateTime;
import org.springframework.batch.core.JobExecution;
import org.springframework.integration.Message;
import org.springframework.integration.message.GenericMessage;

/**
 *
 * @author ben
 *
 */
public class JobExecutionToInfoAdapter {
    /**
     *
     */
    private String baseUrl;

    /**
     * @param newBaseUrl
     *            the baseUrl to set
     */
    public final void setBaseUrl(final String newBaseUrl) {
        this.baseUrl = newBaseUrl;
    }

    /**
     *
     * @param incomingMessage
     *            Set the incoming message
     * @return the transformed message
     */
    public final Message<JobLaunchResponse> adapt(
            final Message<JobExecution> incomingMessage) {
    	JobExecutionInfo jobExecutionInfo = new JobExecutionInfo();
        JobLaunchResponse jobLaunchResponse = new JobLaunchResponse();
        jobLaunchResponse.setJobExecution(jobExecutionInfo);
        JobExecution jobExecution = incomingMessage.getPayload();
        DateTime startTime = new DateTime(jobExecution.getStartTime());
        DateTime endTime = new DateTime(jobExecution.getEndTime());
        jobExecutionInfo.setDuration(endTime.minus(startTime.getMillis()));
        jobExecutionInfo.setStartTime(startTime);
        jobExecutionInfo.setExitDescription(jobExecution.getExitStatus()
                .getExitDescription());
        jobExecutionInfo
                .setExitCode(jobExecution.getExitStatus().getExitCode());
        jobExecutionInfo.setId(jobExecution.getId());
        JobInstanceInfo jobInstanceInfo = new JobInstanceInfo();
        jobInstanceInfo.setResource(baseUrl + "/jobs/"
                + jobExecution.getJobInstance().getJobName() + "/"
                + jobExecution.getJobId() + ".json");
        jobExecutionInfo.setJobInstance(jobInstanceInfo);
        jobExecutionInfo.setResource(baseUrl + "/jobs/executions/"
                + jobExecution.getJobInstance().getId() + ".json");
        jobExecutionInfo.setStatus(jobExecution.getStatus());

        return new GenericMessage<JobLaunchResponse>(jobLaunchResponse,
                incomingMessage.getHeaders());
    }
}
