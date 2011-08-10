package org.emonocot.checklist.controller;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.fail;

import java.text.ParseException;
import java.util.ArrayList;

import org.easymock.EasyMock;
import org.emonocot.checklist.format.ChecklistIdentifierFormatter;
import org.emonocot.checklist.model.Family;
import org.emonocot.checklist.model.Taxon;
import org.emonocot.checklist.persistence.TaxonDao;
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
    private TaxonDao taxonDao;

    /**
     *
     */
    @Before
    public final void setUp() {
        checklistWebserviceController = new ChecklistWebserviceController();
        taxonDao = EasyMock.createMock(TaxonDao.class);
        checklistWebserviceController.setTaxonDao(taxonDao);
    }

    /**
     *
     */
    @Test
    public final void testPing() {
        EasyMock.replay(taxonDao);
        ModelAndViewAssert.assertViewName(checklistWebserviceController.ping(),
                "rdfResponse");
        EasyMock.verify(taxonDao);
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
        EasyMock.expect(taxonDao.search(EasyMock.eq("Poa annua"))).andReturn(
                new ArrayList<Taxon>());
        EasyMock.replay(taxonDao);
        ModelAndView modelAndView = checklistWebserviceController
                .search("Poa annua");
        ModelAndViewAssert.assertViewName(modelAndView, "rdfResponse");
        ModelAndViewAssert
                .assertModelAttributeAvailable(modelAndView, "result");
        EasyMock.verify(taxonDao);
    }

    /**
    *
    */
   @Test
   public final void testSearchForFamily() {
       EasyMock.expect(taxonDao.countGenera(
               EasyMock.eq(Family.Poaceae))).andReturn(
               new Integer(0));
       EasyMock.replay(taxonDao);
       ModelAndView modelAndView = checklistWebserviceController
               .search("Poaceae");
       ModelAndViewAssert.assertViewName(modelAndView, "rdfFamilyResponse");
       ModelAndViewAssert
               .assertModelAttributeAvailable(modelAndView, "numberOfGenera");
       ModelAndViewAssert
       .assertModelAttributeAvailable(modelAndView, "family");
       EasyMock.verify(taxonDao);
   }

    /**
     *
     */
    @Test
    public final void testGet() {
        EasyMock.expect(taxonDao.get(EasyMock.eq(TAXON_IDENTIFIER)))
                .andReturn(new Taxon());
        EasyMock.replay(taxonDao);
        ModelAndView modelAndView
            = checklistWebserviceController.get(TAXON_IDENTIFIER.longValue());
        ModelAndViewAssert.assertViewName(modelAndView, "tcsXmlResponse");
        ModelAndViewAssert
                .assertModelAttributeAvailable(modelAndView, "result");
        EasyMock.verify(taxonDao);
    }

    /**
    *
    */
   @Test
   public final void testGetFamily() {
       EasyMock.expect(taxonDao.getGenera(EasyMock.eq(Family.Poaceae)))
               .andReturn(new ArrayList<Taxon>());
       EasyMock.replay(taxonDao);
       ModelAndView modelAndView
           = checklistWebserviceController.get(FAMILY_IDENTIFIER.longValue());
       ModelAndViewAssert.assertViewName(modelAndView, "tcsXmlFamilyResponse");
       ModelAndViewAssert
               .assertModelAttributeAvailable(modelAndView, "family");
       ModelAndViewAssert
       .assertModelAttributeAvailable(modelAndView, "children");
       EasyMock.verify(taxonDao);
   }

    /**
     *
     */
    @Test
    public final void testHandleInvalidTaxonIdentifier() {
        EasyMock.replay(taxonDao);
        ModelAndView modelAndView = checklistWebserviceController
                .handleInvalidTaxonIdentifier(new DataRetrievalFailureException(
                        "Invalid identifier 123"));
        ModelAndViewAssert.assertViewName(modelAndView, "exception");
        ModelAndViewAssert.assertModelAttributeAvailable(modelAndView,
                "exception");
        EasyMock.verify(taxonDao);
    }

}
