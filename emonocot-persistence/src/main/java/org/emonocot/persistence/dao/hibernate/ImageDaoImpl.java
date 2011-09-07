package org.emonocot.persistence.dao.hibernate;

import org.emonocot.model.hibernate.Fetch;
import org.emonocot.model.media.Image;
import org.emonocot.persistence.dao.FacetName;
import org.emonocot.persistence.dao.ImageDao;
import org.hibernate.search.ProjectionConstants;
import org.hibernate.search.query.dsl.FacetContext;
import org.hibernate.search.query.facet.FacetSortOrder;
import org.hibernate.search.query.facet.FacetingRequest;
import org.springframework.stereotype.Repository;

/**
 *
 * @author ben
 *
 */
@Repository
public class ImageDaoImpl extends DaoImpl<Image> implements ImageDao {

    /**
     *
     */
    public ImageDaoImpl() {
        super(Image.class);
    }

    /**
     * @param profile Set the profile name
     * @return an array of related objects to fetch
     */
    @Override
    protected final Fetch[] getProfile(final String profile) {
        // TODO Auto-generated method stub
        return null;
    }

    @Override
    protected final FacetingRequest createFacetingRequest(
            final FacetContext facetContext, final FacetName facetName) {
        FacetingRequest facetingRequest = null;

        switch (facetName) {
        case CLASS:
            facetingRequest = facetContext.name(facetName.name())
                    .onField(ProjectionConstants.OBJECT_CLASS).discrete()
                    .orderedBy(FacetSortOrder.FIELD_VALUE)
                    .includeZeroCounts(true).createFacetingRequest();
            break;
        case CONTINENT:
            facetingRequest = facetContext.name(facetName.name())
                    .onField("continent").discrete()
                    .orderedBy(FacetSortOrder.FIELD_VALUE)
                    .includeZeroCounts(true).createFacetingRequest();
            break;
        case AUTHORITY:
            facetingRequest = facetContext.name(facetName.name())
                    .onField("authorities.name").discrete()
                    .orderedBy(FacetSortOrder.FIELD_VALUE)
                    .includeZeroCounts(true).createFacetingRequest();
            break;
        case FAMILY:
            facetingRequest = facetContext.name(facetName.name())
                    .onField("family").discrete()
                    .orderedBy(FacetSortOrder.FIELD_VALUE)
                    .includeZeroCounts(true).createFacetingRequest();
            break;
        default:
            break;
        }

        return facetingRequest;
    }

    @Override
    protected final String[] getDocumentFields() {
        return new String[] {"caption"};
    }
}
