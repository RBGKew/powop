package org.emonocot.job.checklist;

import static org.junit.Assert.assertNull;

import java.net.URI;

import org.easymock.EasyMock;
import org.emonocot.api.TaxonService;
import org.emonocot.model.taxon.Taxon;
import org.joda.time.DateTime;
import org.junit.Before;
import org.junit.Test;
import org.openarchives.pmh.Header;
import org.openarchives.pmh.Metadata;
import org.openarchives.pmh.Record;
import org.springframework.batch.core.JobExecution;
import org.springframework.batch.core.StepExecution;
import org.tdwg.voc.TaxonConcept;
import org.tdwg.voc.TaxonName;

/**
 * Test to verify that the OaiPmhProcessor filters illegal characters.
 * Should be dealt with in the service too.
 *
 * http://build.e-monocot.org/bugzilla/show_bug.cgi?id=101
 * @author ben
 *
 */
public class InvalidCharacterTest {

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
       header.setDatestamp(new DateTime());
       record.setHeader(header);
       Metadata metadata = new Metadata();
       record.setMetadata(metadata);
       TaxonConcept taxonConcept = new TaxonConcept();
       taxonConcept.setIdentifier(new URI("urn:lsid:example.com:test:123"));
       metadata.setTaxonConcept(taxonConcept);
       TaxonName taxonName = new TaxonName();
       taxonConcept.setHasName(taxonName);
       taxonName.setAuthorship("\u008A");
       taxonService = EasyMock.createMock(TaxonService.class);
       processor.setTaxonService(taxonService);
       processor.beforeStep(new StepExecution("test", new JobExecution(1L)));
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
       assertNull("Authorship should be null because it contains invalid characters", t.getAuthorship());
   }
}
