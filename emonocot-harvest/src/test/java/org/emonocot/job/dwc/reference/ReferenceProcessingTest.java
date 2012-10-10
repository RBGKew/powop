package org.emonocot.job.dwc.reference;

import org.easymock.EasyMock;
import org.emonocot.api.ReferenceService;
import org.emonocot.api.SourceService;
import org.emonocot.model.Reference;
import org.emonocot.model.Source;
import org.emonocot.model.Taxon;
import org.emonocot.model.constants.ReferenceType;
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

    /**
     *
     */
    private SourceService sourceService;

    /**
     *
     */
    private Taxon taxon;

    /**
     *
     */
    private Source source = new Source();

    /**
     *
     */
    private Validator referenceValidator;

    /**
     *
     */
    @Before
    public final void setUp() {
        reference = new Reference();
        taxon = new Taxon();
        taxon.setFamily("Araceae");
        reference.getTaxa().add(taxon);
        reference.setType(ReferenceType.publication);
        reference.setIdentifier("http://build.e-monocot.org/test/test.pdf");
        referenceService = EasyMock.createMock(ReferenceService.class);
        sourceService = EasyMock.createMock(SourceService.class);

        referenceValidator = new Validator();
        referenceValidator.setReferenceService(referenceService);
        referenceValidator.setSourceService(sourceService);
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
        EasyMock.expect(sourceService.load(EasyMock.eq("test source")))
                .andReturn(source);
        EasyMock.replay(referenceService, sourceService);
        Reference ref = referenceValidator.process(reference);
        EasyMock.verify(referenceService, sourceService);        
    }

}
