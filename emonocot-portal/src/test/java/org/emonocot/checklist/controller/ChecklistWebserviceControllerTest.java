package org.emonocot.checklist.controller;

import java.util.ArrayList;
import java.util.Map;

import org.easymock.EasyMock;
import org.emonocot.api.FacetName;
import org.emonocot.api.Sorting;
import org.emonocot.api.TaxonService;
import org.emonocot.model.Taxon;
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

    /**
     *
     */
    private static final String TAXON_IDENTIFIER = "urn:kew.org:wcs:taxon:123";
    /**
     *
     */
    private static final String FAMILY_IDENTIFIER = "urn:kew.org:wcs:family:80";
    /**
     *
     */
    private ChecklistWebserviceController checklistWebserviceController;

    /**
     *
     */
    private TaxonService taxonService;

    /**
     *
     */
    @Before
    public final void setUp() {
        checklistWebserviceController = new ChecklistWebserviceController();
        taxonService = EasyMock.createMock(TaxonService.class);
        checklistWebserviceController.setTaxonDao(taxonService);
    }

    /**
     *
     */
    @Test
    public final void testPing() {
        EasyMock.replay(taxonService);
        ModelAndViewAssert.assertViewName(checklistWebserviceController.ping(),
                "rdfResponse");
        EasyMock.verify(taxonService);
    }

    /**
     *
     */
    @Test
    public final void testSearch() {
        EasyMock.expect(
                taxonService.search(EasyMock.eq("label:Poa annua"),
                        (String) EasyMock.isNull(),
                        (Integer) EasyMock.isNull(),
                        (Integer) EasyMock.isNull(),
                        (FacetName[]) EasyMock.isNull(),
                        (Map<FacetName, String>) EasyMock.isNull(),
                        (Sorting) EasyMock.isNull(),
                        EasyMock.eq("taxon-ws"))).andReturn(
                new DefaultPageImpl<Taxon>(0, 0, 1, new ArrayList<Taxon>()));
        EasyMock.replay(taxonService);
        ModelAndView modelAndView = checklistWebserviceController
                .search("Poa annua");
        ModelAndViewAssert.assertViewName(modelAndView, "rdfResponse");
        ModelAndViewAssert
                .assertModelAttributeAvailable(modelAndView, "result");
        EasyMock.verify(taxonService);
    }

    /**
    *
    */
   @Test
   public final void testSearchForFamily() {
       EasyMock.expect(taxonService.search(EasyMock.eq("label:Poaceae"),
               (String) EasyMock.isNull(),
               (Integer) EasyMock.isNull(),
               (Integer) EasyMock.isNull(),
               (FacetName[]) EasyMock.isNull(),
               (Map<FacetName, String>) EasyMock.isNull(),
               (Sorting) EasyMock.isNull(),
               EasyMock.eq("taxon-ws"))).andReturn(
       new DefaultPageImpl<Taxon>(0, 0, 1, new ArrayList<Taxon>()));
       EasyMock.replay(taxonService);
       ModelAndView modelAndView = checklistWebserviceController
               .search("Poaceae");
       ModelAndViewAssert.assertViewName(modelAndView, "rdfResponse");
       ModelAndViewAssert
               .assertModelAttributeAvailable(modelAndView, "result");      
       EasyMock.verify(taxonService);
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
