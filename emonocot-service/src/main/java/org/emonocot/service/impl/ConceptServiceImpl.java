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

import org.emonocot.api.ConceptService;
import org.emonocot.model.Concept;
import org.emonocot.persistence.dao.ConceptDao;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

/**
 *
 * @author ben
 *
 */
@Service
public class ConceptServiceImpl extends ServiceImpl<Concept, ConceptDao>
        implements ConceptService {

    /**
     *
     * @param conceptDao Set the image dao
     */
    @Autowired
    public final void setConceptDao(final ConceptDao conceptDao) {
        super.dao = conceptDao;
    }
}
