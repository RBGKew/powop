package org.emonocot.harvest.common;

import org.emonocot.api.job.JobExecutionInfo;
import org.emonocot.api.job.JobInstanceInfo;
import org.emonocot.api.job.JobStatusNotifier;
import org.joda.time.DateTime;
import org.springframework.batch.core.JobExecution;
import org.springframework.batch.core.StepExecution;
import org.springframework.batch.core.listener.JobExecutionListenerSupport;

/**
 *
 * @author ben
 *
 */
public class JobStatusListener extends JobExecutionListenerSupport {

    private JobStatusNotifier jobStatusNotifier;

    private String baseUrl;
    
    public void setBaseUrl(String newBaseUrl) {
        this.baseUrl = newBaseUrl;
    }

    public void setJobStatusNotifier(
            JobStatusNotifier newJobStatusNotifier) {
        this.jobStatusNotifier = newJobStatusNotifier;
    }

    @Override
    public void afterJob(JobExecution jobExecution) {
    	this.notify(jobExecution);		
    }
    
    @Override
    public void beforeJob(JobExecution jobExecution) {
    	this.notify(jobExecution);		
    }
    
    private void notify(JobExecution jobExecution) {
    	if (jobExecution.getJobInstance().getJobParameters().getString("resource.identifier") != null) {
			JobExecutionInfo jobExecutionInfo = new JobExecutionInfo();
			
			jobExecutionInfo.setResourceIdentifier(jobExecution.getJobInstance().getJobParameters().getString("resource.identifier"));
			DateTime startTime = new DateTime(jobExecution.getStartTime());
			DateTime endTime = new DateTime(jobExecution.getEndTime());
			jobExecutionInfo.setDuration(endTime.minus(startTime.getMillis()));
			jobExecutionInfo.setStartTime(startTime);
			jobExecutionInfo.setExitDescription(jobExecution.getExitStatus().getExitDescription());
			jobExecutionInfo.setExitCode(jobExecution.getExitStatus().getExitCode());
			jobExecutionInfo.setId(jobExecution.getId());
			JobInstanceInfo jobInstanceInfo = new JobInstanceInfo();
			jobInstanceInfo.setResource(baseUrl + "/jobs/"	+ jobExecution.getJobInstance().getJobName() + "/"	+ jobExecution.getJobInstance().getId() + ".json");
			jobExecutionInfo.setJobInstance(jobInstanceInfo);
			jobExecutionInfo.setResource(baseUrl + "/jobs/executions/"	+ jobExecution.getId() + ".json");
			jobExecutionInfo.setStatus(jobExecution.getStatus());
			Integer read = 0;
			Integer readSkip = 0;
			Integer processSkip = 0;
			Integer write = 0;
			Integer writeSkip = 0;
			for (StepExecution stepExecution : jobExecution.getStepExecutions()) {
				read += stepExecution.getReadCount();
				readSkip += stepExecution.getReadSkipCount();
				processSkip += stepExecution.getProcessSkipCount();
				write += stepExecution.getWriteCount();
				writeSkip += stepExecution.getWriteSkipCount();
			}
			jobExecutionInfo.setRecordsRead(read);
			jobExecutionInfo.setReadSkip(readSkip);
			jobExecutionInfo.setProcessSkip(processSkip);
			jobExecutionInfo.setWriteSkip(writeSkip);
			jobExecutionInfo.setWritten(write);
			jobStatusNotifier.notify(jobExecutionInfo);
		}
    }

}
