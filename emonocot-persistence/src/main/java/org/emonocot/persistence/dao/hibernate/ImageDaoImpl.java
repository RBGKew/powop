package org.emonocot.persistence.dao.hibernate;

import org.emonocot.model.hibernate.Fetch;
import org.emonocot.model.media.Image;
import org.emonocot.persistence.dao.FacetName;
import org.emonocot.persistence.dao.ImageDao;
import org.hibernate.search.query.dsl.FacetContext;
import org.hibernate.search.query.facet.FacetingRequest;
import org.springframework.stereotype.Repository;

/**
 *
 * @author ben
 *
 */
@Repository
public class ImageDaoImpl extends DaoImpl<Image> implements ImageDao {

    public ImageDaoImpl() {
        super(Image.class);
    }

    @Override
    protected Fetch[] getProfile(String profile) {
        // TODO Auto-generated method stub
        return null;
    }

    @Override
    protected FacetingRequest createFacetingRequest(FacetContext facetContext,
            FacetName facetName) {
        // TODO Auto-generated method stub
        return null;
    }

    @Override
    protected String[] getDocumentFields() {
        return new String[] {"caption"};
    }
}
