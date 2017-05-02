package org.emonocot.harvest.common;

import org.emonocot.harvest.service.JobListService;
import org.emonocot.model.JobList;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.batch.core.JobExecution;
import org.springframework.batch.core.listener.JobExecutionListenerSupport;
import org.springframework.beans.factory.annotation.Autowired;

public class JobSequenceListener extends JobExecutionListenerSupport {

	Logger logger = LoggerFactory.getLogger(JobSequenceListener.class);

	@Autowired
	JobListService jobListService;

	@Override
	public void afterJob(JobExecution jobExecution) {
		String jobListId = jobExecution.getJobParameters().getString("job.list.id");

		if(jobListId != null) {
			JobList list = jobListService.load(Long.parseLong(jobListId));
			logger.debug("Scheduling next job for jobList - {}", list.getDescription());
			if(list.hasNextJob()) {
				jobListService.scheduleNextJob(list);
			} else {
				jobListService.updateNextAvailableDate(list);
			}
		}
	}
}