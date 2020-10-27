package org.powo.persistence.dao.hibernate;

import java.util.List;

import org.powo.model.JobConfiguration;
import org.powo.model.hibernate.Fetch;
import org.powo.persistence.dao.JobConfigurationDao;
import org.springframework.stereotype.Repository;

@Repository
public class JobConfigurationDaoImpl extends DaoImpl<JobConfiguration> implements JobConfigurationDao {

	public JobConfigurationDaoImpl() {
		super(JobConfiguration.class);
	}

	public List<JobConfiguration> listByName(String jobName) {
		return getSession().createQuery("SELECT c FROM JobConfiguration c where jobName = :jobName", JobConfiguration.class)
				.setParameter("jobName", jobName)
				.getResultList();
	}

	@Override
	protected Fetch[] getProfile(String profile) {
		return new Fetch[] {};
	}
}
