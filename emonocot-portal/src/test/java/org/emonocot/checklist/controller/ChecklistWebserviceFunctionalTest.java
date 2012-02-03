package org.emonocot.checklist.controller;

import static com.jayway.restassured.RestAssured.given;
import static com.jayway.restassured.path.xml.XmlPath.with;
import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertTrue;

import java.util.Properties;

import org.emonocot.test.TestDataManager;
import org.junit.After;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.io.ClassPathResource;
import org.springframework.core.io.Resource;
import org.springframework.http.HttpStatus;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;

import com.jayway.restassured.RestAssured;

/**
 *
 * @author ben
 *
 */
@RunWith(SpringJUnit4ClassRunner.class)
@ContextConfiguration("/applicationContext-test.xml")
public class ChecklistWebserviceFunctionalTest {

    /**
    *
    */
    private Properties properties;

    /**
    *
    */
    @Autowired
    private TestDataManager testDataManager;

    /**
     * @throws Exception
     *             if there is a problem instantiating the selenium server
     */
    @Before
    public final void setUp() throws Exception {
        Resource propertiesFile = new ClassPathResource(
                "application.properties");
        properties = new Properties();
        properties.load(propertiesFile.getInputStream());
        RestAssured.baseURI = properties.getProperty("functional.test.baseUri",
                "http://build.e-monocot.org");
        RestAssured.port = Integer.parseInt(properties.getProperty(
                "functional.test.port", "80"));
        RestAssured.basePath = properties.getProperty(
                "functional.test.basePath", "/latest/checklist");

        if (properties.getProperty("http.proxyHost", null) != null
                && properties.getProperty("http.proxyHost", null).length() > 0) {
            RestAssured.proxyHost = properties.getProperty("http.proxyHost",
                    null);
            RestAssured.proxyPort = Integer.parseInt(properties.getProperty(
                    "http.proxyPort", "8080"));
            RestAssured.proxyScheme = properties.getProperty(
                    "http.proxyScheme", "http");
        }
        testDataManager.createTaxon("urn:kew.org:wcs:taxon:3", "Lorem",
                "Lowiaceae", "Lorem",
                null, "GENUS", "accepted", null, null, null,
                null, null, null, null, null, null, null, null, null, null, null, null, null, null, null);
        testDataManager.createTaxon("urn:kew.org:wcs:taxon:1", "Lorem ipsum",
                "Lowiaceae", "Lorem", "ipsum", "SPECIES", "accepted",
                null, null, null, null, null, null, null, null, null, null,
                null, null, null, null, "urn:kew.org:wcs:taxon:3", null, null, null);
        testDataManager.createTaxon("urn:kew.org:wcs:taxon:2", "Lorem dolor",
                "Lowiaceae", "Lorem", "dolor", "SPECIES", "accepted",
                null, null, null, null, null, null, null, null, null, null,
                null, null, null, null, "urn:kew.org:wcs:taxon:3", null, null, null);
        testDataManager.createTaxon("urn:kew.org:wcs:taxon:4",
                "Lorem consectetur", "Lowiaceae", "Lorem", "consectetur",
                "SPECIES", "synonym", null, null, null, null, null, null, null,
                null, null, null, null, null, null, null,
                null, "urn:kew.org:wcs:taxon:1", null, null);
        testDataManager.createTaxon("urn:kew.org:wcs:taxon:5",
                "Lorem adipiscing", "Lowiaceae", "Lorem", "adipiscing",
                "SPECIES", "synonym", null, null, null, null, null, null, null,
                null, null, null, null, null, null, null,
                null, "urn:kew.org:wcs:taxon:1", null, null);
        testDataManager.createTaxon("urn:kew.org:wcs:taxon:6",
                "Curabitur vehicula", "Curabituraceae", "Curabitur",
                "vehicula", "SPECIES", "accepted", null, null, null, null,
                null, null, null, null, null, null, null, null, null, null,
                null, null, null, null);
        testDataManager.createTaxon("urn:kew.org:wcs:taxon:7",
                "Curabitur nulla", "Curabituraceae", "Curabitur", "nulla",
                "SPECIES", "unplaced", null, null, null, null, null, null,
                null, null, null, null, null, null, null, null, null, null, null, null);
    }

    /**
     * Tests that any request which does not include the parameter "scratchpad"
     * results in a "400 BAD REQUEST".
     */
    @Test
    public final void testRequestWithoutClientParameter() {
        given().expect().statusCode(HttpStatus.BAD_REQUEST.value())
                .get("/endpoint");
    }

