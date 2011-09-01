package org.emonocot.checklist.controller.oai;

import static com.jayway.restassured.RestAssured.expect;
import static com.jayway.restassured.RestAssured.given;
import static com.jayway.restassured.path.xml.XmlPath.with;
import static org.junit.Assert.assertEquals;

import java.util.Properties;

import org.junit.Before;
import org.junit.Test;
import org.springframework.core.io.ClassPathResource;
import org.springframework.core.io.Resource;
import org.springframework.http.HttpStatus;

import com.jayway.restassured.RestAssured;

/**
 *
 * @author ben
 *
 */
public class ChecklistOaiPmhWebserviceFunctionalTest {

    /**
     *
     */
    private static final int TOTAL_NUMBER_OF_NODES = 8;

  /**
   *
   */
    private static final int NODES_IN_LOWIACEAE = 5;

    /**
    *
    */
     private static final int NUMBER_OF_DISTRIBUTION_RECORDS = 3;

    /**
    *
    */
    private Properties properties;

    /**
     * @throws Exception
     *             if there is a problem instantiating the selenium server
     */
    @Before
    public final void setUp() throws Exception {
        Resource propertiesFile
        = new ClassPathResource("application.properties");
        properties = new Properties();
        properties.load(propertiesFile.getInputStream());
        RestAssured.baseURI = properties.getProperty(
                "functional.test.baseUri",
                "http://build.e-monocot.org");
        RestAssured.port = Integer.parseInt(
                properties.getProperty("functional.test.port",
                "80"));
        RestAssured.basePath = properties.getProperty(
                "functional.test.basePath",
                "/latest/checklist");

        if (properties.getProperty("http.proxyHost", null) != null
                && properties
                  .getProperty("http.proxyHost", null).length() > 0) {
            RestAssured.proxyHost = properties.getProperty("http.proxyHost",
                    null);
            RestAssured.proxyPort = Integer.parseInt(properties.getProperty(
                    "http.proxyPort", "8080"));
            RestAssured.proxyScheme = properties.getProperty(
                    "http.proxyScheme", "http");
        }
    }

    /**
     * Tests that any request which does not include the parameter
     * "scratchpad" results in a "400 BAD REQUEST".
     */
    @Test
    public final void testRequestWithoutClientParameter() {
        given().parameters("verb", "Identify").expect()
        .statusCode(HttpStatus.BAD_REQUEST.value())
        .get("/oai");
    }

    /**
     * Tests ListIdentifiers without any restrictions.
     */
    @Test
    public final void testListIdentifiers() {

        String xml = given()
                .parameters("verb", "ListIdentifiers", "metadataPrefix",
                        "oai_dc",
                        "scratchpad", "functional-test.e-monocot.org")
                        .get("/oai").asString();
        assertEquals("There should be 8 identifiers returned",
                TOTAL_NUMBER_OF_NODES,
                with(xml).get("OAI-PMH.ListIdentifiers.header.size()"));
    }

    /**
     * Tests ListIdentifiers in a set.
     */
    @Test
    public final void testListIdentifiersInSet() {

        String xml = given()
                .parameters("verb", "ListIdentifiers", "metadataPrefix",
                        "oai_dc", "set", "Lowiaceae",
                        "scratchpad", "functional-test.e-monocot.org")
                        .get("/oai").asString();
        assertEquals("There should be 5 identifiers returned",
                NODES_IN_LOWIACEAE,
                with(xml).get("OAI-PMH.ListIdentifiers.header.size()"));
    }

    /**
     * Tests ListIdentifiers where no nodes match.
     */
    @Test
    public final void testListIdentifiersInEmptySet() {
        expect().statusCode(HttpStatus.BAD_REQUEST.value()).given()
                .parameters("verb", "ListIdentifiers", "metadataPrefix",
                        "oai_dc", "set", "Orchidaceae",
                        "scratchpad", "functional-test.e-monocot.org")
                        .get("/oai");
    }

    /**
     * Tests ListIdentifiers of object modified after a certain date.
     */
    @Test
    public final void testListIdentifiersFromDate() {

        String xml = given()
                .parameters("verb", "ListIdentifiers", "metadataPrefix",
                        "oai_dc", "from", "2011-09-01T01:00:00Z",
                        "scratchpad", "functional-test.e-monocot.org")
                        .get("/oai").asString();
        assertEquals("There should be no identifiers returned",
                0,
                with(xml).get("OAI-PMH.ListIdentifiers.header.size()"));
    }

