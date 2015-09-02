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
package org.emonocot.api;

import java.util.List;

import org.springframework.batch.core.JobInstance;

/**
 *
 * @author ben
 *
 */
public interface JobInstanceService {

	/**
	 *
	 * @param id Set the id
	 * @return a job instance
	 */
	JobInstance find(long id);

	/**
	 *
	 * @param id The id to delete
	 */
	void delete(long id);

	/**
	 *
	 * @param jobInstance The jobInstance to save
	 */
	void save(JobInstance jobInstance);

	/**
	 *
	 * @param limit
	 * @param start
	 * @return
	 */
	List<JobInstance> list(Integer limit, Integer start);

}
