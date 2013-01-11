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
			JobExecutionInfo jobExecutionInfo = new JobExecutionInfo(jobExecution, baseUrl);
			jobStatusNotifier.notify(jobExecutionInfo);
		}
    }

}
