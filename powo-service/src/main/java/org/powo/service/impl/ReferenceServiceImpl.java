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
package org.powo.service.impl;

import org.powo.api.ReferenceService;
import org.powo.model.Reference;
import org.powo.persistence.dao.ReferenceDao;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

/**
 *
 * @author ben
 *
 */
@Service
public class ReferenceServiceImpl extends ServiceImpl<Reference, ReferenceDao>
implements ReferenceService {

	/**
	 *
	 * @param referenceDao Set the reference dao
	 */
	@Autowired
	public final void setReferenceDao(final ReferenceDao referenceDao) {
		super.dao = referenceDao;
	}

	/**
	 * @param source The source of the reference you want to find
	 * @return a reference or null if it does not exist
	 */
	@Transactional(readOnly = true)
	public final Reference findByBibliographicCitation(final String source) {
		return dao.findByBibliographicCitation(source);
	}

}
