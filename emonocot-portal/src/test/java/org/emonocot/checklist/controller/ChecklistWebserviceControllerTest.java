package org.emonocot.checklist.controller;

import java.util.ArrayList;
import java.util.Map;

import org.easymock.EasyMock;
import org.emonocot.api.SearchableObjectService;
import org.emonocot.api.TaxonService;
import org.emonocot.model.SearchableObject;
import org.emonocot.pager.DefaultPageImpl;
import org.junit.Before;
import org.junit.Test;
import org.springframework.dao.DataRetrievalFailureException;
import org.springframework.test.web.ModelAndViewAssert;
import org.springframework.web.servlet.ModelAndView;

/**
 *
 * @author ben
 *
 */
public class ChecklistWebserviceControllerTest {

    private static final String TAXON_IDENTIFIER = "urn:kew.org:wcs:taxon:123";

    private static final String FAMILY_IDENTIFIER = "urn:kew.org:wcs:family:80";
    
    private ChecklistWebserviceController checklistWebserviceController;

    private TaxonService taxonService;
    
    private SearchableObjectService searchableObjectService;

    @Before
    public final void setUp() {
        checklistWebserviceController = new ChecklistWebserviceController();
        taxonService = EasyMock.createMock(TaxonService.class);
        searchableObjectService = EasyMock.createMock(SearchableObjectService.class);
        checklistWebserviceController.setTaxonDao(taxonService);
        checklistWebserviceController.setSearchableObjectService(searchableObjectService);
    }

    @Test
    public final void testPing() {
        EasyMock.replay(taxonService);
        ModelAndViewAssert.assertViewName(checklistWebserviceController.ping(),
                "rdfResponse");
        EasyMock.verify(taxonService);
    }

    @Test
    public final void testSearch() {
        EasyMock.expect(
                searchableObjectService.search(EasyMock.eq("searchable.label_sort:Poa annua"),
                        (String) EasyMock.isNull(),
                        (Integer) EasyMock.isNull(),
                        (Integer) EasyMock.isNull(),
                        (String[]) EasyMock.isNull(),
                        (Map<String, String>) EasyMock.isA(Map.class),
                        (String) EasyMock.isNull(),
                        (String) EasyMock.isNull())).andReturn(
                new DefaultPageImpl<SearchableObject>(0, 0, 1, new ArrayList<SearchableObject>(),null));
        EasyMock.replay(taxonService,searchableObjectService);
        ModelAndView modelAndView = checklistWebserviceController
                .search("Poa annua");
        ModelAndViewAssert.assertViewName(modelAndView, "rdfResponse");
        ModelAndViewAssert
                .assertModelAttributeAvailable(modelAndView, "result");
        EasyMock.verify(taxonService,searchableObjectService);
    }

   @Test
   public final void testSearchForFamily() {
       EasyMock.expect(searchableObjectService.search(EasyMock.eq("searchable.label_sort:Poaceae"),
               (String) EasyMock.isNull(),
               (Integer) EasyMock.isNull(),
               (Integer) EasyMock.isNull(),
               (String[]) EasyMock.isNull(),
               (Map<String, String>) EasyMock.isA(Map.class),
               (String) EasyMock.isNull(),
               (String) EasyMock.isNull())).andReturn(
       new DefaultPageImpl<SearchableObject>(0, 0, 1, new ArrayList<SearchableObject>(),null));
       EasyMock.replay(taxonService,searchableObjectService);
       ModelAndView modelAndView = checklistWebserviceController
               .search("Poaceae");
       ModelAndViewAssert.assertViewName(modelAndView, "rdfResponse");
       ModelAndViewAssert
               .assertModelAttributeAvailable(modelAndView, "result");      
       EasyMock.verify(taxonService,searchableObjectService);
   }

    /**
     *
     */
    @Test
    public final void testHandleInvalidTaxonIdentifier() {
        EasyMock.replay(taxonService);
        ModelAndView modelAndView = checklistWebserviceController
                .handleInvalidTaxonIdentifier(new DataRetrievalFailureException(
                        "Invalid identifier 123"));
        ModelAndViewAssert.assertViewName(modelAndView, "exception");
        ModelAndViewAssert.assertModelAttributeAvailable(modelAndView,
                "exception");
        EasyMock.verify(taxonService);
    }

}
