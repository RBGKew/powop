package org.powo.persistence.dao;

import java.util.List;

import javax.persistence.NoResultException;

import org.hibernate.Session;
import org.hibernate.SessionFactory;
import org.powo.model.JobConfiguration;
import org.powo.model.exception.NotFoundException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

@Transactional
@Repository
public class JobConfigurationDao {

	private SessionFactory sessionFactory;

	@Autowired
	public JobConfigurationDao(SessionFactory sessionFactory) {
		this.sessionFactory = sessionFactory;
	}

	private Session session() {
		return sessionFactory.getCurrentSession();
	}

	public void save(JobConfiguration conf) {
		session().save(conf);
	}

	public void saveOrUpdate(JobConfiguration conf) {
		session().saveOrUpdate(conf);
	}

	public JobConfiguration get(long id) {
		try {
			JobConfiguration jobConfiguration = session().get(JobConfiguration.class, id);
			return jobConfiguration;
		} catch (NoResultException e) {
			throw new NotFoundException(JobConfiguration.class, id);
		}
	}

	public JobConfiguration get(String identifier) {
		try {
			JobConfiguration jobConfiguration = session()
					.createQuery("SELECT c FROM JobConfiguration c where identifier = :identifier", JobConfiguration.class)
					.setParameter("identifier", identifier)
					.getSingleResult();
			return jobConfiguration;
		} catch (NoResultException e) {
			throw new NotFoundException(JobConfiguration.class, identifier);
		}
	}

	public void refresh(JobConfiguration conf) {
		session().refresh(conf);
	}

	public List<JobConfiguration> list() {
		return session().createQuery("SELECT c FROM JobConfiguration c", JobConfiguration.class)
				.getResultList();
	}

	public List<JobConfiguration> list(final Integer page, final Integer size) {
		return session().createQuery("SELECT c FROM JobConfiguration c", JobConfiguration.class)
				.setFirstResult(page * size)
				.setMaxResults(size)
				.getResultList();
	}

	public List<JobConfiguration> listByName(String jobName) {
		return session().createQuery("SELECT c FROM JobConfiguration c where jobName = :jobName", JobConfiguration.class)
				.setParameter("jobName", jobName)
				.getResultList();
	}
}
