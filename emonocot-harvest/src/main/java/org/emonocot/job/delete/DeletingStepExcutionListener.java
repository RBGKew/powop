package org.emonocot.job.delete;

import org.emonocot.api.job.JobExecutionInfo;
import org.emonocot.api.job.JobStatusNotifier;
import org.springframework.batch.core.ExitStatus;
import org.springframework.batch.core.JobExecution;
import org.springframework.batch.core.StepExecution;
import org.springframework.batch.core.StepExecutionListener;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Component;

@Component
public class DeletingStepExcutionListener implements StepExecutionListener{

	private JobStatusNotifier jobStatusNotifier;
	private String resourceIdentifier;

	public void setJobStatusNotifier(JobStatusNotifier jobStatusNotifier) {
		this.jobStatusNotifier = jobStatusNotifier;
	}
	public void setResourceIdentifier(String identifier) {
		this.resourceIdentifier = identifier;
	}

	@Override
	public ExitStatus afterStep(StepExecution step) {
		ExitStatus exitStatus = new ExitStatus("RECORDS DELETED");
		exitStatus.addExitDescription("All records deleted. press delete again to remove resource");
		JobExecution jobExecution = step.getJobExecution();
		if (resourceIdentifier != null) {
			JobExecutionInfo jobExecutionInfo = new JobExecutionInfo(jobExecution);
			jobStatusNotifier.notify(jobExecutionInfo);
		}
		return exitStatus;
	}

	@Override
	public void beforeStep(StepExecution begin) { }
}
