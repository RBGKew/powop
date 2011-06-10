package org.emonocot.persistence.dao.hibernate;

import org.emonocot.model.description.Content;
import org.emonocot.model.description.Feature;
import org.emonocot.model.description.TextContent;
import org.emonocot.model.hibernate.Fetch;
import org.emonocot.model.taxon.Taxon;
import org.emonocot.persistence.dao.DescriptionDao;
import org.emonocot.persistence.dao.FacetName;
import org.hibernate.search.query.dsl.FacetContext;
import org.hibernate.search.query.facet.FacetingRequest;
import org.springframework.stereotype.Repository;

/**
 *
 * @author ben
 *
 */
@Repository
public class DescriptionDaoImpl extends DaoImpl<Content> implements
        DescriptionDao {

    /**
     *
     */
    public DescriptionDaoImpl() {
        super(Content.class);
    }

    @Override
    public final TextContent getTextContent(
            final Feature feature, final Taxon taxon) {
        // TODO Auto-generated method stub
        return null;
    }

    @Override
    protected final Fetch[] getProfile(final String profile) {
        // TODO Auto-generated method stub
        return null;
    }

    @Override
    protected final FacetingRequest createFacetingRequest(
            final FacetContext facetContext, final FacetName facetName) {
        // TODO Auto-generated method stub
        return null;
    }

}
