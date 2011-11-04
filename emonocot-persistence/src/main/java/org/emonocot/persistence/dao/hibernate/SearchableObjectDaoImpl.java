/**
 *
 */
package org.emonocot.persistence.dao.hibernate;

import java.util.List;

import org.emonocot.api.FacetName;
import org.emonocot.model.common.SearchableObject;
import org.emonocot.model.hibernate.Fetch;
import org.emonocot.model.media.Image;
import org.emonocot.model.pager.Page;
import org.emonocot.model.source.Source;
import org.emonocot.model.taxon.Taxon;
import org.emonocot.persistence.dao.SearchableObjectDao;
import org.hibernate.search.ProjectionConstants;
import org.hibernate.search.query.dsl.FacetContext;
import org.hibernate.search.query.engine.spi.FacetManager;
import org.hibernate.search.query.facet.Facet;
import org.hibernate.search.query.facet.FacetSortOrder;
import org.hibernate.search.query.facet.FacetingRequest;
import org.springframework.stereotype.Repository;

/**
 * @author jk00kg
 *
 */
@Repository
public class SearchableObjectDaoImpl extends SearchableDaoImpl<SearchableObject> implements
        SearchableObjectDao {

    /**
     *
     */
    public SearchableObjectDaoImpl() {
        super(SearchableObject.class, Taxon.class, Image.class);
    }

    @Override
    protected final Fetch[] getProfile(final String profile) {
        // TODO Auto-generated method stub
        return null;
    }

    @Override
    protected final void createFacetingRequest(final FacetContext facetContext,
            final FacetName facetName, final FacetManager facetManager) {

        FacetingRequest facetingRequest = null;

        switch (facetName) {
        case CLASS:
            facetingRequest = facetContext.name(facetName.name())
                    .onField(ProjectionConstants.OBJECT_CLASS).discrete()
                    .orderedBy(FacetSortOrder.FIELD_VALUE)
                    .includeZeroCounts(true).createFacetingRequest();
            facetManager.enableFaceting(facetingRequest);
            break;
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
    * @param facetName Set the facet name
    * @param facetManager Set the facet manager
    * @param selectedFacetIndex Set the selected facet
    */
   @Override
   protected final void selectFacet(final FacetName facetName,
           final FacetManager facetManager,
           final Integer selectedFacetIndex) {
       switch (facetName) {
       case CLASS:
       case CONTINENT:
       case AUTHORITY:
       case FAMILY:
           List<Facet> facetResults =
               facetManager.getFacets(facetName.name());
           Facet selectedFacet = facetResults.get(selectedFacetIndex);
           facetManager.getFacetGroup(facetName.name())
                   .selectFacets(selectedFacet);
       default:
           break;
       }
   }

    @Override
    protected final void addFacet(final Page<SearchableObject> page,
            final FacetName facetName, final FacetManager facetManager) {
       switch (facetName) {
       case CLASS:
       case CONTINENT:
       case AUTHORITY:
       case FAMILY:
           page.addFacets(facetName.name(),
                   facetManager.getFacets(facetName.name()));
       default:
           break;
       }
   }

    /**
     * @return the fields to search
     */
    @Override
    protected final String[] getDocumentFields() {
        return new String[]{"name", "caption", "title", "content.content"};
    }

    /**
     *
     */
    @Override
    protected Class getAnalyzerType() {
        return Taxon.class;
    }
}
