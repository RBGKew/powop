/**
 * 
 */
package org.emonocot.persistence.dao.hibernate;

import java.util.HashMap;
import java.util.Map;

import org.emonocot.api.FacetName;
import org.emonocot.model.hibernate.Fetch;
import org.emonocot.model.key.IdentificationKey;
import org.emonocot.persistence.dao.IdentificationKeyDao;
import org.hibernate.FetchMode;
import org.hibernate.search.query.dsl.FacetContext;
import org.hibernate.search.query.engine.spi.FacetManager;
import org.hibernate.search.query.facet.FacetSortOrder;
import org.hibernate.search.query.facet.FacetingRequest;
import org.springframework.stereotype.Repository;

/**
 * @author jk00kg
 */
@Repository
public class IdentificationKeyDaoImpl extends
        SearchableDaoImpl<IdentificationKey> implements IdentificationKeyDao {

    /**
     * 
     */
    private static Map<String, Fetch[]> FETCH_PROFILES;

    static {
        FETCH_PROFILES = new HashMap<String, Fetch[]>();
        FETCH_PROFILES.put("object-page", new Fetch[] {
                new Fetch("taxon", FetchMode.SELECT),
                new Fetch("authority", FetchMode.SELECT),
                new Fetch("sources", FetchMode.SELECT)});
        FETCH_PROFILES.put("front-cover", new Fetch[] {
                new Fetch("taxon", FetchMode.SELECT)});
    }

    /**
     * 
     */
    public IdentificationKeyDaoImpl() {
        super(IdentificationKey.class, IdentificationKey.class);
    }

    /* (non-Javadoc)
     * @see org.emonocot.persistence.dao.hibernate.SearchableDaoImpl#createFacetingRequest(org.hibernate.search.query.dsl.FacetContext, org.emonocot.api.FacetName, org.hibernate.search.query.engine.spi.FacetManager)
     */
    @Override
    protected void createFacetingRequest(FacetContext facetContext,
            FacetName facetName, FacetManager facetManager) {

        FacetingRequest facetingRequest = null;
        switch (facetName) {
        case AUTHORITY:
            facetingRequest = facetContext.name(facetName.name())
                    .onField("sources.identifier").discrete()
                    .orderedBy(FacetSortOrder.FIELD_VALUE)
                    .includeZeroCounts(false).createFacetingRequest();
            facetManager.enableFaceting(facetingRequest);
            break;
        case FAMILY:
            facetingRequest = facetContext.name(facetName.name())
                    .onField("family").discrete()
                    .orderedBy(FacetSortOrder.FIELD_VALUE)
                    .includeZeroCounts(false).createFacetingRequest();
            facetManager.enableFaceting(facetingRequest);
            break;
        default:
            break;
        }
    }

    /* (non-Javadoc)
     * @see org.emonocot.persistence.dao.hibernate.SearchableDaoImpl#getDocumentFields()
     */
    @Override
    protected String[] getDocumentFields() {
        return new String[] {"title", "description", "taxon.name"};
    }

    /* (non-Javadoc)
     * @see org.emonocot.persistence.dao.hibernate.SearchableDaoImpl#getDefaultField()
     */
    @Override
    public String getDefaultField() {
        return "title";
    }

    /* (non-Javadoc)
     * @see org.emonocot.persistence.dao.hibernate.DaoImpl#getProfile(java.lang.String)
     */
    @Override
    protected Fetch[] getProfile(String profile) {
        return FETCH_PROFILES.get("object-page");
    }

}
