package org.emonocot.harvest.common;

import org.emonocot.api.job.JobExecutionInfo;
import org.emonocot.api.job.JobInstanceInfo;
import org.emonocot.api.job.JobStatusNotifier;
import org.joda.time.DateTime;
import org.springframework.batch.core.JobExecution;
import org.springframework.batch.core.listener.JobExecutionListenerSupport;

/**
 *
 * @author ben
 *
 */
public class JobStatusListener extends JobExecutionListenerSupport {

    /**
     *
     */
    private JobStatusNotifier jobStatusNotifier;

    /**
     * @param newBaseUrl the baseUrl to set
     */
    public final void setBaseUrl(final String newBaseUrl) {
        this.baseUrl = newBaseUrl;
    }


    /**
     *
     */
    private String baseUrl;


    /**
     * @param newJobStatusNotifier the jobStatusNotifier to set
     */
    public final void setJobStatusNotifier(
            final JobStatusNotifier newJobStatusNotifier) {
        this.jobStatusNotifier = newJobStatusNotifier;
    }


    /**
     * @param jobExecution Set the job execution
     */
    @Override
    public final void afterJob(final JobExecution jobExecution) {
    	JobExecutionInfo jobExecutionInfo = new JobExecutionInfo();
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
        jobStatusNotifier.notify(jobExecutionInfo);
    }

}
