package org.emonocot.job.dwc.media;

import static org.junit.Assert.assertEquals;

import java.io.File;
import java.util.Properties;

import org.emonocot.harvest.common.GetResourceClient;
import org.emonocot.harvest.media.ImageFileProcessor;
import org.emonocot.harvest.media.ImageMetadataExtractor;
import org.emonocot.harvest.media.ImageThumbnailGenerator;
import org.emonocot.model.Image;
import org.emonocot.model.constants.ImageFormat;
import org.junit.Before;
import org.junit.Test;
import org.springframework.core.io.ClassPathResource;
import org.springframework.core.io.Resource;

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
    private ImageThumbnailGenerator imageThumbnailGenerator = new ImageThumbnailGenerator();

    /**
     *
     */
    private ImageMetadataExtractor imageMetadataExtractor = new ImageMetadataExtractor();

    /**
     *
     */
    private Image image = new Image();

    /**
     * @throws Exception
     *             if there is a problem
     */
    @Before
    public final void setUp() throws Exception {
        image.setIdentifier("http://build.e-monocot.org/test/test.jpg");
        image.setFormat(ImageFormat.jpg);
        GetResourceClient getResourceClient = new GetResourceClient();
        imageFileProcessor.setGetResourceClient(getResourceClient);
        String imagesDirectoryName = System.getProperty("java.io.tmpdir")
                + File.separatorChar + "images";
        File imagesDirectory = new File(imagesDirectoryName);
        imagesDirectory.mkdir();
        imagesDirectory.deleteOnExit();
        imageMetadataExtractor.setImageDirectory(imagesDirectoryName);
        imageFileProcessor.setImageDirectory(imagesDirectoryName);
        String thumbnailDirectoryName = System.getProperty("java.io.tmpdir")
                + File.separatorChar + "thumbnails";
        File thumbnailDirectory = new File(thumbnailDirectoryName);
        thumbnailDirectory.mkdir();
        thumbnailDirectory.deleteOnExit();

        Resource propertiesFile = new ClassPathResource(
                "/META-INF/spring/application.properties");
        Properties properties = new Properties();
        properties.load(propertiesFile.getInputStream());

        imageThumbnailGenerator.setImageDirectory(imagesDirectoryName);
        imageThumbnailGenerator.setThumbnailDirectory(thumbnailDirectoryName);
        imageThumbnailGenerator.setImageMagickSearchPath(properties.getProperty(
                "harvester.imagemagick.path", "/Program Files/ImageMagick"));
        getResourceClient.setProxyHost(properties.getProperty("http.proxyHost",
                null));
        getResourceClient.setProxyPort(properties.getProperty("http.proxyPort",
                null));
    }


    /**
     * @throws Exception
     *             if there is a problem accessing the file
     */
    @Test
    public final void testProcess() throws Exception {
        Image i = imageFileProcessor.process(image);
        imageMetadataExtractor.process(i);
        assertEquals("Arecaceae; Howea forsteriana", i.getTitle());
        // assertEquals("Male inflorescences", i.getDescription());
        // assertEquals("William J. Baker", i.getCreator());
        assertEquals(
                "ARECOIDEAE, Arecaceae, Areceae, Howea, Linospadicinae, Palmae, Palms, flowers, inflorescences",
                i.getSubject());
        assertEquals("Path to Little island, Lord Howe Island, Australia",
                i.getSpatial());
    }

}
