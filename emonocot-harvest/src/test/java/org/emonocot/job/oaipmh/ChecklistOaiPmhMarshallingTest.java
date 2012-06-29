package org.emonocot.job.oaipmh;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;

import java.net.URI;

import javax.xml.transform.stax.StAXSource;

import org.emonocot.job.common.AbstractXmlEventReaderTest;
import org.emonocot.model.marshall.xml.StaxDriver;
import org.emonocot.model.marshall.xml.UriConverter;
import org.emonocot.model.marshall.xml.XStreamMarshaller;
import org.junit.Before;
import org.junit.Test;
import org.openarchives.pmh.Record;
import org.openarchives.pmh.marshall.xml.OpenArchivesQNameMapFactory;
import org.openarchives.pmh.marshall.xml.ReflectionProviderFactory;
import org.openarchives.pmh.marshall.xml.ResumptionTokenConverter;
import org.springframework.oxm.Unmarshaller;
import org.tdwg.voc.DefinedTermLinkType;
import org.tdwg.voc.Distribution;
import org.tdwg.voc.PublicationCitation;
import org.tdwg.voc.PublicationTypeTerm;
import org.tdwg.voc.Relationship;
import org.tdwg.voc.SpeciesProfileModel;
import org.tdwg.voc.TaxonConcept;
import org.tdwg.voc.TaxonName;
import org.tdwg.voc.TaxonRankTerm;
import org.tdwg.voc.TaxonRelationshipTerm;

import com.bea.xml.stream.MXParserFactory;
import com.thoughtworks.xstream.converters.ConverterMatcher;

/**
 *
 * @author ben
 *
 */
public class ChecklistOaiPmhMarshallingTest extends
        AbstractXmlEventReaderTest {
    /**
     *
     */
    private Unmarshaller unmarshaller = null;

    /**
     *
     */
    private String filename
        = "/org/emonocot/job/oai/Acoraceae_fragment.xml";

    /**
     * @throws Exception if there is a problem initializing the unmarshaller
     */
    @Before
    public final void setUp() throws Exception {
        OpenArchivesQNameMapFactory oaiPmhQNameMapFactory
        = new OpenArchivesQNameMapFactory();
        oaiPmhQNameMapFactory.afterPropertiesSet();
        ReflectionProviderFactory reflectionProviderFactory
        = new ReflectionProviderFactory();
        reflectionProviderFactory.afterPropertiesSet();

        StaxDriver streamDriver = new StaxDriver(
                oaiPmhQNameMapFactory.getObject());
        streamDriver.setRepairingNamespace(false);
        streamDriver.setXmlInputFactory(new MXParserFactory());

        unmarshaller = new XStreamMarshaller();

        ((XStreamMarshaller) unmarshaller).setAutodetectAnnotations(true);
        ((XStreamMarshaller) unmarshaller)
            .setReflectionProvider(reflectionProviderFactory.getObject());
        ((XStreamMarshaller) unmarshaller)
            .setQNameMap(oaiPmhQNameMapFactory.getObject());

        ((XStreamMarshaller) unmarshaller).setStreamDriver(streamDriver);
        ((XStreamMarshaller) unmarshaller).setConverters(
                new ConverterMatcher[] {
                        new ResumptionTokenConverter(),
                        new UriConverter()
                        });
        ((XStreamMarshaller) unmarshaller).afterPropertiesSet();

    }

    /**
     * @throws Exception
     *             if there is a problem
     *
     */
    @Test
    public final void testParseTaxonItemFragment() throws
            Exception {

        Record record = (Record) unmarshaller
            .unmarshal(new StAXSource(getXMLEventReader(filename)));

        assertNotNull("Result from unmarshal should not be null", record);
        assertNotNull("Metadata should not be null", record.getMetadata());
        TaxonConcept taxonConcept = record.getMetadata().getTaxonConcept();
        assertNotNull("Taxon concept should not be null", taxonConcept);
        assertEquals("The identifier should be as expected",
                new URI("urn:kew.org:wcs:taxon:29332"),
                taxonConcept.getIdentifier());
        TaxonName taxonName = taxonConcept.getHasName();
        assertNotNull("TaxonName should not be null", taxonName);
        assertEquals("Authorship should be Gersault", "Gersault",
                taxonName.getAuthorship());
        assertEquals("Combination authorhship should be equal to Gersault",
                "Gersault", taxonName.getCombinationAuthorship());
        assertEquals("Namecomplete should should be equal to Calamus",
                "Calamus", taxonName.getNameComplete());
        assertEquals("Uninomial should should be equal to Calamus",
                "Calamus", taxonName.getUninomial());
        assertEquals("Rank should should be equal to Genus",
                TaxonRankTerm.GENUS, taxonName.getRank());
        assertEquals("Rank String should should be equal to Genus",
                "Genus", taxonName.getRankString());
        PublicationCitation protologue
          = taxonName.getPublishedInCitations().iterator().next();
        assertEquals("The identifier of the protologue should be correct",
                "urn:kew.org:wcs:placeOfPublication:1738",
                protologue.getIdentifier().toString());
        assertEquals("The title should be correct",
                "Fig. Pl. Méd.",
                protologue.getTpubTitle());
        assertEquals("The pages should be correct",
                " t. 40",
                protologue.getPages());
        assertEquals("The date published should be correct",
                "(1764)",
                protologue.getDatePublished());
        assertEquals("The publication type should be correct",
                PublicationTypeTerm.GENERIC,
                protologue.getPublicationType());
        PublicationCitation citation
            = taxonConcept.getPublishedInCitations().iterator().next();
        assertEquals("The identifier of the citation should be correct",
                "urn:kew.org:wcs:publicationEdition:4",
                citation.getIdentifier().toString());
        assertEquals("The publication type should be correct",
                PublicationTypeTerm.BOOK_SECTION,
                citation.getPublicationType());
        assertNotNull("The Parent Publication should not be null",
                citation.getParentPublication());
        PublicationCitation parentPublication = citation.getParentPublication();
        assertEquals("The identifier of the parent publication should be correct",
                "urn:kew.org:wcs:publication:4",
                parentPublication.getIdentifier().toString());
        assertEquals("The title should be correct",
                "World Checklist and Bibliography of Araceae (and Acoraceae)",
                parentPublication.getTpubTitle());
        assertEquals("The authorship should be correct",
                "Govaerts, R. & Frodin, D.G.",
                parentPublication.getAuthorship());
        assertEquals("The publisher should be correct",
                "The Board of Trustees of the Royal Botanic Gardens, Kew",
                parentPublication.getPublisher());
        assertEquals("The publication type should be correct",
                PublicationTypeTerm.BOOK,
                parentPublication.getPublicationType());

        Relationship relationship =
            taxonConcept.getHasRelationship().iterator().next();
        assertEquals("The taxon identifier should be correct",
                "urn:kew.org:wcs:taxon:2295",
                relationship.getToTaxonRelation().getResource().toString());
        assertEquals("The relationship category should be correct",
                TaxonRelationshipTerm.IS_SYNONYM_FOR,
                relationship.getRelationshipCategory());
        SpeciesProfileModel spm =
            taxonConcept.getDescribedBy().iterator().next();
        Distribution distribution
            = (Distribution) spm.getHasInformation().iterator().next();
        DefinedTermLinkType hasValue = distribution.getHasValueRelation().iterator().next();
        assertEquals("The geographical area should be correct",
                "http://rs.tdwg.org/ontology/voc/GeographicRegion.rdf#GER",
                hasValue.getResource().toString());
    }
}
