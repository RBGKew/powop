package org.powo.api;

import java.util.List;

import org.powo.model.JobConfiguration;
import org.powo.pager.Page;

public interface JobConfigurationService {

	public void save(JobConfiguration conf);

	public JobConfiguration get(Long id);

	public JobConfiguration get(String identifier);

	public void refresh(JobConfiguration conf);

	public void saveOrUpdate(JobConfiguration job);

	public Page<JobConfiguration> list(int page, int size);

	public List<JobConfiguration> list();

	public List<JobConfiguration> listByName(String jobName);
}
