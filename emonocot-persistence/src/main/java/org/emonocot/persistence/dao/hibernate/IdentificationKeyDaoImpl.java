package org.emonocot.persistence.dao.hibernate;

import java.util.HashMap;
import java.util.Map;

import org.emonocot.api.FacetName;
import org.emonocot.model.IdentificationKey;
import org.emonocot.model.hibernate.Fetch;
import org.emonocot.persistence.dao.IdentificationKeyDao;
import org.hibernate.FetchMode;
import org.hibernate.criterion.Restrictions;
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

    /**
     *
     * @see org.emonocot.persistence.dao.hibernate.SearchableDaoImpl#
     * createFacetingRequest(org.hibernate.search.query.dsl.FacetContext,
     * org.emonocot.api.FacetName,
     * org.hibernate.search.query.engine.spi.FacetManager)
     */
    @Override
    public final void createFacetingRequest(final FacetContext facetContext,
            final FacetName facetName, final FacetManager facetManager) {

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

    @Override
    public final String[] getDocumentFields() {
        return new String[] {"title", "description", "taxon.name"};
    }

    @Override
    public final String getDefaultField() {
        return "title";
    }

    @Override
    public final Fetch[] getProfile(final String profile) {
        return FETCH_PROFILES.get("object-page");
    }

   /**
    *
    * @param source Set the source of the identification key
    * @return an identification key
    */
    public final IdentificationKey findBySource(final String source) {
        return (IdentificationKey) getSession().createCriteria(type)
                .add(Restrictions.eq("source", source)).uniqueResult();
    }

}
