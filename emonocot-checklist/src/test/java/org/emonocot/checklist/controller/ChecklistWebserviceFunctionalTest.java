package org.emonocot.checklist.controller;

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
public class ChecklistWebserviceFunctionalTest {

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
                "http://129.67.24.160");
        RestAssured.port = Integer.parseInt(
                properties.getProperty("functional.test.port",
                "80"));
        RestAssured.basePath = properties.getProperty(
                "functional.test.basePath",
                "/latest/checklist");
    }

    /**
     * Tests that the PING request returns the response
     * "200 OK".
     *
     * @throws Exception
     *             if there is a problem with the test
     */
    @Test
    public final void testPing() throws Exception {
        expect().statusCode(HttpStatus.OK.value()).get("/endpoint");
    }

   /**
    * Tests the GET response returns a valid document containing
    * a taxon of species rank.
    * @throws Exception
    *             if there is a problem with the test
    */
   @Test
   public final void testGet() throws Exception {
       String xml = given().parameters("function", "details_tcs",
               "id", "urn:kew.org:wcs:taxon:1")
               .get("/endpoint").andReturn().body().asString();
       assertEquals("TaxonName id should equal 'urn:kew.org:wcs:name:1'",
               "urn:kew.org:wcs:name:1",
               with(xml).get(
                       "DataSet.TaxonNames.TaxonName.@id"));
       assertEquals("Nomenclatural code should equal 'Botanical'",
               "Botanical",
               with(xml).get(
                       "DataSet.TaxonNames.TaxonName.@nomenclaturalCode"));
       assertEquals("Simple should equal 'Lorem ipsum'",
               "Lorem ipsum",
               with(xml).get(
                       "DataSet.TaxonNames.TaxonName.Simple"));
       assertEquals("Rank should equal 'Species'",
               "Species",
               with(xml).get(
                       "DataSet.TaxonNames.TaxonName.Rank"));
       assertEquals("Rank code should equal 'sp'",
               "sp",
               with(xml).get(
                       "DataSet.TaxonNames.TaxonName.Rank.@code"));
       assertEquals("CanonicalName.Simple should equal 'Lorem ipsum'",
               "Lorem ipsum",
               with(xml).get(
                       "DataSet.TaxonNames.TaxonName.CanonicalName.Simple"));
       assertEquals("TaxonConcept.Name scientific should equal 'true'",
               "true",
               with(xml).get(
                       "DataSet.TaxonConcepts.TaxonConcept.Name.@scientific"));
       assertEquals(
               "TaxonConcept.Name ref should equal 'urn:kew.org:wcs:name:1'",
               "urn:kew.org:wcs:name:1",
               with(xml).get(
                       "DataSet.TaxonConcepts.TaxonConcept.Name.@ref"));
       assertEquals(
               "TaxonConcept.Name should equal 'Lorem ipsum'",
               "Lorem ipsum",
               with(xml).get(
                       "DataSet.TaxonConcepts.TaxonConcept.Name"));
       assertEquals("TaxonConcept.Rank should equal 'Species'",
               "Species",
               with(xml).get(
                       "DataSet.TaxonConcepts.TaxonConcept.Rank"));
       assertEquals("TaxonConcept.Rank code should equal 'sp'",
               "sp",
               with(xml).get(
                       "DataSet.TaxonConcepts.TaxonConcept.Rank.@code"));
   }
}
