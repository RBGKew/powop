package org.emonocot.harvest.media;

import java.io.File;

import org.emonocot.harvest.common.GetResourceClient;
import org.emonocot.model.Image;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.batch.core.ExitStatus;
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
            System.out.println(imageDirectory);
            System.out.println(image.getId());
            System.out.println(image.getFormat());
            logger.info("File exists in image directory, issuing a conditional GET");
            ExitStatus status = getResourceClient.getBinaryResource("", image.getIdentifier(), Long.toString(file.lastModified()), imageFileName);
            if(status == null || ExitStatus.FAILED.equals(status)) {
                logger.error("There was a problem trying to get " + image.getIdentifier());
                return null;
            } else if ("NOT_MODIFIED".equals(status.getExitCode())) {
                logger.info("The image is already up to date");
                //TODO annotate?
                return null;
            }
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