    /**
     * In response to http://build.e-monocot.org/bugzilla/show_bug.cgi?id=67.
     */
    @Test
    public final void testHandleFromDateWithHoursOfDayAfterTwelve() {

        String xml = given()
                .parameters("verb", "ListIdentifiers", "metadataPrefix",
                        "oai_dc", "from", "2011-09-01T21:00:00Z",
                        "scratchpad", "functional-test.e-monocot.org")
                        .get("/oai").asString();
        assertEquals("There should be no identifiers returned",
                0,
                with(xml).get("OAI-PMH.ListIdentifiers.header.size()"));
    }

    /**
     * Tests ListIdentifiers of object modified before a certain date.
     */
    @Test
    public final void testListIdentifiersUntilDate() {

        String xml = given()
                .parameters("verb", "ListIdentifiers", "metadataPrefix",
                        "oai_dc", "until", "2011-09-01T01:00:00Z",
                        "scratchpad", "functional-test.e-monocot.org")
                        .get("/oai").asString();
        assertEquals("There should be 7 identifiers returned",
                7,
                with(xml).get("OAI-PMH.ListIdentifiers.header.size()"));
    }

    /**
     * Tests a returned record to check that the
     * metadata is being serialized properly.
     */
    @Test
    public final void testGetRecord() {

        String xml = given()
                .parameters("verb", "GetRecord", "metadataPrefix",
                        "rdf", "identifier", "urn:kew.org:wcs:taxon:1",
                        "scratchpad", "functional-test.e-monocot.org")
                        .get("/oai").asString();
        assertEquals(
                "The response should include the identifier of the "
                + "taxon concept",
                "urn:kew.org:wcs:taxon:1",
                with(xml).get(
                "OAI-PMH.GetRecord.record.metadata.TaxonConcept.identifier"));
        assertEquals(
                "The response should include the identifier of the "
                + "taxon name",
                "urn:kew.org:wcs:name:1",
                with(xml).get(
                "OAI-PMH.GetRecord.record.metadata.TaxonConcept.hasName.TaxonName.identifier"));
        assertEquals(
                "The authorship should be present",
                "(Archer) Pargetter",
                with(xml).get(
                "OAI-PMH.GetRecord.record.metadata.TaxonConcept.hasName.TaxonName.authorship"));
        assertEquals(
                "The basionymAuthorship should be present",
                "Archer",
                with(xml).get(
                "OAI-PMH.GetRecord.record.metadata.TaxonConcept.hasName.TaxonName.basionymAuthorship"));
        assertEquals(
                "The combinationAuthorship should be present",
                "Pargetter",
                with(xml).get(
                "OAI-PMH.GetRecord.record.metadata.TaxonConcept.hasName.TaxonName.combinationAuthorship"));
        assertEquals(
                "The nameComplete should be present",
                "Lorem ipsum",
                with(xml).get(
                "OAI-PMH.GetRecord.record.metadata.TaxonConcept.hasName.TaxonName.nameComplete"));
        assertEquals(
                "The genusPart should be present",
                "Lorem",
                with(xml).get(
                "OAI-PMH.GetRecord.record.metadata.TaxonConcept.hasName.TaxonName.genusPart"));
        assertEquals(
                "The specificEpithet should be present",
                "ipsum",
                with(xml).get(
                "OAI-PMH.GetRecord.record.metadata.TaxonConcept.hasName.TaxonName.specificEpithet"));
        assertEquals(
                "The rank should be present",
                "http://rs.tdwg.org/ontology/voc/TaxonRank#Species",
                with(xml).get(
                "OAI-PMH.GetRecord.record.metadata.TaxonConcept.hasName.TaxonName.rank.@resource"));
        assertEquals(
                "The protologue title should be present",
                "Integer elementum lorem ut nibh scelerisque at condimentum",
                with(xml).get(
                "OAI-PMH.GetRecord.record.metadata.TaxonConcept.hasName.TaxonName.publishedInCitation.PublicationCitation.title"));
        assertEquals(
                "The protologue authorship should be present",
                "Pargetter",
                with(xml).get(
                "OAI-PMH.GetRecord.record.metadata.TaxonConcept.hasName.TaxonName.publishedInCitation.PublicationCitation.authorship"));
        assertEquals(
                "The protologue type should be present",
                "http://rs.tdwg.org/ontology/voc/PublicationCitation#Generic",
                with(xml).get(
                "OAI-PMH.GetRecord.record.metadata.TaxonConcept.hasName.TaxonName.publishedInCitation.PublicationCitation.publicationType.@resource"));
        assertEquals(
                "The protologue volume should be present",
                "2",
                with(xml).get(
                "OAI-PMH.GetRecord.record.metadata.TaxonConcept.hasName.TaxonName.publishedInCitation.PublicationCitation.volume"));
        assertEquals(
                "The protologue pages should be present",
                " 34-56",
                with(xml).get(
                "OAI-PMH.GetRecord.record.metadata.TaxonConcept.hasName.TaxonName.publishedInCitation.PublicationCitation.pages"));
        assertEquals(
                "The protologue date published should be present",
                "1784",
                with(xml).get(
                "OAI-PMH.GetRecord.record.metadata.TaxonConcept.hasName.TaxonName.publishedInCitation.PublicationCitation.datePublished"));
        assertEquals(
                "The rankString should be present",
                "Species",
                with(xml).get(
                "OAI-PMH.GetRecord.record.metadata.TaxonConcept.hasName.TaxonName.rankString"));
        assertEquals(
                "The synonyms should be present",
                2,
                with(xml).get(
                "OAI-PMH.GetRecord.record.metadata.TaxonConcept.hasRelationship.Relationship.relationshipCategory.findAll { it.@resource == 'http://rs.tdwg.org/ontology/voc/TaxonConcept#HasSynonym' }.size()"));
        assertEquals(
                "The parent should be present",
                1,
                with(xml).get(
                "OAI-PMH.GetRecord.record.metadata.TaxonConcept.hasRelationship.Relationship.relationshipCategory.findAll { it.@resource == 'http://rs.tdwg.org/ontology/voc/TaxonConcept#IsChildTaxonOf' }.size()"));
        assertEquals(
                "The distributional data should be present",
                NUMBER_OF_DISTRIBUTION_RECORDS,
                with(xml).get(
                "OAI-PMH.GetRecord.record.metadata.TaxonConcept.describedBy.SpeciesProfileModel.hasInformation.Distribution.size()"));
        assertEquals(
                "The citation title should be present",
                "Vestibulum erat massa dapibus sit amet dictum vel",
                with(xml).get(
                "OAI-PMH.GetRecord.record.metadata.TaxonConcept.publishedInCitation.PublicationCitation.title"));
        assertEquals(
                "The citation authorship should be present",
                "Pargetter",
                with(xml).get(
                "OAI-PMH.GetRecord.record.metadata.TaxonConcept.publishedInCitation.PublicationCitation.authorship"));
        assertEquals(
                "The citation type should be present",
                "http://rs.tdwg.org/ontology/voc/PublicationCitation#BookSection",
                with(xml).get(
                "OAI-PMH.GetRecord.record.metadata.TaxonConcept.publishedInCitation.PublicationCitation.publicationType.@resource"));
        assertEquals(
                "The publication title should be present",
                "Lorem ipsum dolor sit amet consectetur adipiscing elit",
                with(xml).get(
                "OAI-PMH.GetRecord.record.metadata.TaxonConcept.publishedInCitation.PublicationCitation.parentPublication.PublicationCitation.title"));
        assertEquals(
                "The publication authorship should be present",
                "Archer",
                with(xml).get(
                "OAI-PMH.GetRecord.record.metadata.TaxonConcept.publishedInCitation.PublicationCitation.parentPublication.PublicationCitation.authorship"));
        assertEquals(
                "The publication publisher should be present",
                "Lorem",
                with(xml).get(
                "OAI-PMH.GetRecord.record.metadata.TaxonConcept.publishedInCitation.PublicationCitation.parentPublication.PublicationCitation.publisher"));
        assertEquals(
                "The publication type should be present",
                "http://rs.tdwg.org/ontology/voc/PublicationCitation#Book",
                with(xml).get(
                "OAI-PMH.GetRecord.record.metadata.TaxonConcept.publishedInCitation.PublicationCitation.parentPublication.PublicationCitation.publicationType.@resource"));

    }

    /**
     * Test that the accepted name relationships are being serialized properly
     * In response to http://build.e-monocot.org/bugzilla/show_bug.cgi?id=69.
     */
    @Test
    public final void testGetSynonymRecord() {

        String xml = given()
                .parameters("verb", "GetRecord", "metadataPrefix",
                        "rdf", "identifier", "urn:kew.org:wcs:taxon:6",
                        "scratchpad", "functional-test.e-monocot.org")
                        .get("/oai").asString();

        assertEquals(
                "The accepted name should be present",
                1,
                with(xml).get(
                "OAI-PMH.GetRecord.record.metadata.TaxonConcept.hasRelationship.Relationship.relationshipCategory.findAll { it.@resource == 'http://rs.tdwg.org/ontology/voc/TaxonConcept#IsSynonymFor' }.size()"));
        assertEquals(
                "The accepted name should be serialized as a link",
                "urn:kew.org:wcs:taxon:6",
                with(xml).get(
                "OAI-PMH.GetRecord.record.metadata.TaxonConcept.hasRelationship.Relationship.toTaxon.@resource"));
        }
}
