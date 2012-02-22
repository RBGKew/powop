package org.emonocot.checklist.controller;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.fail;

import java.text.ParseException;
import java.util.ArrayList;
import java.util.Map;

import org.easymock.EasyMock;
import org.emonocot.api.FacetName;
import org.emonocot.api.Sorting;
import org.emonocot.api.TaxonService;
import org.emonocot.checklist.format.ChecklistIdentifierFormatter;
import org.emonocot.model.pager.DefaultPageImpl;
import org.emonocot.model.taxon.Family;
import org.emonocot.model.taxon.Taxon;
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
    private static final Integer TAXON_IDENTIFIER = 123;
    /**
     *
     */
    private static final Integer FAMILY_IDENTIFIER = -80;
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
    public final void testParseIdentifier() {
        ChecklistIdentifierFormatter formatter
            = new ChecklistIdentifierFormatter();
        try {
          assertEquals(new Long(1),
                  formatter.parse("urn:kew.org:wcs:taxon:1", null));
        } catch (ParseException pe) {
            fail();
        }
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
       EasyMock.expect(taxonService.countGenera(
               EasyMock.eq(Family.Poaceae))).andReturn(
               new Integer(0));
       EasyMock.replay(taxonService);
       ModelAndView modelAndView = checklistWebserviceController
               .search("Poaceae");
       ModelAndViewAssert.assertViewName(modelAndView, "rdfFamilyResponse");
       ModelAndViewAssert
               .assertModelAttributeAvailable(modelAndView, "numberOfGenera");
       ModelAndViewAssert
       .assertModelAttributeAvailable(modelAndView, "family");
       EasyMock.verify(taxonService);
   }

    /**
     *
     */
    @Test
    public final void testGet() {
        EasyMock.expect(
                taxonService.load(EasyMock.eq("urn:kew.org:wcs:taxon:123"),
                        EasyMock.eq("taxon-ws"))).andReturn(
                new Taxon());
        EasyMock.replay(taxonService);
        ModelAndView modelAndView
            = checklistWebserviceController.get(TAXON_IDENTIFIER.longValue());
        ModelAndViewAssert.assertViewName(modelAndView, "tcsXmlResponse");
        ModelAndViewAssert
                .assertModelAttributeAvailable(modelAndView, "result");
        EasyMock.verify(taxonService);
    }

    /**
    *
    */
   @Test
   public final void testGetFamily() {
       EasyMock.expect(taxonService.getGenera(EasyMock.eq(Family.Poaceae)))
               .andReturn(new ArrayList<Taxon>());
       EasyMock.replay(taxonService);
       ModelAndView modelAndView
           = checklistWebserviceController.get(FAMILY_IDENTIFIER.longValue());
       ModelAndViewAssert.assertViewName(modelAndView, "tcsXmlFamilyResponse");
       ModelAndViewAssert
               .assertModelAttributeAvailable(modelAndView, "family");
       ModelAndViewAssert
       .assertModelAttributeAvailable(modelAndView, "children");
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
