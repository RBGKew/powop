package org.emonocot.job.dwc.reference;

import org.easymock.EasyMock;
import org.emonocot.api.ReferenceService;
import org.emonocot.api.OrganisationService;
import org.emonocot.api.TaxonService;
import org.emonocot.model.Reference;
import org.emonocot.model.Taxon;
import org.emonocot.model.constants.ReferenceType;
import org.emonocot.model.registry.Organisation;
import org.junit.Before;
import org.junit.Test;
import org.springframework.batch.core.JobExecution;
import org.springframework.batch.core.StepExecution;

/**
 *
 * @author ben
 *
 */
public class ReferenceProcessingTest {
    /**
     *
     */
    private Reference reference;

    /**
     *
     */
    private ReferenceService referenceService;
    
    private TaxonService taxonService;

    /**
     *
     */
    private OrganisationService sourceService;

    /**
     *
     */
    private Taxon taxon;

    /**
     *
     */
    private Organisation source = new Organisation();

    /**
     *
     */
    private Processor referenceValidator;

    /**
     *
     */
    @Before
    public final void setUp() {
        reference = new Reference();
        taxon = new Taxon();
        taxon.setId(0L);
        taxon.setIdentifier("identifier");
        taxon.setFamily("Araceae");
        reference.getTaxa().add(taxon);
        reference.setType(ReferenceType.publication);
        reference.setIdentifier("http://build.e-monocot.org/test/test.pdf");
        referenceService = EasyMock.createMock(ReferenceService.class);
        taxonService = EasyMock.createMock(TaxonService.class);
        sourceService = EasyMock.createMock(OrganisationService.class);

        referenceValidator = new Processor();
        referenceValidator.setReferenceService(referenceService);
        referenceValidator.setOrganisationService(sourceService);
        referenceValidator.setTaxonService(taxonService);
        referenceValidator.setSourceName("test source");
        referenceValidator.setFamily("Araceae");
        referenceValidator.beforeStep(new StepExecution("teststep",
                new JobExecution(1L)));
    }
    /**
     * @throws Exception if there is a problem
     */
    @Test
    public final void testProcessReference() throws Exception {
        EasyMock.expect(referenceService.find(EasyMock.isA(String.class)))
                .andReturn(null).anyTimes();
        EasyMock.expect(taxonService.find(EasyMock.eq(0L))).andReturn(taxon).anyTimes();
        EasyMock.expect(sourceService.load(EasyMock.eq("test source")))
                .andReturn(source);
        EasyMock.replay(referenceService, sourceService,taxonService);
        Reference ref = referenceValidator.process(reference);
        EasyMock.verify(referenceService, sourceService,taxonService);        
    }

}
