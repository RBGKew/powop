package org.emonocot.persistence.dao.hibernate;

import java.util.HashMap;
import java.util.Map;

import org.emonocot.model.hibernate.Fetch;
import org.emonocot.model.taxon.Taxon;
import org.emonocot.persistence.dao.FacetName;
import org.emonocot.persistence.dao.TaxonDao;
import org.hibernate.FetchMode;
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
public class TaxonDaoImpl extends DaoImpl<Taxon> implements TaxonDao {

    /**
     *
     */
    private static Map<String, Fetch[]> FETCH_PROFILES;

    static {
        FETCH_PROFILES = new HashMap<String, Fetch[]>();
        FETCH_PROFILES.put("taxon-with-related", new Fetch[] {
                new Fetch("parent", FetchMode.JOIN),
                new Fetch("accepted", FetchMode.JOIN),
                new Fetch("children", FetchMode.SELECT),
                new Fetch("synonyms", FetchMode.SELECT),
                new Fetch("annotations", FetchMode.SELECT)
        });
        FETCH_PROFILES.put("taxon-page", new Fetch[] {
                new Fetch("parent", FetchMode.JOIN),
                new Fetch("accepted", FetchMode.JOIN),
                new Fetch("children", FetchMode.SELECT),
                new Fetch("synonyms", FetchMode.SELECT),
                new Fetch("distribution", FetchMode.SELECT)
        });
    }

    /**
     *
     * @return the fields to search by default
     */
    protected final String[] getDocumentFields() {
        return new String[] {"title", "name"};
    }

    /**
     *
     */
    public TaxonDaoImpl() {
        super(Taxon.class);
    }

    /**
     *
     * @param facetContext The faceting context of this request
     * @param facetName The name of the facet required
     * @return the faceting context
     */
    protected final FacetingRequest createFacetingRequest(
            final FacetContext facetContext, final FacetName facetName) {

        FacetingRequest facetingRequest = null;

        switch (facetName) {
        case CONTINENT:
            facetingRequest = facetContext.name(facetName.name())
                    .onField("continent").discrete()
                    .orderedBy(FacetSortOrder.FIELD_VALUE)
                    .includeZeroCounts(true)
                    .createFacetingRequest();
            break;
        case FAMILY:
            facetingRequest = facetContext.name(facetName.name())
                    .onField("family").discrete()
                    .orderedBy(FacetSortOrder.FIELD_VALUE)
                    .includeZeroCounts(true)
                    .createFacetingRequest();
            break;
        default:
            break;
        }

        return facetingRequest;
    }

    public final boolean verify(final String identifer,
            final String scientificName) {
        // TODO Auto-generated method stub
        return false;
    }

    @Override
    protected final Fetch[] getProfile(final String profile) {
        return TaxonDaoImpl.FETCH_PROFILES.get(profile);
    }
}
