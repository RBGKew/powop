package org.emonocot.harvest.service;

import java.util.List;

import org.emonocot.api.job.JobExecutionException;
import org.emonocot.api.job.JobLaunchRequest;
import org.emonocot.api.job.JobLauncher;
import org.emonocot.model.JobConfiguration;
import org.emonocot.model.JobList;
import org.emonocot.model.constants.JobListStatus;
import org.emonocot.pager.DefaultPageImpl;
import org.emonocot.pager.Page;
import org.emonocot.persistence.dao.JobListDao;
import org.joda.time.DateTime;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
public class JobListService {

	@Autowired
	private JobListDao dao;

	@Autowired
	private JobLauncher jobLauncher;

	@Transactional
	public void save(JobList list) {
		dao.save(list);
	}

	@Transactional
	private void saveOrUpdate(JobList list) {
		dao.saveOrUpdate(list);
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
			jl.setStatus(JobListStatus.Scheduled);
			saveOrUpdate(jl);
		}
	}

	@Transactional
	public Page<JobList> list(int page, int size) {
		return new DefaultPageImpl<>(dao.list(page, size), page, size);
	}

	public void runAvailable() {
		scheduleAvailable();

		for(JobList jobs : scheduled()) {
			JobConfiguration job = jobs.getJobConfigurations().get(jobs.getCurrentJob());
			JobLaunchRequest launchRequest = new JobLaunchRequest(job);
			launchRequest.addParameter("run.at", DateTime.now().toString());
			launchRequest.addParameter("job.list.id", jobs.getId().toString());

			try {
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
