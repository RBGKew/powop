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

import org.emonocot.api.TaxonService;
import org.emonocot.model.Taxon;
import org.emonocot.pager.Page;
import org.emonocot.persistence.dao.TaxonDao;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

/**
 *
 * @author ben
 *
 */
@Service
public class TaxonServiceImpl extends ServiceImpl<Taxon, TaxonDao>
        implements TaxonService {

    /**
     *
     * @param taxonDao Set the taxon dao
     */
    @Autowired
    public final void setTaxonDao(TaxonDao taxonDao) {
        super.dao = taxonDao;
    }

    /**
     * Returns the child taxa of the taxon with identifier specified.
     * @param identifier Set the identifier
     *
     * @param pageSize
     *            The maximum number of results to return
     * @param pageNumber
     *            The offset (in pageSize chunks, 0-based) from the beginning of
     *            the recordset
     * @param fetch
     *            Set the fetch profile
     *
     * @return a Page from the resultset
     */
    @Transactional(readOnly = true)
    public final List<Taxon> loadChildren(final String identifier,
            final Integer pageSize, final Integer pageNumber, final String fetch) {
        return dao.loadChildren(identifier, pageSize, pageNumber, fetch);
    }

	@Override
	public Page<Taxon> searchByExample(Taxon example, boolean ignoreCase,
			boolean useLike) {
		return dao.searchByExample(example, ignoreCase, useLike);
	}

}
