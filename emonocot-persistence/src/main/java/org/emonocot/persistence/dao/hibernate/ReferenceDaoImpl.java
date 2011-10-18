package org.emonocot.persistence.dao.hibernate;

import org.emonocot.api.FacetName;
import org.emonocot.model.hibernate.Fetch;
import org.emonocot.model.reference.Reference;
import org.emonocot.persistence.dao.ReferenceDao;
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
}
