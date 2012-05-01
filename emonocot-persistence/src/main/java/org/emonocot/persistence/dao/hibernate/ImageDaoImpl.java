package org.emonocot.persistence.dao.hibernate;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.emonocot.api.FacetName;
import org.emonocot.model.geography.Continent;
import org.emonocot.model.geography.Region;
import org.emonocot.model.hibernate.Fetch;
import org.emonocot.model.media.Image;
import org.emonocot.model.pager.Page;
import org.emonocot.persistence.dao.ImageDao;
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
 * @author ben
 *
 */
@Repository
public class ImageDaoImpl extends SearchableDaoImpl<Image> implements ImageDao {

   /**
    *
    */
    private static Map<String, Fetch[]> FETCH_PROFILES;

    static {
        FETCH_PROFILES = new HashMap<String, Fetch[]>();
        FETCH_PROFILES.put("image-page", new Fetch[] {
                new Fetch("taxon", FetchMode.JOIN),
                new Fetch("authority", FetchMode.JOIN),
                new Fetch("sources", FetchMode.SELECT)
                });
        FETCH_PROFILES.put("image-taxon", new Fetch[] {
                new Fetch("taxon", FetchMode.JOIN)
                });

    }

    /**
     *
     */
    public ImageDaoImpl() {
        super(Image.class, Image.class);
    }

    /**
     * @param profile
     *            Set the profile name
     * @return an array of related objects to fetch
     */
    @Override
    protected final Fetch[] getProfile(final String profile) {
        return ImageDaoImpl.FETCH_PROFILES.get(profile);
    }

    @Override
    protected final void createFacetingRequest(final FacetContext facetContext,
            final FacetName facetName, final FacetManager facetManager) {
        FacetingRequest facetingRequest = null;

        switch (facetName) {
        case CONTINENT:
            facetingRequest = facetContext.name(facetName.name())
                    .onField("continent").discrete()
                    .orderedBy(FacetSortOrder.FIELD_VALUE)
                    .includeZeroCounts(false).createFacetingRequest();
            facetManager.enableFaceting(facetingRequest);
            break;
        case REGION:
            facetingRequest = facetContext.name(facetName.name())
                    .onField("region").discrete()
                    .orderedBy(FacetSortOrder.FIELD_VALUE)
                    .includeZeroCounts(false).createFacetingRequest();
            facetManager.enableFaceting(facetingRequest);
            break;
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
    protected final void addFacet(final Page<Image> page,
            final FacetName facetName, final FacetManager facetManager,
            final Map<FacetName, String> selectedFacets) {
        switch (facetName) {
        case REGION:
            String selectedContinent = selectedFacets.get(FacetName.CONTINENT);
            if (selectedContinent != null) {
                Continent continent = Continent.valueOf(selectedContinent);
                List<Facet> facets = facetManager.getFacets(facetName.REGION
                        .name());
                List<Facet> filteredFacets = new ArrayList<Facet>();
                for (Facet f : facets) {
                    Region r = Region.valueOf(f.getValue());
                    if (r.getContinent().equals(continent)) {
                        filteredFacets.add(f);
                    }
                }
                page.addFacets(facetName.name(), filteredFacets);
            } else {
                // should not really get here
                page.addFacets(facetName.name(),
                        facetManager.getFacets(facetName.name()));
            }
            break;
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
        case CONTINENT:
        case AUTHORITY:
        case FAMILY:
            page.addFacets(facetName.name(),
                    facetManager.getFacets(facetName.name()));
        default:
            break;
        }
    }

    @Override
    protected final String[] getDocumentFields() {
        return new String[] {"caption", "description", "creator", "keywords", "locality"};
    }

    @Override
    public final String getDefaultField() {
        return "caption";
    }

    /**
     * @param url Set the url
     * @return an image or null if one doesn't exist
     */
    public final Image findByUrl(final String url) {
        Criteria criteria = getSession().createCriteria(type)
        .add(Restrictions.eq("url", url));
        return (Image) criteria.uniqueResult();
    }
}
