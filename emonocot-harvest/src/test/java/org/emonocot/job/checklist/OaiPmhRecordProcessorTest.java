package org.emonocot.job.checklist;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNull;
import static org.junit.Assert.assertNotNull;

import java.net.URI;
import java.util.HashSet;
import java.util.Set;

import org.easymock.EasyMock;
import org.emonocot.api.SourceService;
import org.emonocot.api.TaxonService;
import org.emonocot.harvest.common.TaxonRelationshipResolver;
import org.emonocot.harvest.common.TaxonRelationshipResolverImpl;
import org.emonocot.model.geography.GeographyConverter;
import org.emonocot.model.reference.ReferenceTypeConverter;
import org.emonocot.model.source.Source;
import org.emonocot.model.taxon.RankConverter;
import org.emonocot.model.taxon.Taxon;
import org.emonocot.model.taxon.TaxonomicStatus;
import org.emonocot.model.taxon.TaxonomicStatusConverter;
import org.joda.time.DateTime;
import org.junit.Before;
import org.junit.Test;
import org.openarchives.pmh.Header;
import org.openarchives.pmh.Metadata;
import org.openarchives.pmh.Record;
import org.openarchives.pmh.Status;
import org.springframework.batch.core.JobExecution;
import org.springframework.batch.core.StepExecution;
import org.springframework.context.support.ConversionServiceFactoryBean;
import org.springframework.core.convert.converter.Converter;
import org.tdwg.voc.TaxonConcept;
import org.tdwg.voc.TaxonStatusTerm;

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
    *
    */
   private SourceService sourceService;

    /**
     * @throws Exception if something goes wrong
     */
    @Before
    public final void setUp() throws Exception {
        URI identifier = new URI("urn:lsid:example.com:test:123");
        processor = new OaiPmhRecordProcessorImpl();
        record = new Record();
        Metadata metadata = new Metadata();
        TaxonConcept taxonConcept = new TaxonConcept();
        taxonConcept.setIdentifier(identifier);

        TaxonStatusTerm status = new TaxonStatusTerm();
        status.setIdentifier(new URI(
                "http://e-monocot.org/TaxonomicStatus#synonym"));
        taxonConcept.setStatus(status);
        metadata.setTaxonConcept(taxonConcept);
        record.setMetadata(metadata);
        Header header = new Header();
        header.setIdentifier(identifier);
        header.setDatestamp(new DateTime());
        record.setHeader(header);
        taxonService = EasyMock.createMock(TaxonService.class);
        processor.setTaxonService(taxonService);

        Set<Converter> converters = new HashSet<Converter>();
        converters.add(new TaxonomicStatusConverter());
        converters.add(new GeographyConverter());
        converters.add(new ReferenceTypeConverter());
        converters.add(new RankConverter());
        ConversionServiceFactoryBean factoryBean = new ConversionServiceFactoryBean();
        factoryBean.setConverters(converters);
        factoryBean.afterPropertiesSet();
        processor.setConversionService(factoryBean.getObject());

        processor.setSourceName("WCS");
        sourceService = EasyMock.createMock(SourceService.class);
        processor.setSourceService(sourceService);

        TaxonRelationshipResolver resolver = new TaxonRelationshipResolverImpl();
        processor.setTaxonRelationshipResolver(resolver);

        StepExecution stepExecution = new StepExecution("test step",
                new JobExecution(1L));
        processor.beforeStep(stepExecution);
    }

    /**
     * @throws Exception if something goes wrong
     */
    @Test
    public final void testSkipDeletedRecord() throws Exception {
        record.getHeader().setStatus(Status.deleted);
        EasyMock.expect(
                taxonService.load(EasyMock.eq("urn:lsid:example.com:test:123"),
                        EasyMock.isA(String.class))).andReturn(null);
        Taxon t = processor.process(record);
        assertNull("Taxon should be null because the record was deleted", t);
    }

    /**
     *
     * @throws Exception if there is a problem converting the object
     */
    @Test
    public final void testConvertTaxonomicStatus() throws Exception {
        EasyMock.expect(
                taxonService.find(EasyMock.eq("urn:lsid:example.com:test:123"),
                        EasyMock.isA(String.class))).andReturn(null);
        EasyMock.expect(sourceService.load(EasyMock.eq("WCS"))).andReturn(
                new Source());
        EasyMock.replay(taxonService, sourceService);
        Taxon t = processor.process(record);
        assertNotNull("Taxon should not be null", t);
        assertEquals("Taxonomic Status should be set correctly", t.getStatus(), TaxonomicStatus.synonym);
    }

}
