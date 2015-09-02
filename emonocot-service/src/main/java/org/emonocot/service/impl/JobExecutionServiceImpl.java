/*
 * This is eMonocot, a global online biodiversity information resource.
 *
 * Copyright © 2011–2015 The Board of Trustees of the Royal Botanic Gardens, Kew and The University of Oxford
 *
 * eMonocot is free software: you can redistribute it and/or modify it under the terms of the
 * GNU Affero General Public License as published by the Free Software Foundation, either version 3
 * of the License, or (at your option) any later version.
 *
 * eMonocot is distributed in the hope that it will be useful, but WITHOUT ANY WARRANTY; without even
 * the implied warranty of MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 * GNU Affero General Public License for more details.
 *
 * The complete text of the GNU Affero General Public License is in the source repository as the file
 * ‘COPYING’.  It is also available from <http://www.gnu.org/licenses/>.
 */
package org.emonocot.service.impl;

import java.util.List;

import org.emonocot.api.JobExecutionService;
import org.emonocot.persistence.dao.JobExecutionDao;
import org.springframework.batch.core.JobExecution;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

/**
 *
 * @author ben
 *
 */
@Service
public class JobExecutionServiceImpl implements JobExecutionService {

	/**
	 *
	 */
	private JobExecutionDao dao;

	/**
	 *
	 * @param jobExecutionDao
	 *            set the job dao
	 */
	@Autowired
	public void setJobExecutionDao(JobExecutionDao jobExecutionDao) {
		this.dao = jobExecutionDao;
	}

	/**
	 * @param identifier
	 *            Set the identifier
	 * @param pageSize
	 *            Set the page size
	 * @param pageNumber
	 *            Set the page number
	 * @return a list of job executions
	 */
	@Transactional(readOnly = true)
	public List<JobExecution> listJobExecutions(final String identifier,
			final Integer pageSize, final Integer pageNumber) {
		return dao.getJobExecutions(identifier, pageSize, pageNumber);
	}

	/**
	 *
	 * @param identifier
	 *            the identifier of the job
	 * @return a job execution
	 */
	@Transactional(readOnly = true)
	public JobExecution find(final long identifier) {
		return dao.load(identifier);
	}

	/**
	 *
	 * @param id The id to delete
	 */
	@Transactional
	public void delete(final long id) {
		dao.delete(id);
	}

	/**
	 *
	 * @param jobExecution The jobExecution to save
	 */
	@Transactional
	public void save(JobExecution jobExecution) {
		dao.save(jobExecution);
	}
}
