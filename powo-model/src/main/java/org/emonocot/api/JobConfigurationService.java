package org.emonocot.api;

import java.util.List;

import org.emonocot.model.JobConfiguration;
import org.emonocot.pager.Page;

public interface JobConfigurationService {

	public void save(JobConfiguration conf);

	public JobConfiguration get(Long id);

	public void refresh(JobConfiguration conf);

	public void saveOrUpdate(JobConfiguration job);

	public Page<JobConfiguration> list(int page, int size);

	public List<JobConfiguration> listByName(String jobName);
}
