package org.emonocot.service.impl;

import java.util.List;

import org.emonocot.api.JobListService;
import org.emonocot.api.job.JobExecutionException;
import org.emonocot.api.job.JobLaunchRequest;
import org.emonocot.api.job.JobLauncher;
import org.emonocot.model.JobConfiguration;
import org.emonocot.model.JobList;
import org.emonocot.model.constants.JobListStatus;
import org.emonocot.model.marshall.json.JobSchedule;
import org.emonocot.pager.DefaultPageImpl;
import org.emonocot.pager.Page;
import org.emonocot.persistence.dao.JobListDao;
import org.joda.time.DateTime;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
public class JobListServiceImpl implements JobListService {

	private final Logger log = LoggerFactory.getLogger(JobListServiceImpl.class);

	@Autowired
	private JobListDao dao;

	@Autowired
	private JobLauncher jobLauncher;

	@Transactional
	public void save(JobList list) {
		dao.save(list);
	}

	@Transactional
	public void saveOrUpdate(JobList list) {
		dao.saveOrUpdate(list);
	}

	@Transactional
	public void delete(Long id) {
		dao.delete(id);
	}

	@Transactional(readOnly = true)
	public JobList get(Long id) {
		return dao.get(id);
	}

	@Transactional(readOnly = true)
	public List<JobList> list() {
		return dao.list();
	}

	@Transactional
	public void refresh(JobList list) {
		dao.refresh(list);
	}

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
	public Page<JobList> list(int page, int size) {
		return new DefaultPageImpl<>(dao.list(page, size), page, size);
	}

	@Transactional
	public JobList schedule(Long id, JobSchedule schedule) {
		JobList jobList = get(id);

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
