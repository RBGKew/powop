package org.emonocot.checklist.controller;

import static com.jayway.restassured.RestAssured.expect;
import static org.hamcrest.xml.HasXPath.hasXPath;

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
    * Tests the GET response returns a valid document.
    * @throws Exception
    *             if there is a problem with the test
    */
   @Test
   public final void testGet() throws Exception {
       expect()
         .body(hasXPath("/DataSet"))
         .given()
           .parameters("function", "details_tcs",
                       "id", "urn:kew.org:wcs:taxon:1")
           .get("/endpoint");
   }
}
