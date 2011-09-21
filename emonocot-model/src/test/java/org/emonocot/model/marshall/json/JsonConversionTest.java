package org.emonocot.model.marshall.json;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.fail;

import org.codehaus.jackson.map.ObjectMapper;
import org.emonocot.model.media.Image;
import org.emonocot.model.taxon.Taxon;
import org.junit.Test;

/**
 *
 * @author ben
 *
 */
public class JsonConversionTest {

    /**
     *
     */
    private ObjectMapper objectMapper
        = new CustomObjectMapper();

    /**
     *
     * @throws Exception if there is a problem serializing the object
     */
    @Test
    public final void testReadTaxon() throws Exception {
        String content
            = "{\"identifier\":\"urn:kew.org:wcs:taxon:2295\",\"name\":\"Acorus\"}";
        Taxon taxon = (Taxon) objectMapper.readValue(content, Taxon.class);
        assertNotNull("Returned object should not be null", taxon);
        assertEquals("The identifier should be \"urn:kew.org:wcs:taxon:2295\"",
                "urn:kew.org:wcs:taxon:2295", taxon.getIdentifier());
        assertEquals("The name should be \"Acorus\"",
                "Acorus", taxon.getName());
    }

    /**
    *
    * @throws Exception if there is a problem serializing the object
    */
   @Test
   public final void testWriteTaxon() throws Exception {
       String content
           = "{\"identifier\":\"urn:kew.org:wcs:taxon:2295\",\"name\":\"Acorus\"}";
       Taxon taxon = new Taxon();
       taxon.setIdentifier("urn:kew.org:wcs:taxon:2295");
       taxon.setName("Acorus");

       try {
           objectMapper.writeValueAsString(taxon);
       } catch (Exception e) {
           fail();
       }

   }

   /**
   *
   * @throws Exception if there is a problem serializing the object
   */
  @Test
  public final void testWriteImage() throws Exception {
      String content
          = "{\"identifier\":\"urn:http:upload.wikimedia.org:wikipedia.commons.2.25:Illustration_Acorus_calamus0.jpg\",\"caption\":\"Acorus\"}";
      Image image = new Image();
      image.setIdentifier("urn:http:upload.wikimedia.org:wikipedia.commons.2.25:Illustration_Acorus_calamus0.jpg");
      image.setCaption("Acorus");

      try {
          objectMapper.writeValueAsString(image);
      } catch (Exception e) {
          fail();
      }

  }
}
