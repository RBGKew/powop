package org.powo.service.impl;

import java.util.List;

import org.powo.api.JobConfigurationService;
import org.powo.model.JobConfiguration;
import org.powo.persistence.dao.JobConfigurationDao;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class JobConfigurationServiceImpl extends ServiceImpl<JobConfiguration, JobConfigurationDao> implements JobConfigurationService {

	@Autowired
	public final void setMeasurementOrFactDao(final JobConfigurationDao jobConfigurationDao) {
		super.dao = jobConfigurationDao;
	}

	@Override
	public List<JobConfiguration> listByName(String jobName) {
		return dao.listByName(jobName);
	}
}
