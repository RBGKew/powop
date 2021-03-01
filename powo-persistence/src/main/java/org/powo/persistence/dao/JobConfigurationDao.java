package org.powo.persistence.dao;

import java.util.List;

import org.powo.model.JobConfiguration;

public interface JobConfigurationDao extends Dao<JobConfiguration> {
	public List<JobConfiguration> listByName(String jobName);
}
