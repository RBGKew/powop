package org.emonocot.harvest.media;

import java.io.File;

import org.apache.sanselan.ImageInfo;
import org.apache.sanselan.Sanselan;
import org.emonocot.model.media.Image;
import org.emonocot.ws.GetResourceClient;
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
    private final Double THUMBNAIL_DIMENSION = 100D;

    /**
     *
     */
    private final Integer IMAGE_DIMENSION = 1000;

    /**
     *
     */
    private String searchPath;

    /**
     *
     */
    private String imageDirectory;

    /**
     *
     */
    private String thumbnailDirectory;

    /**
     *
     */
    private GetResourceClient getResourceClient;

    /**
     *
     * @param newThumbnailDirectory set the thumbnail directory
     */
    public final void setThumbnailDirectory(
            final String newThumbnailDirectory) {
        this.thumbnailDirectory = newThumbnailDirectory;
    }

    /**
     *
     * @param imageMagickSearchPath set the image magick search path directory
     */
   public final void setImageMagickSearchPath(
           final String imageMagickSearchPath) {
       this.searchPath = imageMagickSearchPath;
   }

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
        String imageFileName = imageDirectory + File.separatorChar
                + image.getIdentifier() + '.' + image.getFormat();
        String thumbnailFileName = thumbnailDirectory + File.separatorChar
                + image.getIdentifier() + '.' + image.getFormat();
        File file = new File(imageFileName);
        logger.debug("Image File " + imageFileName);
        if (file.exists()) {
            logger.info("File exists in image directory, skipping");
            return null;
        } else {
            try {
                getResourceClient.getBinaryResource("",
                        image.getUrl(), "1", imageFileName);
                file = new File(imageFileName);
                if (!file.exists()) {
                    logger.error("File does not exist in image directory, skipping");
                    return null;
                }
                ImageInfo imageInfo = Sanselan.getImageInfo(file);
                Double width = new Double(imageInfo.getWidth());
                Double height = new Double(imageInfo.getHeight());
                logger.debug("Image " + imageFileName + " dimensions: " + width + " x " + height);

                if (width > height) {
                    Double newWidth = (width / height) * THUMBNAIL_DIMENSION;
                    Double xOffset = (newWidth - THUMBNAIL_DIMENSION) / 2.0D;
                    // shrink to 100px high then crop
                    ConvertCmd convert = new ConvertCmd();
                    if (searchPath != null) {
                        convert.setSearchPath(searchPath);
                    }
                    IMOperation resize = new IMOperation();
                    resize.addImage(imageFileName);
                    logger.debug("Resizing to " + newWidth.intValue()
                            + " * " + THUMBNAIL_DIMENSION.intValue());
                    resize.resize(newWidth.intValue(),
                            THUMBNAIL_DIMENSION.intValue());
                    resize.addImage(thumbnailFileName);
                    convert.run(resize);

                    MogrifyCmd mogrify = new MogrifyCmd();
                    if (searchPath != null) {
                        mogrify.setSearchPath(searchPath);
                    }
                    IMOperation crop = new IMOperation();
                    crop = new IMOperation();
                    logger.debug("Cropping to " + xOffset.intValue() + " * 0");
                    crop.crop(THUMBNAIL_DIMENSION.intValue(),
                            THUMBNAIL_DIMENSION.intValue(), xOffset.intValue());
                    crop.addImage(thumbnailFileName);
                    mogrify.run(crop);

                } else {
                    Double newHeight = (height / width) * THUMBNAIL_DIMENSION;
                    Double yOffset = (newHeight - THUMBNAIL_DIMENSION) / 2.0D;
                    // shrink to 100px high then crop
                    ConvertCmd convert = new ConvertCmd();
                    if (searchPath != null) {
                        convert.setSearchPath(searchPath);
                    }
                    IMOperation resize = new IMOperation();
                    resize.addImage(imageFileName);
                    logger.debug("Resizing to " + THUMBNAIL_DIMENSION.intValue()
                            + " * " + newHeight.intValue());
                    resize.resize(THUMBNAIL_DIMENSION.intValue(),
                            newHeight.intValue());
                    resize.addImage(thumbnailFileName);
                    convert.run(resize);

                    MogrifyCmd mogrify = new MogrifyCmd();
                    if (searchPath != null) {
                        mogrify.setSearchPath(searchPath);
                    }
                    IMOperation crop = new IMOperation();
                    crop = new IMOperation();
                    logger.debug("Cropping to 0 * " + yOffset.intValue());
                    crop.crop(THUMBNAIL_DIMENSION.intValue(),
                            THUMBNAIL_DIMENSION.intValue(), 0, yOffset.intValue());
                    crop.addImage(thumbnailFileName);
                    mogrify.run(crop);
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
