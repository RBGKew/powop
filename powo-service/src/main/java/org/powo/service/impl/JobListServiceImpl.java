package org.powo.service.impl;

import java.util.List;

import org.joda.time.DateTime;
import org.powo.api.JobListService;
import org.powo.api.job.JobExecutionException;
import org.powo.api.job.JobLaunchRequest;
import org.powo.api.job.JobLauncher;
import org.powo.model.JobConfiguration;
import org.powo.model.JobList;
import org.powo.model.constants.JobListStatus;
import org.powo.model.marshall.json.JobSchedule;
import org.powo.persistence.dao.JobListDao;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
public class JobListServiceImpl extends ServiceImpl<JobList, JobListDao> implements JobListService {

	private final Logger log = LoggerFactory.getLogger(JobListServiceImpl.class);

	@Autowired
	private JobListDao dao;

	@Autowired
	private JobLauncher jobLauncher;

	@Transactional(readOnly = true)
	public List<JobList> scheduled() {
		return dao.scheduled();
	}

	@Transactional(readOnly = true)
	public List<JobList> schedulable() {
		return dao.schedulable();
	}

	@Transactional
	public void scheduleAvailable() {
		for(JobList jl : schedulable()) {
			log.debug("Scheduling JobList[{}]", jl.getDescription());
			jl.setStatus(JobListStatus.Scheduled);
			saveOrUpdate(jl);
		}
	}

	@Transactional
	public JobList schedule(String id, JobSchedule schedule) {
		JobList jobList = find(id);

		if(jobList.isSchedulable()) {
			jobList.setCurrentJob(0);
			jobList.setNextRun(schedule.getNextRun());
			jobList.setSchedulingPeriod(schedule.getSchedulingPeriod());
			saveOrUpdate(jobList);
		}

		return jobList;
	}

	public void runAvailable() {
		scheduleAvailable();

		for(JobList jobs : scheduled()) {
			JobConfiguration job = jobs.getJobConfigurations().get(jobs.getCurrentJob());
			log.debug("Scheduling {} as part of JobList[{}] run", job, jobs.getDescription());
			JobLaunchRequest launchRequest = new JobLaunchRequest(job);
			launchRequest.addParameter("run.at", DateTime.now().toString());
			launchRequest.addParameter("job.list.id", jobs.getId().toString());

			try {
				log.info("Running {} as part of JobList[{}] run", job, jobs.getDescription());
				jobLauncher.launch(launchRequest);
			} catch (JobExecutionException e) {
				e.printStackTrace();
			}
		}
	}

	public void scheduleNextJob(JobList list) {
		list.scheduleNextJob();
		saveOrUpdate(list);
	}

	public void updateNextAvailableDate(JobList list) {
		list.updateNextAvailableDate();
		saveOrUpdate(list);
	}
}
