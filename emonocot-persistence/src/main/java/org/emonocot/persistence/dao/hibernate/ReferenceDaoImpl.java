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
package org.emonocot.persistence.dao.hibernate;

import java.util.HashMap;
import java.util.Map;

import org.emonocot.model.Reference;
import org.emonocot.model.hibernate.Fetch;
import org.emonocot.persistence.dao.ReferenceDao;
import org.hibernate.Criteria;
import org.hibernate.FetchMode;
import org.hibernate.criterion.Restrictions;
import org.springframework.stereotype.Repository;

/**
 *
 * @author ben
 *
 */
@Repository
public class ReferenceDaoImpl extends DaoImpl<Reference> implements
        ReferenceDao {
   /**
    *
    */
    private static Map<String, Fetch[]> FETCH_PROFILES;

    static {
        FETCH_PROFILES = new HashMap<String, Fetch[]>();
        FETCH_PROFILES.put("reference-with-taxa", new Fetch[] {
                new Fetch("taxa", FetchMode.SELECT)
                });
    }

    /**
     *
     */
    public ReferenceDaoImpl() {
        super(Reference.class);
    }

    /**
     * @param profile
     *            Set the profile name
     * @return an array of related objects to fetch
     */
    @Override
    protected final Fetch[] getProfile(final String profile) {
        return ReferenceDaoImpl.FETCH_PROFILES.get(profile);
    }

    /**
     * @param source The source of the reference you want to find
     * @return a reference or null if it does not exist
     */
    public final Reference findByBibliographicCitation(final String bibliographicCitation) {
        Criteria criteria = getSession().createCriteria(type).add(Restrictions.eq("bibliographicCitation", bibliographicCitation));
        return (Reference) criteria.uniqueResult();
    }
}
