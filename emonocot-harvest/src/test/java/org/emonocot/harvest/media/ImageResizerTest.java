/**
 * 
 */
package org.emonocot.harvest.media;

import static org.junit.Assert.assertTrue;
import static org.junit.Assert.fail;

import java.io.File;
import java.util.Properties;

import org.apache.sanselan.ImageInfo;
import org.apache.sanselan.Sanselan;
import org.emonocot.model.Image;
import org.emonocot.model.constants.MediaFormat;
import org.junit.After;
import org.junit.Before;
import org.junit.Test;
import org.springframework.core.io.ClassPathResource;
import org.springframework.core.io.Resource;

/**
 * @author jk00kg
 *
 */
public class ImageResizerTest {
    
    /**
     * 
     */
    private static final String TEST_IMAGE_DIRECTORY = "target/test-classes/org/emonocot/job/media";
    
    /**
     * 
     */
    private ImageResizerImpl resizer;

    /**
     * @throws java.lang.Exception
     */
    @Before
    public void setUp() throws Exception {
        resizer = new ImageResizerImpl();
        resizer.setImageDirectory(TEST_IMAGE_DIRECTORY);
        Resource propertiesFile = new ClassPathResource("META-INF/spring/application.properties");
        Properties properties = new Properties();
        properties.load(propertiesFile.getInputStream());
        String imageMagickSearchPath = properties.getProperty("harvester.imagemagick.path", "/usr/bin");
        resizer.setImageMagickSearchPath(imageMagickSearchPath);
        
    }

    /**
     * @throws java.lang.Exception
     */
    @After
    public void tearDown() throws Exception {
    }

    @Test
    public final void testNoResize() {
        //Set up
        long imageNumber = 0L;
        Image image = new Image();
        image.setId(imageNumber);
        image.setFormat(MediaFormat.jpg);
        ImageInfo info = null;
        Integer originalHeight = null;
        Integer originalWidth = null;
        try {
            info = Sanselan.getImageInfo(new File(TEST_IMAGE_DIRECTORY + File.separator + imageNumber + ".jpg"));
            originalHeight = new Integer(info.getHeight());
            originalWidth = new Integer(info.getWidth());
        } catch (Exception e) {
            e.printStackTrace();
            fail("Unable to read original image");
        }
        //Run resize
        try {
            resizer.process(image);
            info = Sanselan.getImageInfo(new File(TEST_IMAGE_DIRECTORY + File.separator + imageNumber + ".jpg"));
            assertTrue("The image height should be unchanged", info.getHeight() == originalHeight.intValue());
            assertTrue("The image width should be unchanged", info.getWidth() == originalWidth.intValue());
        } catch (Exception e) {
            e.printStackTrace();
            fail(e.getMessage());
        }
    }
    
    @Test
    public final void testResize() {
        //Set up
        long imageNumber = 1L;
        Image image = new Image();
        image.setId(imageNumber);
        image.setFormat(MediaFormat.jpg);
        ImageInfo info = null;
        Integer originalHeight = null;
        Integer originalWidth = null;
        try {
            info = Sanselan.getImageInfo(new File(TEST_IMAGE_DIRECTORY + File.separator + imageNumber + ".jpg"));
            originalHeight = new Integer(info.getHeight());
            originalWidth = new Integer(info.getWidth());
            assertTrue("The dimensions of the original image do not meet the requirements for this test", originalHeight > 1000 && originalWidth > 1000);
        } catch (Exception e) {
            e.printStackTrace();
            fail("Unable to read original image");
        }
        //Run resize
        try {
            resizer.process(image);
            info = Sanselan.getImageInfo(new File(TEST_IMAGE_DIRECTORY + File.separator + imageNumber + ".jpg"));
            assertTrue("The image height should have been reduced to no more than 1000px", info.getHeight() <= 1000);
            assertTrue("The image width should have been reduced to no more than 1000px", info.getWidth() <= 1000);
        } catch (Exception e) {
            e.printStackTrace();
            fail(e.getMessage());
        }
    }
    
}
