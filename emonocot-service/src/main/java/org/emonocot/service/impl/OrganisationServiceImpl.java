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

import org.emonocot.api.OrganisationService;
import org.emonocot.model.registry.Organisation;
import org.emonocot.persistence.dao.OrganisationDao;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.access.prepost.PostAuthorize;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

/**
 *
 * @author ben
 *
 */
@Service
public class OrganisationServiceImpl extends SearchableServiceImpl<Organisation, OrganisationDao> implements
        OrganisationService {

    /**
     *
     * @param sourceDao Set the source dao
     */
    @Autowired
    public final void setSourceDao(OrganisationDao sourceDao) {
        super.dao = sourceDao;
    }

    /**
     *
     * @param identifier the identifier of the object
     * @param fetch the fetch profile to use
     * @return the object
     */
    @PostAuthorize("hasPermission(returnObject, 'READ') or hasRole('PERMISSION_ADMINISTRATE')")
    @Transactional(readOnly = true)
    public final Organisation load(final String identifier, final String fetch) {
        return dao.load(identifier, fetch);
    }

    /**
     *
     * @param identifier the identifier of the object
     * @return the object loaded using the default fetch profile
     *
     */
    @PostAuthorize("hasPermission(returnObject, 'READ') or hasRole('PERMISSION_ADMINISTRATE')")
    @Transactional(readOnly = true)
    public final Organisation load(final String identifier) {
        return dao.load(identifier);
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
