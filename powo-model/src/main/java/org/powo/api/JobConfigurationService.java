package org.powo.api;

import java.util.List;

import org.powo.model.JobConfiguration;

public interface JobConfigurationService extends Service<JobConfiguration> {

	public List<JobConfiguration> listByName(String jobName);

}
