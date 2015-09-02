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
package org.emonocot.persistence.dao;

import java.util.List;

import org.springframework.batch.core.JobExecution;

/**
 *
 * @author ben
 *
 */
public interface JobExecutionDao {

	/**
	 *
	 * @param authorityName the name of the authorty
	 * @param pageSize set the maximum size of the list of executions
	 * @param pageNumber Set the page number
	 * @return a list of job executions
	 */
	List<JobExecution> getJobExecutions(String authorityName, Integer pageSize,
			Integer pageNumber);

	/**
	 *
	 * @param identifier the identifier of the job
	 * @return a job execution
	 */
	JobExecution load(Long identifier);

	/**
	 *
	 * @param id The id to delete
	 */
	void delete(Long id);

	/**
	 *
	 * @param jobExecution The jobExecution to save
	 */
	void save(JobExecution jobExecution);

}
