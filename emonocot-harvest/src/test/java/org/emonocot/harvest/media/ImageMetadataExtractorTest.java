/**
 * 
 */
package org.emonocot.harvest.media;

import static org.junit.Assert.assertEquals;

import java.util.HashSet;
import java.util.Set;

import javax.validation.ConstraintViolation;
import javax.validation.Validator;

import org.easymock.EasyMock;
import org.emonocot.harvest.common.HtmlSanitizer;
import org.emonocot.model.Image;
import org.emonocot.model.constants.MediaFormat;
import org.joda.time.DateTime;
import org.joda.time.DateTimeZone;
import org.junit.Before;
import org.junit.Test;

/**
 * @author ben
 *
 */
public class ImageMetadataExtractorTest {
    
    private static final String TEST_IMAGE_DIRECTORY = "target/test-classes/org/emonocot/job/media";
    
    private ImageMetadataExtractorImpl metadataExtractor;
    
    private Validator validator;
    
    private ImageAnnotator imageAnnotator;
    
    private Image image;
    
    private Set<ConstraintViolation<Image>> constraintViolations;

    /**
     * @throws java.lang.Exception
     */
    @Before
    public void setUp() throws Exception {
    	constraintViolations = new HashSet<ConstraintViolation<Image>>();
    	validator = EasyMock.createMock(Validator.class);
    	metadataExtractor = new ImageMetadataExtractorImpl();
    	metadataExtractor.setImageDirectory(TEST_IMAGE_DIRECTORY);
    	imageAnnotator = EasyMock.createMock(ImageAnnotator.class);
    	HtmlSanitizer htmlSanitizer = new HtmlSanitizer();
    	htmlSanitizer.afterPropertiesSet();
    	metadataExtractor.setSanitizer(htmlSanitizer);
    	metadataExtractor.setValidator(validator);
    	metadataExtractor.setImageAnnotator(imageAnnotator);
    	
    	image = new Image();
        image.setId(2L);
        image.setFormat(MediaFormat.jpg);
    }

    @Test
    public final void testExtactMetadata() throws Exception {
        EasyMock.expect(validator.validate(EasyMock.isA(Image.class))).andReturn(constraintViolations);
        EasyMock.replay(validator,imageAnnotator);
        metadataExtractor.process(image);
        assertEquals(image.getRights(),"Jean-Christophe Pintaud");
        assertEquals(image.getTitle(), "Arecaceae; Actinokentia huerlimannii");
        EasyMock.verify(validator,imageAnnotator);           
    }
    
    @Test
    public final void testExtactMetadata2() throws Exception {
    	image.setId(3L);
        EasyMock.expect(validator.validate(EasyMock.isA(Image.class))).andReturn(constraintViolations);
        EasyMock.replay(validator,imageAnnotator);
        metadataExtractor.process(image);
        assertEquals(image.getRights(),"SD Barfoot, RBG Kew");
        assertEquals(image.getTitle(), "<i>Agapanthus inapertus</i> subsp. <i>intermedius</i> (Amaryllidaceae)");
        //assertEquals(image.getCreated(), new DateTime(1979,9,11, 0, 0, 0, 0, DateTimeZone.getDefault()));
        EasyMock.verify(validator,imageAnnotator);           
    }
}
