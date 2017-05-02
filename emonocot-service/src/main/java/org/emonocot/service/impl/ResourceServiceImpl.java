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
import org.emonocot.api.ResourceService;
import org.emonocot.model.registry.Resource;
import org.emonocot.persistence.dao.ResourceDao;
import org.joda.time.DateTime;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
public class ResourceServiceImpl extends SearchableServiceImpl<Resource, ResourceDao> implements ResourceService {

	/**
	 *
	 * @param newJobDao Set the image dao
	 */
	@Autowired
	public final void setJobDao(final ResourceDao newJobDao) {
		super.dao = newJobDao;
	}

	/**
	 * @param sourceId Set the source identifier
	 * @return the total number of jobs for a given source
	 */
	@Transactional(readOnly = true)
	public final Long count(final String sourceId) {
		return dao.count(sourceId);
	}

	/**
	 * @param sourceId
	 *            Set the source identifier
	 * @param page
	 *            Set the offset (in size chunks, 0-based), optional
	 * @param size
	 *            Set the page size
	 * @return A list of jobs
	 */
	@Transactional(readOnly = true)
	public final List<Resource> list(final String sourceId, final Integer page,
			final Integer size) {
		return dao.list(sourceId, page, size);
	}

	/**
	 * @param id Set the job id
	 * @return the job
	 */
	@Transactional(readOnly = true)
	public final Resource findByJobId(final Long id) {
		return dao.findByJobId(id);
	}

	@Transactional(readOnly = true)
	public boolean isHarvesting() {
		return dao.isHarvesting();
	}

	@Transactional(readOnly = true)
	public List<Resource> listResourcesToHarvest(Integer limit, DateTime now, String fetch) {
		return dao.listResourcesToHarvest(limit,now,fetch);
	}

	@Transactional(readOnly = true)
	public Resource findByResourceUri(String identifier) {
		return dao.findResourceByUri(identifier);
	}

	@Override
	@PreAuthorize("hasRole('PERMISSION_ADMINISTRATE')")
	@Transactional(readOnly = false)
	public void deleteById(Long id) {
		super.deleteById(id);
	}

	
	@Override
	@PreAuthorize("hasRole('PERMISSION_ADMINISTRATE')")
	@Transactional(readOnly = false)
	public void delete(String identifier) {
		super.delete(identifier);
	}
}
