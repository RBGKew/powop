package org.emonocot.persistence.dao.hibernate;

import java.util.HashMap;
import java.util.Map;

import org.emonocot.api.FacetName;
import org.emonocot.model.common.Annotation;
import org.emonocot.model.hibernate.Fetch;
import org.emonocot.persistence.dao.AnnotationDao;
import org.hibernate.FetchMode;
import org.hibernate.search.query.dsl.FacetContext;
import org.hibernate.search.query.engine.spi.FacetManager;
import org.hibernate.search.query.facet.FacetSortOrder;
import org.hibernate.search.query.facet.FacetingRequest;
import org.springframework.stereotype.Repository;

/**
 *
 * @author ben
 *
 */
@Repository
public class AnnotationDaoImpl extends SearchableDaoImpl<Annotation> implements
        AnnotationDao {

    /**
    *
    */
   private static Map<String, Fetch[]> FETCH_PROFILES;

   static {
       FETCH_PROFILES = new HashMap<String, Fetch[]>();
       FETCH_PROFILES.put("annotated-obj", new Fetch[] {
               new Fetch("annotatedObj", FetchMode.SELECT)});
   }

    /**
     *
     */
    public AnnotationDaoImpl() {
        super(Annotation.class, Annotation.class);
    }

    @Override
    protected final Fetch[] getProfile(final String profile) {
        return AnnotationDaoImpl.FETCH_PROFILES.get(profile);
    }

    @Override
    protected final void createFacetingRequest(final FacetContext facetContext,
            final FacetName facetName, final FacetManager facetManager) {
        FacetingRequest facetingRequest = null;

        switch (facetName) {
        case RECORD_TYPE:
            facetingRequest = facetContext.name(facetName.name())
                    .onField("annotatedObjType").discrete()
                    .orderedBy(FacetSortOrder.FIELD_VALUE)
                    .includeZeroCounts(true).createFacetingRequest();
            facetManager.enableFaceting(facetingRequest);
            break;
        case ISSUE_TYPE:
            facetingRequest = facetContext.name(facetName.name())
                    .onField("type").discrete()
                    .orderedBy(FacetSortOrder.FIELD_VALUE)
                    .includeZeroCounts(true).createFacetingRequest();
            facetManager.enableFaceting(facetingRequest);
            break;
        case ERROR_CODE:
            facetingRequest = facetContext.name(facetName.name())
                    .onField("code").discrete()
                    .orderedBy(FacetSortOrder.FIELD_VALUE)
                    .includeZeroCounts(true).createFacetingRequest();
            facetManager.enableFaceting(facetingRequest);
            break;
        case JOB_INSTANCE:
            facetingRequest = facetContext.name(facetName.name())
                    .onField("jobId").discrete()
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
    * @param selectedFacet Set the selected facet
    */
   @Override
   protected final void selectFacet(final FacetName facetName,
           final FacetManager facetManager,
           final String selectedFacet) {
       switch (facetName) {
       case RECORD_TYPE:
       case ISSUE_TYPE:
       case ERROR_CODE:
       case JOB_INSTANCE:
           doSelectFacet(facetName, facetManager, selectedFacet);
       default:
           break;
       }
   }

    @Override
    public final String[] getDocumentFields() {
        return new String[] {"type", "code", "text"};
    }

    @Override
    public final String getDefaultField() {
        return "text";
    }
}
