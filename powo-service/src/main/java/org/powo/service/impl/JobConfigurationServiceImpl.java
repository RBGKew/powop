package org.powo.service.impl;

import java.util.List;
import java.util.Map.Entry;
import java.util.Optional;

import org.powo.api.JobConfigurationService;
import org.powo.api.ResourceService;
import org.powo.model.JobConfiguration;
import org.powo.model.registry.Resource;
import org.powo.persistence.dao.JobConfigurationDao;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class JobConfigurationServiceImpl extends ServiceImpl<JobConfiguration, JobConfigurationDao> implements JobConfigurationService {

	@Autowired
	private ResourceService resourceService;

	@Autowired
	public final void setDao(final JobConfigurationDao jobConfigurationDao) {
		super.dao = jobConfigurationDao;
	}

	@Override
	public List<JobConfiguration> listByName(String jobName) {
		return dao.listByName(jobName);
	}

	@Override
	public JobConfiguration save(JobConfiguration jc) {
		Optional<Entry<String, String>> param = jc.getParameters().entrySet().stream()
				.filter(entry -> entry.getKey().equals("resource.identifier"))
				.findFirst();

		if(param.isPresent()) {
			Resource resource = resourceService.find(param.get().getValue());
			if(resource != null) {
				resource.setJobConfiguration(jc);
				resourceService.saveOrUpdate(resource);
				return jc;
			}
		}

		return super.save(jc);
	}
}
