package org.powo.api;

import java.util.List;

import org.powo.model.JobList;
import org.powo.model.marshall.json.JobSchedule;
import org.powo.pager.Page;

public interface JobListService {

	public void save(JobList list);

	public void saveOrUpdate(JobList list);

	public void delete(Long id);

	public JobList get(Long id);

	public List<JobList> list();

	public void refresh(JobList list);

	public List<JobList> scheduled();

	public List<JobList> schedulable();

	public void scheduleAvailable();

	public Page<JobList> list(int page, int size);

	public JobList schedule(Long id, JobSchedule schedule);

	public void runAvailable();

	public void scheduleNextJob(JobList list);

	public void updateNextAvailableDate(JobList list);

}
