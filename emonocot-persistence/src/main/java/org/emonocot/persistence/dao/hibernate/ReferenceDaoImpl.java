package org.emonocot.persistence.dao.hibernate;

import org.emonocot.model.hibernate.Fetch;
import org.emonocot.model.reference.Reference;
import org.emonocot.persistence.dao.FacetName;
import org.emonocot.persistence.dao.ReferenceDao;
import org.hibernate.search.query.dsl.FacetContext;
import org.hibernate.search.query.facet.FacetingRequest;
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
    public ReferenceDaoImpl() {
        super(Reference.class);
    }

    @Override
    protected final Fetch[] getProfile(final String profile) {
        return null;
    }

    /**
     * @param facetContext Set the facet context
     * @param facetName Set the facet name
     * @return a faceting request
     */
    @Override
    protected final FacetingRequest createFacetingRequest(
            final FacetContext facetContext, final FacetName facetName) {
        // TODO Auto-generated method stub
        return null;
    }

    /**
     * @return the document fields to search by default
     */
    @Override
    protected final String[] getDocumentFields() {
        return new String[] {"title"};
    }
}
