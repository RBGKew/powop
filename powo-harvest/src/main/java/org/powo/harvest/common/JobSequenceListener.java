package org.powo.harvest.common;

import org.powo.api.JobListService;
import org.powo.model.JobList;
import org.powo.model.constants.JobListStatus;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.batch.core.JobExecution;
import org.springframework.batch.core.JobExecutionListener;
import org.springframework.beans.factory.annotation.Autowired;

public class JobSequenceListener implements JobExecutionListener {

	Logger logger = LoggerFactory.getLogger(JobSequenceListener.class);

	@Autowired
	JobListService jobListService;

	@Override
	public void beforeJob(JobExecution jobExecution) {
		String jobListId = jobExecution.getJobParameters().getString("job.list.id");

		if(jobListId != null) {
			Long id = Long.parseLong(jobListId);
			JobList list = jobListService.get(id);
			if(list != null) {
				list.setStatus(JobListStatus.Running);
				jobListService.saveOrUpdate(list);
			} else {
				logger.warn("Unknown job list with id: {}", id);
			}
		}
	}

	@Override
	public void afterJob(JobExecution jobExecution) {
		String jobListId = jobExecution.getJobParameters().getString("job.list.id");

		if(jobListId != null) {
			Long id = Long.parseLong(jobListId);
			JobList list = jobListService.get(id);
			if(list != null) {
				logger.debug("Scheduling next job for jobList - {}", list.getDescription());
				if(list.hasNextJob()) {
					jobListService.scheduleNextJob(list);
				} else {
					jobListService.updateNextAvailableDate(list);
				}
			} else {
				logger.warn("Unknown job list with id: {}", id);
			}
		}
	}
}