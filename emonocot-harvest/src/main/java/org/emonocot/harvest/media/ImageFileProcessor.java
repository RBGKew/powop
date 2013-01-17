package org.emonocot.harvest.media;

import java.io.File;

import org.apache.sanselan.ImageInfo;
import org.apache.sanselan.Sanselan;
import org.emonocot.harvest.common.GetResourceClient;
import org.emonocot.model.Image;
import org.im4java.core.ConvertCmd;
import org.im4java.core.IMOperation;
import org.im4java.core.MogrifyCmd;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.batch.item.ItemProcessor;

/**
 *
 * @author ben
 *
 */
public class ImageFileProcessor implements ItemProcessor<Image, Image> {

   /**
    *
    */
    private Logger logger = LoggerFactory.getLogger(ImageFileProcessor.class);

    /**
     *
     */
    private final Integer IMAGE_DIMENSION = 1000;

    /**
     *
     */
    private String imageDirectory;

    /**
     *
     */
    private GetResourceClient getResourceClient;

    /**
     *
     * @param newImageDirectory Set the image directory
     */
    public final void setImageDirectory(
            final String newImageDirectory) {
        this.imageDirectory = newImageDirectory;
    }

    /**
     *
     * @param newGetResourceClient set the get resource client
     */
    public final void setGetResourceClient(
            final GetResourceClient newGetResourceClient) {
        this.getResourceClient = newGetResourceClient;
    }

    /**
     * @param image Set the image
     * @return an image or null if the image is to be skipped
     * @throws Exception if there is a problem processing the image
     */
    public final Image process(final Image image) throws Exception {
        String imageFileName = imageDirectory + File.separatorChar  + image.getId() + '.' + image.getFormat();
        File file = new File(imageFileName);
        logger.debug("Image File " + imageFileName);
        if (file.exists()) {
            logger.info("File exists in image directory, skipping");
        } else {
            try {
                getResourceClient.getBinaryResource("",
                        image.getIdentifier(), "1", imageFileName);
                file = new File(imageFileName);
                if (!file.exists()) {
                    logger.error("File does not exist in image directory, skipping");
                    return null;
                }
            } catch (Exception e) {
                logger.error(e.toString());
                for (StackTraceElement ste : e.getStackTrace()) {
                    logger.error(ste.toString());
                }
            }
        }        
        return image;
    }

}
