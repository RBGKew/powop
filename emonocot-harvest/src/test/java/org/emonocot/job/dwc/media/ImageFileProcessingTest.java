package org.emonocot.job.dwc.media;

import java.io.File;
import java.util.UUID;

import org.emonocot.job.dwc.image.ImageFileProcessor;
import org.emonocot.model.media.Image;
import org.emonocot.ws.GetResourceClient;
import org.joda.time.DateTime;
import org.joda.time.base.BaseDateTime;
import org.junit.Before;
import org.junit.Test;
import org.springframework.core.io.FileSystemResource;

/**
 *
 * @author ben
 *
 */
public class ImageFileProcessingTest {

   /**
    *
    */
    private ImageFileProcessor imageFileProcessor = new ImageFileProcessor();
    
    /**
     *
     */
    private Image image = new Image();

   /**
    *
    */
   @Before
   public final void setUp() throws Exception {
        image.setUrl("http://build.e-monocot.org/test/test.jpg");
        image.setIdentifier(UUID.randomUUID().toString());
        image.setFormat("jpg");
        imageFileProcessor.setAuthorityName("test.authority");
        GetResourceClient getResourceClient = new GetResourceClient();
        imageFileProcessor.setGetResourceClient(getResourceClient);
        FileSystemResource imagesDirectory = new FileSystemResource(System.getProperty("java.io.tmpdir") + File.separatorChar + "images");
        imageFileProcessor.setImageDirectory(imagesDirectory);
        FileSystemResource thumbnailDirectory = new FileSystemResource(System.getProperty("java.io.tmpdir") + File.separatorChar + "thumbnails");
        imageFileProcessor.setThumbnailDirectory(thumbnailDirectory);
   }

    /**
     * @throws Exception if there is a problem accessing the file
     */
    @Test
    public final void testProcess() throws Exception {
        Image i = imageFileProcessor.process(image);
        System.out.println(i.getCaption());
        System.out.println(i.getDescription());
        System.out.println(i.getCreator());
        System.out.println(i.getKeywords());
        System.out.println(i.getSpatial());
        System.out.println(i.getLocation());

    }

}
