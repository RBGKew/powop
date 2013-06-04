/**
 * 
 */
package org.emonocot.harvest.media;

import static org.junit.Assert.*;

import java.io.File;

import org.apache.sanselan.ImageInfo;
import org.apache.sanselan.Sanselan;
import org.emonocot.model.Image;
import org.emonocot.model.constants.ImageFormat;
import org.junit.After;
import org.junit.Before;
import org.junit.Test;

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
    private ImageResizer resizer;

    /**
     * @throws java.lang.Exception
     */
    @Before
    public void setUp() throws Exception {
        resizer = new ImageResizer();
        resizer.setImageDirectory(TEST_IMAGE_DIRECTORY);
        String imHome = System.getenv("MAGICK_HOME");
        if(imHome != null) {
            resizer.setImageMagickSearchPath(imHome);
        } else {
            resizer.setImageMagickSearchPath("/usr/bin");
        }
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
        image.setFormat(ImageFormat.jpg);
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
        image.setFormat(ImageFormat.jpg);
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
