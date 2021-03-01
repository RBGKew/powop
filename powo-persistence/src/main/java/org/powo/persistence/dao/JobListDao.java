package org.powo.persistence.dao;

import java.util.List;

import org.powo.model.JobList;

public interface JobListDao extends Dao<JobList> {
	/**
	 * JobLists which have a job that is scheduled to run
	 * @return List of scheduled JobLists
	 */
	public List<JobList> scheduled();

	/**
	 * JobLists which are available to be scheduled due to time being greater than specified nextRun time
	 * @return List of schedulable JobLists
	 */
	public List<JobList> schedulable();
}