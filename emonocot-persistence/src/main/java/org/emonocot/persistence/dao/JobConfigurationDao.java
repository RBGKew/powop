package org.emonocot.persistence.dao;

import org.emonocot.model.JobConfiguration;
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

	public JobConfiguration load(long id) {
		return session().load(JobConfiguration.class, id);
	}
}