    /**
     * Tests that the PING request returns the response "200 OK".
     *
     * @throws Exception
     *             if there is a problem with the test
     */
    @Test
    public final void testPing() throws Exception {
        given().parameters("scratchpad", "functional-test.e-monocot.org")
                .expect().statusCode(HttpStatus.OK.value()).get("/endpoint");
    }

    /**
     * Tests the GET response returns a valid document containing a taxon of
     * species rank.
     *
     * @throws Exception
     *             if there is a problem with the test
     */
    @Test
    public final void testGet() throws Exception {
        String xml = given()
                .parameters("function", "details_tcs", "id",
                        "urn:kew.org:wcs:taxon:1", "scratchpad",
                        "functional-test.e-monocot.org").get("/endpoint")
                .andReturn().body().asString();
        assertEquals("TaxonName id should equal 'urn:kew.org:wcs:name:1'",
                "urn:kew.org:wcs:name:1",
                with(xml).get("DataSet.TaxonNames.TaxonName.@id"));
        assertEquals("Nomenclatural code should equal 'Botanical'",
                "Botanical",
                with(xml)
                        .get("DataSet.TaxonNames.TaxonName.@nomenclaturalCode"));
        assertEquals("Simple should equal 'Lorem ipsum'", "Lorem ipsum",
                with(xml).get("DataSet.TaxonNames.TaxonName.Simple"));
        assertEquals("Rank should equal 'Species'", "Species",
                with(xml).get("DataSet.TaxonNames.TaxonName.Rank"));
        assertEquals("Rank code should equal 'sp'", "sp",
                with(xml).get("DataSet.TaxonNames.TaxonName.Rank.@code"));
        assertEquals(
                "CanonicalName.Simple should equal 'Lorem ipsum'",
                "Lorem ipsum",
                with(xml).get(
                        "DataSet.TaxonNames.TaxonName.CanonicalName.Simple"));
        assertEquals(
                "TaxonConcept.Name scientific should equal 'true'",
                "true",
                with(xml).get(
                        "DataSet.TaxonConcepts.TaxonConcept.Name.@scientific"));
        assertEquals(
                "TaxonConcept.Name ref should equal 'urn:kew.org:wcs:name:1'",
                "urn:kew.org:wcs:name:1",
                with(xml).get("DataSet.TaxonConcepts.TaxonConcept.Name.@ref"));
        assertEquals("TaxonConcept.Name should equal 'Lorem ipsum'",
                "Lorem ipsum",
                with(xml).get("DataSet.TaxonConcepts.TaxonConcept.Name"));
        assertEquals("TaxonConcept.Rank should equal 'Species'", "Species",
                with(xml).get("DataSet.TaxonConcepts.TaxonConcept.Rank"));
        assertEquals("TaxonConcept.Rank code should equal 'sp'", "sp",
                with(xml).get("DataSet.TaxonConcepts.TaxonConcept.Rank.@code"));
        assertEquals("TaxonConcept.Rank code should equal 'sp'", "sp",
                with(xml).get("DataSet.TaxonConcepts.TaxonConcept.Rank.@code"));
        assertEquals(
                "Two synonyms should be present",
                2,
                with(xml)
                        .get("DataSet.TaxonConcepts.TaxonConcept.TaxonRelationships.TaxonRelationship.findAll { it.@type == 'has synonym' }.size()"));
        assertEquals(
                "One taxonomic parent should be present",
                1,
                with(xml)
                        .get("DataSet.TaxonConcepts.TaxonConcept.TaxonRelationships.TaxonRelationship.findAll { it.@type == 'is child taxon of' }.size()"));
        assertTrue(
                "The links to external data should be absolute, not relative",
                ((String) with(xml)
                        .get("DataSet.TaxonConcepts.TaxonConcept.TaxonRelationships.TaxonRelationship[0].ToTaxonConcept.@ref"))
                        .startsWith("http://"));
        assertTrue(
                "The links to external data should include the client id parameter",
                ((String) with(xml)
                        .get("DataSet.TaxonConcepts.TaxonConcept.TaxonRelationships.TaxonRelationship[0].ToTaxonConcept.@ref"))
                        .contains("&scratchpad=functional-test.e-monocot.org"));
    }

    /**
     * Tests the GET response returns a valid document containing a synonym.
     *
     * @throws Exception
     *             if there is a problem with the test
     */
    @Test
    public final void testSynonym() throws Exception {
        String xml = given()
                .parameters("function", "details_tcs", "id",
                        "urn:kew.org:wcs:taxon:4", "scratchpad",
                        "functional-test.e-monocot.org").get("/endpoint")
                .andReturn().body().asString();

        assertEquals(
                "One accepted name should be present",
                1,
                with(xml)
                        .get("DataSet.TaxonConcepts.TaxonConcept.TaxonRelationships.TaxonRelationship.findAll { it.@type == 'is synonym for' }.size()"));
    }

