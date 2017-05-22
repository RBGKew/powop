package org.emonocot.persistence.dao;

import java.util.List;

import org.emonocot.model.JobConfiguration;
import org.emonocot.model.exception.NotFoundException;
import org.hibernate.Session;
import org.hibernate.SessionFactory;
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
		JobConfiguration jobConfiguration = session().get(JobConfiguration.class, id);

		if(jobConfiguration == null) {
			throw new NotFoundException(JobConfiguration.class, id);
		}

		return jobConfiguration;
	}

	public void refresh(JobConfiguration conf) {
		session().refresh(conf);
	}

	public List<JobConfiguration> list(final Integer page, final Integer size) {
		return session().createQuery("SELECT c FROM JobConfiguration c", JobConfiguration.class)
				.setFirstResult(page * size)
				.setMaxResults(size)
				.getResultList();
	}
}
