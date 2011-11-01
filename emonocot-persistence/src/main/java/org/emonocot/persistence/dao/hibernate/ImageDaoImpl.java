package org.emonocot.persistence.dao.hibernate;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.emonocot.api.FacetName;
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
        FETCH_PROFILES.put("image-page", new Fetch[] { new Fetch("taxon",
                FetchMode.JOIN)
        // new Fetch("taxa", FetchMode.SELECT)
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
                    .includeZeroCounts(true).createFacetingRequest();
            facetManager.enableFaceting(facetingRequest);
            break;
        case AUTHORITY:
            facetingRequest = facetContext.name(facetName.name())
                    .onField("sources.identifier").discrete()
                    .orderedBy(FacetSortOrder.FIELD_VALUE)
                    .includeZeroCounts(true).createFacetingRequest();
            facetManager.enableFaceting(facetingRequest);
            break;
        case FAMILY:
            facetingRequest = facetContext.name(facetName.name())
                    .onField("family").discrete()
                    .orderedBy(FacetSortOrder.FIELD_VALUE)
                    .includeZeroCounts(true).createFacetingRequest();
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
     * @param selectedFacetIndex
     *            Set the selected facet
     */
    @Override
    protected final void selectFacet(final FacetName facetName,
            final FacetManager facetManager, final Integer selectedFacetIndex) {
        switch (facetName) {
        case CONTINENT:
        case AUTHORITY:
        case FAMILY:
            List<Facet> facetResults = facetManager.getFacets(facetName.name());
            Facet selectedFacet = facetResults.get(selectedFacetIndex);
            facetManager.getFacetGroup(facetName.name()).selectFacets(
                    selectedFacet);
        default:
            break;
        }
    }

    @Override
    protected final void addFacet(final Page<Image> page,
            final FacetName facetName, final FacetManager facetManager) {
        switch (facetName) {
        case CLASS:
            List<Facet> facets = new ArrayList<Facet>();
            page.addFacets(facetName.name(), facets);
            for (Class clazz : SEARCHABLE_CLASSES) {
                if (clazz.equals(type)) {
                    facets.add(new FakeFacet("CLASS",
                            ProjectionConstants.OBJECT_CLASS, clazz.getName(),
                            page.getSize()));
                } else {
                    facets.add(new FakeFacet("CLASS",
                            ProjectionConstants.OBJECT_CLASS, clazz.getName(),
                            0));
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
        return new String[] { "caption" };
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
