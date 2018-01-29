package org.powo.service.impl;

import org.powo.api.JobConfigurationService;
import org.powo.api.ResourceService;
import org.powo.api.ResourceWithJobService;
import org.powo.factories.JobConfigurationFactory;
import org.powo.model.JobConfiguration;
import org.powo.model.marshall.json.ResourceWithJob;
import org.powo.model.registry.Resource;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class ResourceWithJobServiceImpl implements ResourceWithJobService {

	private final Logger logger = LoggerFactory.getLogger(ResourceWithJobServiceImpl.class);

	@Autowired
	private ResourceService resourceService;

	@Autowired
	private JobConfigurationService jobConfigurationService;

	public void save(ResourceWithJob resourceWithJob) {
		resourceService.save(resourceWithJob.getResource());

		resourceWithJob.setJobConfiguration(JobConfigurationFactory.resourceJob(resourceWithJob));
		resourceWithJob.getResource().setJobConfiguration(resourceWithJob.getJobConfiguration());

		jobConfigurationService.save(resourceWithJob.getJobConfiguration());
		resourceService.saveOrUpdate(resourceWithJob.getResource());
	}

	public void saveOrUpdate(ResourceWithJob resourceWithJob) {
		logger.debug("Updating " + resourceWithJob.getResource());
		Resource resource = resourceWithJob.getResource();
		Resource persistedResource = resourceService.load(resource.getId());
		JobConfiguration jc = JobConfigurationFactory.resourceJob(resourceWithJob);

		if(persistedResource.getJobConfiguration() != null) {
			jc.setId(persistedResource.getJobConfiguration().getId());
		}

		resource.setJobConfiguration(jc);

		resourceService.saveOrUpdate(resource);
		jobConfigurationService.saveOrUpdate(jc);
	}
}
