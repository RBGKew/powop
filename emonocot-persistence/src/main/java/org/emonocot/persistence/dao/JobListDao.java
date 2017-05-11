package org.emonocot.persistence.dao;

import java.util.List;

import org.emonocot.model.JobList;
import org.hibernate.Session;
import org.hibernate.SessionFactory;
import org.joda.time.DateTime;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

@Repository
@Transactional
public class JobListDao {

	private SessionFactory sessionFactory;

	@Autowired
	public JobListDao(SessionFactory sessionFactory) {
		this.sessionFactory = sessionFactory;
	}

	private Session session() {
		return sessionFactory.getCurrentSession();
	}

	public void save(JobList list) {
		session().save(list);
	}

	public void saveOrUpdate(JobList list) {
		session().saveOrUpdate(list);
	}

	public JobList get(long id) {
		return session().get(JobList.class, id);
	}

	public void refresh(JobList list) {
		session().refresh(list);
	}

	/**
	 * Full list of JobLists
	 * @return List of all JobLists
	 */
	public List<JobList> list() {
		return session().createQuery("SELECT j FROM JobList j", JobList.class)
				.getResultList();
	}

	/**
	 * Full list of JobLists
	 * @return List of all JobLists
	 */
	public List<JobList> list(final Integer page, final Integer size) {
		return session().createQuery("SELECT j FROM JobList j", JobList.class)
				.setFirstResult(page * size)
				.setMaxResults(size)
				.getResultList();
	}

	/**
	 * JobLists which have a job that is scheduled to run
	 * @return List of scheduled JobLists
	 */
	public List<JobList> scheduled() {
		return session().createQuery("SELECT j from JobList j WHERE status = 'Scheduled'", JobList.class)
				.getResultList();
	}

	/**
	 * JobLists which are available to be scheduled due to time being greater than specified nextRun time
	 * @return List of schedulable JobLists
	 */
	public List<JobList> schedulable() {
		return session().createQuery("SELECT j FROM JobList j "
				+ "WHERE nextRun <= :currentTime "
				+ "  AND (status is null OR status != 'Running')", JobList.class)
				.setParameter("currentTime", DateTime.now())
				.getResultList();
	}
}