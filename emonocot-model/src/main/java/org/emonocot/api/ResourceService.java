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

import org.emonocot.api.job.CouldNotLaunchJobException;
import org.emonocot.api.job.ResourceAlreadyBeingHarvestedException;
import org.emonocot.model.registry.Resource;
import org.joda.time.DateTime;

/**
 *
 * @author ben
 *
 */
public interface ResourceService extends SearchableService<Resource> {
	/**
	 * @param sourceId Set the source identifier
	 * @return the total number of jobs for a given source
	 */
	Long count(String sourceId);

	/**
	 * @param sourceId Set the source identifier
	 * @param page Set the offset (in size chunks, 0-based), optional
	 * @param size Set the page size
	 * @return A list of jobs
	 */
	List<Resource> list(String sourceId, Integer page, Integer size);

	/**
	 *
	 * @param id Set the job id
	 * @return a matching job
	 */
	Resource findByJobId(Long id);

	boolean isHarvesting();

	List<Resource> listResourcesToHarvest(Integer limit, DateTime now, String fetch);

	void harvestResource(Long resourceId, Boolean ifModified) throws ResourceAlreadyBeingHarvestedException, CouldNotLaunchJobException;

	Resource findByResourceUri(String identifier) throws ResourceAlreadyBeingHarvestedException, CouldNotLaunchJobException;
}