    /**
     * Tests the GET response returns a valid document containing a taxon of
     * species rank.
     *
     * @throws Exception
     *             if there is a problem with the test
     */
    @Test
    public final void testGenus() throws Exception {
        String xml = given()
                .parameters("function", "details_tcs", "id",
                        "urn:kew.org:wcs:taxon:3", "scratchpad",
                        "functional-test.e-monocot.org").get("/endpoint")
                .andReturn().body().asString();

        assertEquals(
                "One taxonomic parent should be present",
                1,
                with(xml)
                        .get("DataSet.TaxonConcepts.TaxonConcept.TaxonRelationships.TaxonRelationship.findAll { it.@type == 'is child taxon of' }.size()"));
    }

    /**
     * Tests the GET response returns a valid document containing a taxon of
     * family rank.
     *
     * @throws Exception
     *             if there is a problem with the test
     */
    @Test
    public final void testGetFamily() throws Exception {
        String xml = given()
                .parameters("function", "details_tcs", "id",
                        "urn:kew.org:wcs:family:28", "scratchpad",
                        "functional-test.e-monocot.org").get("/endpoint")
                .andReturn().body().asString();
        assertEquals(
                "TaxonName id should equal 'urn:kew.org:wcs:familyName:28'",
                "urn:kew.org:wcs:familyName:28",
                with(xml).get("DataSet.TaxonNames.TaxonName.@id"));
        assertEquals("Nomenclatural code should equal 'Botanical'",
                "Botanical",
                with(xml)
                        .get("DataSet.TaxonNames.TaxonName.@nomenclaturalCode"));
        assertEquals("Simple should equal 'Lowiaceae'", "Lowiaceae", with(xml)
                .get("DataSet.TaxonNames.TaxonName.Simple"));
        assertEquals("Rank should equal 'Family'", "Family",
                with(xml).get("DataSet.TaxonNames.TaxonName.Rank"));
        assertEquals("Rank code should equal 'fam'", "fam",
                with(xml).get("DataSet.TaxonNames.TaxonName.Rank.@code"));
        assertEquals(
                "CanonicalName.Simple should equal 'Lowiaceae'",
                "Lowiaceae",
                with(xml).get(
                        "DataSet.TaxonNames.TaxonName.CanonicalName.Simple"));
        assertEquals(
                "TaxonConcept.Name scientific should equal 'true'",
                "true",
                with(xml).get(
                        "DataSet.TaxonConcepts.TaxonConcept.Name.@scientific"));
        assertEquals(
                "TaxonConcept.Name ref should equal 'urn:kew.org:wcs:familyName:28'",
                "urn:kew.org:wcs:familyName:28",
                with(xml).get("DataSet.TaxonConcepts.TaxonConcept.Name.@ref"));
        assertEquals("TaxonConcept.Name should equal 'Lowiaceae'", "Lowiaceae",
                with(xml).get("DataSet.TaxonConcepts.TaxonConcept.Name"));
        assertEquals("TaxonConcept.Rank should equal 'Family'", "Family",
                with(xml).get("DataSet.TaxonConcepts.TaxonConcept.Rank"));
        assertEquals("TaxonConcept.Rank code should equal 'fam'", "fam",
                with(xml).get("DataSet.TaxonConcepts.TaxonConcept.Rank.@code"));
        assertEquals("TaxonConcept.Rank code should equal 'fam'", "fam",
                with(xml).get("DataSet.TaxonConcepts.TaxonConcept.Rank.@code"));

        assertEquals(
                "No taxonomic parents should be present",
                0,
                with(xml)
                        .get("DataSet.TaxonConcepts.TaxonConcept.TaxonRelationships.TaxonRelationship.findAll { it.@type == 'is child taxon of' }.size()"));
        assertTrue(
                "The links to external data should be absolute, not relative",
                ((String) with(xml)
                        .get("DataSet.TaxonConcepts.TaxonConcept.TaxonRelationships.TaxonRelationship[0].ToTaxonConcept.@ref"))
                        .startsWith("http://"));
        assertTrue(
                "The links to external data should include the client id parameter",
                ((String) with(xml)
                        .get("DataSet.TaxonConcepts.TaxonConcept.TaxonRelationships.TaxonRelationship[0].ToTaxonConcept.@ref"))
                        .contains("&scratchpad=functional-test.e-monocot.org"));
    }

