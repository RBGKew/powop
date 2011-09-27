package org.emonocot.model.marshall.json;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertTrue;
import static org.junit.Assert.fail;

import org.codehaus.jackson.map.ObjectMapper;
import org.easymock.EasyMock;
import org.emonocot.model.description.Feature;
import org.emonocot.model.description.TextContent;
import org.emonocot.model.media.Image;
import org.emonocot.model.reference.Reference;
import org.emonocot.model.taxon.Taxon;
import org.emonocot.service.ImageService;
import org.emonocot.service.ReferenceService;
import org.emonocot.service.TaxonService;
import org.junit.Before;
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
    private ObjectMapper objectMapper;

    /**
     *
     */
    private ReferenceService referenceService;

    /**
     *
     */
    private TaxonService taxonService;

    /**
     *
     */
    private ImageService imageService;

    /**
     *
     */
    @Before
    public final void setUp() {
        referenceService = EasyMock.createMock(ReferenceService.class);
        taxonService = EasyMock.createMock(TaxonService.class);
        imageService = EasyMock.createMock(ImageService.class);
        CustomObjectMapperFactory objectMapperFactory
            = new CustomObjectMapperFactory();
        objectMapperFactory.setReferenceService(referenceService);
        objectMapperFactory.setTaxonService(taxonService);
        objectMapperFactory.setImageService(imageService);
        objectMapper = objectMapperFactory.getObject();
    }

    /**
     *
     * @throws Exception
     *             if there is a problem serializing the object
     */
    @Test
    public final void testReadTaxon() throws Exception {
        Reference reference = new Reference();
        Image image = new Image();
        EasyMock.expect(
                referenceService.load(EasyMock
                        .eq("urn:kew.org:wcs:publication:1"))).andReturn(
                reference);
        EasyMock.expect(imageService.load("urn:http:upload.wikimedia.org:wikipedia.commons.2.25:Illustration_Acorus_calamus0.jpg")).andReturn(image);
        EasyMock.replay(referenceService, imageService);

        String content = "{\"identifier\":\"urn:kew.org:wcs:taxon:2295\",\"name\":\"Acorus\",\"protologue\":\"urn:kew.org:wcs:publication:1\", \"content\": [{\"feature\":\"habitat\",\"content\":\"Lorem ipsum\"}], \"images\":[\"urn:http:upload.wikimedia.org:wikipedia.commons.2.25:Illustration_Acorus_calamus0.jpg\"]}";
        Taxon taxon = (Taxon) objectMapper.readValue(content, Taxon.class);
        EasyMock.verify(referenceService, imageService);

        assertNotNull("Returned object should not be null", taxon);
        assertEquals("The identifier should be \"urn:kew.org:wcs:taxon:2295\"",
                "urn:kew.org:wcs:taxon:2295", taxon.getIdentifier());
        assertEquals("The name should be \"Acorus\"", "Acorus", taxon.getName());
        assertFalse("There should be some content", taxon.getContent()
                .isEmpty());
        assertTrue("There should information on habitat", taxon.getContent()
                .containsKey(Feature.habitat));
        assertEquals("The habitat should be 'Lorem ipsum'",
                ((TextContent) taxon.getContent().get(Feature.habitat))
                        .getContent(), "Lorem ipsum");
        assertEquals("The taxon should be set on the content", taxon
                .getContent().get(Feature.habitat).getTaxon(), taxon);
        assertEquals("The protologue should be set", reference,
                taxon.getProtologue());
        assertTrue("The taxon should contain the image", taxon.getImages()
                .contains(image));

    }

    /**
     *
     * @throws Exception
     *             if there is a problem serializing the object
     */
    @Test
    public final void testWriteTaxon() throws Exception {
        String content = "{\"identifier\":\"urn:kew.org:wcs:taxon:2295\",\"name\":\"Acorus\",\"protologue\":\"urn:kew.org:wcs:publication:1\"}";
        Taxon taxon = new Taxon();
        taxon.setIdentifier("urn:kew.org:wcs:taxon:2295");
        taxon.setName("Acorus");
        TextContent textContent = new TextContent();
        textContent.setContent("Lorem ipsum");
        textContent.setFeature(Feature.habitat);
        textContent.setTaxon(taxon);
        taxon.getContent().put(Feature.habitat, textContent);
        Reference reference = new Reference();
        reference.setIdentifier("urn:kew.org:wcs:publication:1");
        taxon.setProtologue(reference);

        try {
            objectMapper.writeValueAsString(taxon);
        } catch (Exception e) {
            fail(e.getMessage());
        }

    }

    /**
     *
     * @throws Exception
     *             if there is a problem serializing the object
     */
    @Test
    public final void testWriteImage() throws Exception {
        Taxon taxon = new Taxon();
        taxon.setIdentifier("urn:kew.org:wcs:taxon:2295");
        String content = "{\"identifier\":\"urn:http:upload.wikimedia.org:wikipedia.commons.2.25:Illustration_Acorus_calamus0.jpg\",\"caption\":\"Acorus\",\"taxa\":[\"urn:kew.org:wcs:taxon:2295\"]}";
        Image image = new Image();
        image.setIdentifier("urn:http:upload.wikimedia.org:wikipedia.commons.2.25:Illustration_Acorus_calamus0.jpg");
        image.setCaption("Acorus");
        image.getTaxa().add(taxon);

        try {
            objectMapper.writeValueAsString(image);
        } catch (Exception e) {
            fail(e.getMessage());
        }
    }

    /**
     *
     * @throws Exception
     *             if there is a problem serializing the object
     */
    @Test
    public final void testReadImage() throws Exception {
        Taxon taxon = new Taxon();
        EasyMock.expect(
                taxonService.load(EasyMock.eq("urn:kew.org:wcs:taxon:2295"),
                        EasyMock.eq("taxon-page"))).andReturn(taxon);
        EasyMock.replay(referenceService, taxonService);

        String content = "{\"identifier\":\"urn:http:upload.wikimedia.org:wikipedia.commons.2.25:Illustration_Acorus_calamus0.jpg\",\"caption\":\"Acorus\",\"taxa\":[\"urn:kew.org:wcs:taxon:2295\"]}";
        Image image = (Image) objectMapper.readValue(content, Image.class);
        EasyMock.verify(referenceService, taxonService);

        assertNotNull("Returned object should not be null", image);
        assertEquals(
                "The identifier should be \"urn:http:upload.wikimedia.org:wikipedia.commons.2.25:Illustration_Acorus_calamus0.jpg\"",
                "urn:http:upload.wikimedia.org:wikipedia.commons.2.25:Illustration_Acorus_calamus0.jpg",
                image.getIdentifier());
        assertEquals("The caption should be \"Acorus\"", "Acorus",
                image.getCaption());
        assertTrue("The taxon should be set on the image", image.getTaxa()
                .contains(taxon));
    }

    /**
    *
    * @throws Exception
    *             if there is a problem serializing the object
    */
   @Test
   public final void testReadReference() throws Exception {

       EasyMock.replay(referenceService, imageService);

       String content = "{\"identifier\":\"urn:kew.org:wcs:publication:123\",\"title\":\"Lorem ipsum\"}";
        Reference reference = (Reference) objectMapper.readValue(content,
                Reference.class);
        EasyMock.verify(referenceService, imageService);

        assertNotNull("Returned object should not be null", reference);
        assertEquals(
                "The identifier should be \"urn:kew.org:wcs:publication:123\"",
                "urn:kew.org:wcs:publication:123", reference.getIdentifier());
        assertEquals("The title should be \"Lorem ipsum\"", "Lorem ipsum",
                reference.getTitle());
     }
}
