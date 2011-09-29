package org.emonocot.persistence.dao.hibernate;

import org.emonocot.model.hibernate.Fetch;
import org.emonocot.model.reference.Reference;
import org.emonocot.persistence.dao.ReferenceDao;
import org.emonocot.service.FacetName;
import org.hibernate.search.query.dsl.FacetContext;
import org.hibernate.search.query.engine.spi.FacetManager;
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

    @Override
    protected final void createFacetingRequest(final FacetContext facetContext,
            final FacetName facetName, final FacetManager facetManager) {
        // TODO Auto-generated method stub
    }

    /**
     * @return the document fields to search by default
     */
    @Override
    protected final String[] getDocumentFields() {
        return new String[] {"title"};
    }
}
