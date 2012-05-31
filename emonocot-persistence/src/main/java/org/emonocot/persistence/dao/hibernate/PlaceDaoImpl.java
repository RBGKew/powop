package org.emonocot.persistence.dao.hibernate;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.emonocot.api.FacetName;
import org.emonocot.model.geography.Continent;
import org.emonocot.model.geography.Place;
import org.emonocot.model.geography.Region;
import org.emonocot.model.hibernate.Fetch;
import org.emonocot.model.media.Image;
import org.emonocot.model.pager.Page;
import org.emonocot.persistence.dao.ImageDao;
import org.emonocot.persistence.dao.PlaceDao;
import org.hibernate.Criteria;
import org.hibernate.FetchMode;
import org.hibernate.criterion.Restrictions;
import org.hibernate.search.ProjectionConstants;
import org.hibernate.search.query.dsl.FacetContext;
import org.hibernate.search.query.engine.spi.FacetManager;
import org.hibernate.search.query.facet.Facet;
import org.hibernate.search.query.facet.FacetSortOrder;
import org.hibernate.search.query.facet.FacetingRequest;
import org.springframework.stereotype.Repository;

/**
 *
 * @author jk00kg
 *
 */
@Repository
public class PlaceDaoImpl extends SearchableDaoImpl<Place> implements PlaceDao {

   /**
    *
    */
    private static Map<String, Fetch[]> FETCH_PROFILES;

    static {
        FETCH_PROFILES = new HashMap<String, Fetch[]>();
        /* This will only be relevant if there are dynamic associations with 'places'
        FETCH_PROFILES.put("results", new Fetch[] {
                new Fetch("image", FetchMode.SELECT)
                });*/

    }

    /**
     *
     */
    public PlaceDaoImpl() {
        super(Place.class, Place.class);
    }

    /**
     * @param profile
     *            Set the profile name
     * @return an array of related objects to fetch
     */
    @Override
    protected final Fetch[] getProfile(final String profile) {
        return PlaceDaoImpl.FETCH_PROFILES.get(profile);
    }

    @Override
    protected final void createFacetingRequest(final FacetContext facetContext,
            final FacetName facetName, final FacetManager facetManager) {
        FacetingRequest facetingRequest = null;
        //TODO:
    }

    /**
     *
     * @param facetName
     *            Set the facet name
     * @param facetManager
     *            Set the facet manager
     * @param selectedFacet
     *            Set the selected facet
     */
    @Override
    protected final void selectFacet(final FacetName facetName,
            final FacetManager facetManager, final String selectedFacet) {
        switch (facetName) {
        case CONTINENT:
        case AUTHORITY:
        case FAMILY:
            doSelectFacet(facetName, facetManager, selectedFacet);
        default:
            break;
        }
    }

    @Override
    protected final void addFacet(final Page<Place> page,
            final FacetName facetName, final FacetManager facetManager,
            final Map<FacetName, String> selectedFacets) {
        //TODO:
    }

    @Override
    protected final String[] getDocumentFields() {
        return new String[] {"name","fipsCode"};
    }

    @Override
    public final String getDefaultField() {
        return "name";
    }
}
