package org.emonocot.job.checklist;

import static org.junit.Assert.assertNull;

import java.net.URI;
import java.net.URISyntaxException;

import org.easymock.EasyMock;
import org.emonocot.api.TaxonService;
import org.emonocot.model.taxon.Taxon;
import org.joda.time.DateTime;
import org.junit.Before;
import org.junit.Test;
import org.openarchives.pmh.Header;
import org.openarchives.pmh.Record;
import org.openarchives.pmh.Status;

/**
 *
 * @author ben
 *
 */
public class OaiPmhRecordProcessorTest {

    /**
     *
     */
    private OaiPmhRecordProcessorImpl processor;

    /**
     *
     */
    private Record record;

    /**
     *
     */
    private TaxonService taxonService;

    /**
     * @throws Exception if something goes wrong
     */
    @Before
    public final void setUp() throws Exception {
        processor = new OaiPmhRecordProcessorImpl();
        record = new Record();
        Header header = new Header();
        header.setIdentifier(new URI("urn:lsid:example.com:test:123"));
        header.setStatus(Status.deleted);
        header.setDatestamp(new DateTime());
        record.setHeader(header);
        taxonService = EasyMock.createMock(TaxonService.class);
        processor.setTaxonService(taxonService);
    }

    /**
     * @throws Exception if something goes wrong
     */
    @Test
    public final void testSkipDeletedRecord() throws Exception {
        EasyMock.expect(
                taxonService.load(EasyMock.eq("urn:lsid:example.com:test:123"),
                        EasyMock.isA(String.class))).andReturn(null);
        Taxon t = processor.process(record);
        assertNull("Taxon should be null because the record was deleted", t);
    }

}
