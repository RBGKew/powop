package org.emonocot.checklist.controller;

import java.util.ArrayList;

import org.easymock.EasyMock;
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
    public final void testGet() {
        EasyMock.expect(taxonDao.get(EasyMock.eq("123")))
                .andReturn(new Taxon());
        EasyMock.replay(taxonDao);
        ModelAndView modelAndView = checklistWebserviceController.get("123");
        ModelAndViewAssert.assertViewName(modelAndView, "tcsXmlResponse");
        ModelAndViewAssert
                .assertModelAttributeAvailable(modelAndView, "result");
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
