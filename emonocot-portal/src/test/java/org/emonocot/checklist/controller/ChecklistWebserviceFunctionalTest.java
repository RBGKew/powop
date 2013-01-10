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
@ContextConfiguration("classpath:META-INF/spring/applicationContext-functionalTest.xml")
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
                "META-INF/spring/application.properties");
        properties = new Properties();
        properties.load(propertiesFile.getInputStream());
        RestAssured.baseURI = properties.getProperty("functional.test.baseUri",
                "http://build.e-monocot.org");
        RestAssured.port = Integer.parseInt(properties.getProperty(
                "functional.test.port", "80"));
        RestAssured.basePath = properties.getProperty(
                "functional.test.basePath", "/latest/checklist");

        testDataManager
                .createReference(
                        "reference1",
                        "Integer elementum lorem ut nibh scelerisque at condimentum",
                        "Pargetter & Archer",
                        "1784",
                        "2",
                        "250pp",
                        "Pargetter & Archer, Integer elementum lorem ut nibh scelerisque at condimentum 2: 250pp 1784",
                        null, null);
        testDataManager.createTaxon("urn:kew.org:wcs:family:28", "Lowiaceae", "Archer",
                null, null, null, "FAMILY", "Accepted", null, null,
                null, null, null, null, null, null, null,
                null, null, null, null, null, 
                null, null, null, null, null, null, null, null, null);
        testDataManager.createTaxon("urn:kew.org:wcs:taxon:3", "Lorem", null,
                "Lowiaceae", "Lorem", null, "GENUS", "Accepted", null, null,
                null, null, null, null, null, null, null,
                null, null, null, "urn:kew.org:wcs:family:28", null, 
                null, null, null, null, null, null, null, null, null);
        testDataManager.createTaxon("urn:kew.org:wcs:taxon:1", "Lorem ipsum",
                "(Archer & Archer) Pargetter", "Lowiaceae", "Lorem", "ipsum",
                "SPECIES", "Accepted", null, null, null, null, "(Archer & Archer) Pargetter in Pargetter & Archer, Integer elementum lorem ut nibh scelerisque at condimentum 2: 34-56 1784", "2: 34-56",
                null, "DEN", "FIN", "FOR", null, null,
                "urn:kew.org:wcs:taxon:3", null, 
                null, null, null, null, null, null, null, null, null);
        testDataManager.createTaxon("urn:kew.org:wcs:taxon:2", "Lorem dolor",
                null, "Lowiaceae", "Lorem", "dolor", "SPECIES", "Accepted",
                null, null, null, null, null, null, null,
                null, null, null, null, null, "urn:kew.org:wcs:taxon:3", null, 
                null, null, null, null, null, null, null, null, null);
        testDataManager.createTaxon("urn:kew.org:wcs:taxon:4",
                "Lorem consectetur", null, "Lowiaceae", "Lorem", "consectetur",
                "SPECIES", "Synonym", null, null, null, null,
                null, null, null, null, null, null, null, null, null,
                "urn:kew.org:wcs:taxon:1", 
                null, null, null, null, null, null, null, null, null);
        testDataManager.createTaxon("urn:kew.org:wcs:taxon:5",
                "Lorem adipiscing", null, "Lowiaceae", "Lorem", "adipiscing",
                "SPECIES", "Synonym", null, null, null, null,
                null, null, null, null, null, null, null, null, null,
                "urn:kew.org:wcs:taxon:1", 
                null, null, null, null, null, null, null, null, null);
        testDataManager.createTaxon("urn:kew.org:wcs:taxon:6",
                "Curabitur vehicula", null, "Curabituraceae", "Curabitur",
                "vehicula", "SPECIES", "Accepted", null,
                null, null, null, null, null, null, null, null, null, null,
                null, null, null, 
                null, null, null, null, null, null, null, null, null);
        testDataManager.createTaxon("urn:kew.org:wcs:taxon:7",
                "Curabitur nulla", null, "Curabituraceae", "Curabitur",
                "nulla", "SPECIES", "Doubtful", null, null,
                null, null, null, null, null, null, null, null, null, null,
                null, null, 
                null, null, null, null, null, null, null, null, null);
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
        assertEquals("TaxonName id should equal TaxonConcept.Name ref",
        		with(xml).get("DataSet.TaxonConcepts.TaxonConcept.Name.@ref"),
                with(xml).get("DataSet.TaxonNames.TaxonName.@id"));
        assertEquals("Nomenclatural code should equal 'Botanical'",
                "Botanical",
                with(xml)
                        .get("DataSet.TaxonNames.TaxonName.@nomenclaturalCode"));
        assertEquals("Simple should equal 'Lorem ipsum (Archer & Archer) Pargetter'", "Lorem ipsum (Archer & Archer) Pargetter",
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
        assertEquals("TaxonConcept.Name should equal 'Lorem ipsum (Archer & Archer) Pargetter'",
                "Lorem ipsum (Archer & Archer) Pargetter",
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
        assertEquals("TaxonName publication attribute should equal '(Archer & Archer) Pargetter in Pargetter & Archer, Integer elementum lorem ut nibh scelerisque at condimentum 2: 34-56 1784'",
                "(Archer & Archer) Pargetter in Pargetter & Archer, Integer elementum lorem ut nibh scelerisque at condimentum 2: 34-56 1784",
                with(xml).get(
                        "DataSet.TaxonNames.TaxonName.@itis_em_other_ref"));
        /**
         * ISSUE http://build.e-monocot.org/bugzilla/show_bug.cgi?id=180
         *
        assertEquals(
                "Three distribution records should be present",
                3,
                with(xml)
                        .get("DataSet.TaxonConcepts.TaxonConcept.ProviderSpecificData.Distribution.size()"));*/
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
                "TaxonName id should equal TaxonConcept.Name ref",
                with(xml).get("DataSet.TaxonConcepts.TaxonConcept.Name.@ref"),
                with(xml).get("DataSet.TaxonNames.TaxonName.@id"));
        assertEquals("Nomenclatural code should equal 'Botanical'",
                "Botanical",
                with(xml)
                        .get("DataSet.TaxonNames.TaxonName.@nomenclaturalCode"));
        assertEquals("Simple should equal 'Lowiaceae Archer'", "Lowiaceae Archer", with(xml)
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
        assertEquals("TaxonConcept.Name should equal 'Lowiaceae Archer'", "Lowiaceae Archer",
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
@ContextConfiguration("classpath:META-INF/spring/applicationContext-functionalTest.xml")
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
                "META-INF/spring/application.properties");
        properties = new Properties();
        properties.load(propertiesFile.getInputStream());
        RestAssured.baseURI = properties.getProperty("functional.test.baseUri",
                "http://build.e-monocot.org");
        RestAssured.port = Integer.parseInt(properties.getProperty(
                "functional.test.port", "80"));
        RestAssured.basePath = properties.getProperty(
                "functional.test.basePath", "/latest/checklist");

        testDataManager
                .createReference(
                        "reference1",
                        "Integer elementum lorem ut nibh scelerisque at condimentum",
                        "Pargetter & Archer",
                        "1784",
                        "2",
                        "250pp",
                        "Pargetter & Archer, Integer elementum lorem ut nibh scelerisque at condimentum 2: 250pp 1784",
                        null, null);
        testDataManager.createTaxon("urn:kew.org:wcs:family:28", "Lowiaceae", "Archer",
                null, null, null, "FAMILY", "Accepted", null, null,
                null, null, null, null, null, null, null,
                null, null, null, null, null, 
                null, null, null, null, null, null, null, null, null);
        testDataManager.createTaxon("urn:kew.org:wcs:taxon:3", "Lorem", null,
                "Lowiaceae", "Lorem", null, "GENUS", "Accepted", null, null,
                null, null, null, null, null, null, null,
                null, null, null, "urn:kew.org:wcs:family:28", null, 
                null, null, null, null, null, null, null, null, null);
        testDataManager.createTaxon("urn:kew.org:wcs:taxon:1", "Lorem ipsum",
                "(Archer & Archer) Pargetter", "Lowiaceae", "Lorem", "ipsum",
                "SPECIES", "Accepted", null, null, null, null, "(Archer & Archer) Pargetter in Pargetter & Archer, Integer elementum lorem ut nibh scelerisque at condimentum 2: 34-56 1784", "2: 34-56",
                null, "DEN", "FIN", "FOR", null, null,
                "urn:kew.org:wcs:taxon:3", null, 
                null, null, null, null, null, null, null, null, null);
        testDataManager.createTaxon("urn:kew.org:wcs:taxon:2", "Lorem dolor",
                null, "Lowiaceae", "Lorem", "dolor", "SPECIES", "Accepted",
                null, null, null, null, null, null, null,
                null, null, null, null, null, "urn:kew.org:wcs:taxon:3", null, 
                null, null, null, null, null, null, null, null, null);
        testDataManager.createTaxon("urn:kew.org:wcs:taxon:4",
                "Lorem consectetur", null, "Lowiaceae", "Lorem", "consectetur",
                "SPECIES", "Synonym", null, null, null, null,
                null, null, null, null, null, null, null, null, null,
                "urn:kew.org:wcs:taxon:1", 
                null, null, null, null, null, null, null, null, null);
        testDataManager.createTaxon("urn:kew.org:wcs:taxon:5",
                "Lorem adipiscing", null, "Lowiaceae", "Lorem", "adipiscing",
                "SPECIES", "Synonym", null, null, null, null,
                null, null, null, null, null, null, null, null, null,
                "urn:kew.org:wcs:taxon:1", 
                null, null, null, null, null, null, null, null, null);
        testDataManager.createTaxon("urn:kew.org:wcs:taxon:6",
                "Curabitur vehicula", null, "Curabituraceae", "Curabitur",
                "vehicula", "SPECIES", "Accepted", null,
                null, null, null, null, null, null, null, null, null, null,
                null, null, null, 
                null, null, null, null, null, null, null, null, null);
        testDataManager.createTaxon("urn:kew.org:wcs:taxon:7",
                "Curabitur nulla", null, "Curabituraceae", "Curabitur",
                "nulla", "SPECIES", "Doubtful", null, null,
                null, null, null, null, null, null, null, null, null, null,
                null, null, 
                null, null, null, null, null, null, null, null, null);
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
        assertEquals("TaxonName id should equal TaxonConcept.Name ref",
        		with(xml).get("DataSet.TaxonConcepts.TaxonConcept.Name.@ref"),
                with(xml).get("DataSet.TaxonNames.TaxonName.@id"));
        assertEquals("Nomenclatural code should equal 'Botanical'",
                "Botanical",
                with(xml)
                        .get("DataSet.TaxonNames.TaxonName.@nomenclaturalCode"));
        assertEquals("Simple should equal 'Lorem ipsum (Archer & Archer) Pargetter'", "Lorem ipsum (Archer & Archer) Pargetter",
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
        assertEquals("TaxonConcept.Name should equal 'Lorem ipsum (Archer & Archer) Pargetter'",
                "Lorem ipsum (Archer & Archer) Pargetter",
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
        assertEquals("TaxonName publication attribute should equal '(Archer & Archer) Pargetter in Pargetter & Archer, Integer elementum lorem ut nibh scelerisque at condimentum 2: 34-56 1784'",
                "(Archer & Archer) Pargetter in Pargetter & Archer, Integer elementum lorem ut nibh scelerisque at condimentum 2: 34-56 1784",
                with(xml).get(
                        "DataSet.TaxonNames.TaxonName.@itis_em_other_ref"));
        /**
         * ISSUE http://build.e-monocot.org/bugzilla/show_bug.cgi?id=180
         *
        assertEquals(
                "Three distribution records should be present",
                3,
                with(xml)
                        .get("DataSet.TaxonConcepts.TaxonConcept.ProviderSpecificData.Distribution.size()"));*/
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
                "TaxonName id should equal TaxonConcept.Name ref",
                with(xml).get("DataSet.TaxonConcepts.TaxonConcept.Name.@ref"),
                with(xml).get("DataSet.TaxonNames.TaxonName.@id"));
        assertEquals("Nomenclatural code should equal 'Botanical'",
                "Botanical",
                with(xml)
                        .get("DataSet.TaxonNames.TaxonName.@nomenclaturalCode"));
        assertEquals("Simple should equal 'Lowiaceae Archer'", "Lowiaceae Archer", with(xml)
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
        assertEquals("TaxonConcept.Name should equal 'Lowiaceae Archer'", "Lowiaceae Archer",
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
