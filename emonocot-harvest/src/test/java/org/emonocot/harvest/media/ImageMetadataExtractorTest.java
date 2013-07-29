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
    	metadataExtractor.setSanitizer(new HtmlSanitizer());
    	metadataExtractor.setValidator(validator);
    	
    	image = new Image();
        image.setId(2L);
        image.setFormat(MediaFormat.jpg);
    }

    @Test
    public final void testExtactMetadata() throws Exception {
        EasyMock.expect(validator.validate(EasyMock.isA(Image.class))).andReturn(constraintViolations);
        EasyMock.replay(validator);
        metadataExtractor.process(image);
        assertEquals(image.getRights(),"Jean-Christophe Pintaud");
        assertEquals(image.getTitle(), "Arecaceae; Actinokentia huerlimannii");
        EasyMock.verify(validator);           
    }
    
}