    /**
     * Tests the search operation handles a range of inputs.
     *
     */
    @Test
    public final void testSearch() {

        String searchName = "Lorem";
        // get nothing
        String xml = given()
                .parameters("function", "search", "search", "Misspelt name",
                        "scratchpad", "functional-test.e-monocot.org")
                .get("/endpoint").asString();
        assertNotNull("A results element was expected", with(xml)
                .get("results"));
        assertFalse(xml.contains("<value"));

        // get a record
        xml = given()
                .parameters("function", "search", "search", searchName,
                        "scratchpad", "functional-test.e-monocot.org")
                .get("/endpoint").asString();
        assertNotNull("A results element was expected", with(xml)
                .get("results"));
        assertNotNull("<value> element was expected",
                with(xml).get("results.value"));
        assertEquals("The name of the taxon should be included", "Lorem",
                with(xml).get("results.value[0].name"));
        assertEquals(
                "The canonical form of the name of the taxon should be included",
                "Lorem", with(xml).get("results.value[0].canonical_form"));
        assertEquals("The id of the taxon should be included",
                "urn:kew.org:wcs:taxon:3", with(xml).get("results.value[0].id"));
        assertEquals("The ancestry of the taxon should be included",
                "Lowiaceae|Lorem", with(xml).get("results.value[0].ancestry"));
        assertEquals("The ranked ancestry of the taxon should be included",
                "Lowiaceae|Lorem",
                with(xml).get("results.value[0].ranked_ancestry"));
        assertEquals("The rank of the taxon should be included", "Genus",
                with(xml).get("results.value[0].rank"));
        assertEquals("The number of children should be included", "2",
                with(xml).get("results.value[0].number_of_children"));
        assertEquals("The number of synonyms should be included", "0",
                with(xml).get("results.value[0].number_of_children_synonyms"));
        assertEquals("The title of the service should be included",
                "World Checklist System: Taxon Extractor Service", with(xml)
                        .get("results.value[0].metadata.title"));
        assertEquals(
                "The title of the service should be included",
                "This Checklist gives information on the accepted scientific names and synonyms of selected plant families.",
                with(xml).get("results.value[0].metadata.description"));
        assertEquals("The title of the service should be included",
                "http://apps.kew.org/wcsp/home.do",
                with(xml).get("results.value[0].metadata.url"));
    }

    /**
     * Tests the search operation handles a range of inputs.
     *
     */
    @Test
    public final void testSearchFamily() {

        String searchName = "Lowiaceae";
        // get nothing
        String xml = given()
                .parameters("function", "search", "search", "Misspelt name",
                        "scratchpad", "functional-test.e-monocot.org")
                .get("/endpoint").asString();
        assertNotNull("A results element was expected", with(xml)
                .get("results"));
        assertFalse(xml.contains("<value"));

        // get a record
        xml = given()
                .parameters("function", "search", "search", searchName,
                        "scratchpad", "functional-test.e-monocot.org")
                .get("/endpoint").asString();
        assertNotNull("A results element was expected", with(xml)
                .get("results"));
        assertNotNull("<value> element was expected",
                with(xml).get("results.value"));
        assertEquals("The name of the taxon should be included", "Lowiaceae",
                with(xml).get("results.value.name"));
        assertEquals(
                "The canonical form of the name of the taxon should be included",
                "Lowiaceae", with(xml).get("results.value.canonical_form"));
        assertEquals("The id of the taxon should be included",
                "urn:kew.org:wcs:family:28", with(xml).get("results.value.id"));
        assertEquals("The ancestry of the taxon should be included",
                "Lowiaceae", with(xml).get("results.value.ancestry"));
        assertEquals("The ranked ancestry of the taxon should be included",
                "Lowiaceae", with(xml).get("results.value.ranked_ancestry"));
        assertEquals("The rank of the taxon should be included", "Family",
                with(xml).get("results.value.rank"));
        assertEquals("The number of children should be included", "1",
                with(xml).get("results.value.number_of_children"));
        assertEquals("The number of synonyms should be included", "0",
                with(xml).get("results.value.number_of_children_synonyms"));
        assertEquals("The title of the service should be included",
                "World Checklist System: Taxon Extractor Service", with(xml)
                        .get("results.value.metadata.title"));
        assertEquals(
                "The title of the service should be included",
                "This Checklist gives information on the accepted scientific names and synonyms of selected plant families.",
                with(xml).get("results.value.metadata.description"));
        assertEquals("The title of the service should be included",
                "http://apps.kew.org/wcsp/home.do",
                with(xml).get("results.value.metadata.url"));
    }

    /**
     *
     */
    @After
    public final void tearDown() {
        testDataManager.tearDown();
    }
}
