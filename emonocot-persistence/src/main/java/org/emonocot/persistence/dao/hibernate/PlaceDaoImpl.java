package org.emonocot.persistence.dao.hibernate;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.emonocot.api.FacetName;
import org.emonocot.model.geography.Place;
import org.emonocot.model.hibernate.Fetch;
import org.emonocot.pager.Page;
import org.emonocot.persistence.dao.PlaceDao;
import org.hibernate.search.ProjectionConstants;
import org.hibernate.search.query.dsl.FacetContext;
import org.hibernate.search.query.engine.spi.FacetManager;
import org.hibernate.search.query.facet.Facet;
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
        return new Fetch[]{};
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
    protected final boolean selectFacet(final FacetName facetName,
            final FacetManager facetManager, final String selectedFacet) {
        switch (facetName) {
        case CLASS:
        	return true;
        default:
            return false;
        }
    }

    @Override
    protected final void addFacet(final Page<Place> page,
            final FacetName facetName, final FacetManager facetManager,
            final Map<FacetName, String> selectedFacets) {
    	switch (facetName) {
    	case CLASS:
    		List<Facet> facets = new ArrayList<Facet>();
            page.addFacets(facetName.name(), facets);
            for (Class clazz : SEARCHABLE_CLASSES) {
                if (clazz.equals(type)) {
                    facets.add(new FakeFacet("CLASS",
                            ProjectionConstants.OBJECT_CLASS, clazz.getName(),
                            page.getSize()));
                }
            }
            break;
        default:
            break;
        }
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
