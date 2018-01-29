package org.emonocot.service.impl;

import org.emonocot.api.JobConfigurationService;
import org.emonocot.api.ResourceService;
import org.emonocot.api.ResourceWithJobService;
import org.emonocot.factories.JobConfigurationFactory;
import org.emonocot.model.JobConfiguration;
import org.emonocot.model.marshall.json.ResourceWithJob;
import org.emonocot.model.registry.Resource;
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
