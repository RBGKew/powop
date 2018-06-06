package org.powo.api;

import java.util.List;

import org.powo.model.JobList;
import org.powo.model.marshall.json.JobSchedule;

public interface JobListService extends Service<JobList> {

	public List<JobList> scheduled();

	public List<JobList> schedulable();

	public void scheduleAvailable();

	public JobList schedule(String identifier, JobSchedule schedule);

	public void runAvailable();

	public void scheduleNextJob(JobList list);

	public void updateNextAvailableDate(JobList list);
}
