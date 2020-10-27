package org.powo.persistence.dao.hibernate;

import java.util.List;

import org.joda.time.DateTime;
import org.powo.model.JobList;
import org.powo.model.hibernate.Fetch;
import org.powo.persistence.dao.JobListDao;
import org.springframework.stereotype.Repository;

@Repository
public class JobListDaoImpl extends DaoImpl<JobList> implements JobListDao {

	public JobListDaoImpl() {
		super(JobList.class);
	}

	/**
	 * JobLists which have a job that is scheduled to run
	 * @return List of scheduled JobLists
	 */
	public List<JobList> scheduled() {
		return getSession().createQuery("SELECT j from JobList j WHERE status = 'Scheduled'", JobList.class)
				.getResultList();
	}

	/**
	 * JobLists which are available to be scheduled due to time being greater than specified nextRun time
	 * @return List of schedulable JobLists
	 */
	public List<JobList> schedulable() {
		return getSession().createQuery("SELECT j FROM JobList j "
				+ "WHERE nextRun <= :currentTime "
				+ "AND (status is null OR status != 'Running')", JobList.class)
				.setParameter("currentTime", DateTime.now())
				.getResultList();
	}

	@Override
	protected Fetch[] getProfile(String profile) {
		return new Fetch[] {};
	}
}
