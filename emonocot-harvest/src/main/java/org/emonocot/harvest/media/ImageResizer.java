package org.emonocot.harvest.media;

import java.io.File;

import org.apache.sanselan.ImageInfo;
import org.apache.sanselan.Sanselan;
import org.emonocot.model.Image;
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
public class ImageResizer implements ItemProcessor<Image, Image> {

    private Logger logger = LoggerFactory.getLogger(ImageResizer.class);

    private final Integer MAX_IMAGE_DIMENSION = 1000;

    private String searchPath;

    private String imageDirectory;
    
   public final void setImageMagickSearchPath(final String imageMagickSearchPath) {
       this.searchPath = imageMagickSearchPath;
   }

    public final void setImageDirectory(final String newImageDirectory) {
        this.imageDirectory = newImageDirectory;
    }

    /**
     * @param image Set the image
     * @return an image or null if the image is to be skipped
     * @throws Exception if there is a problem processing the image
     */
    public final Image process(final Image image) throws Exception {        
        
        String imageFileName = imageDirectory + File.separatorChar  + image.getId() + '.' + image.getFormat();
        File file = new File(imageFileName);
        if(!file.exists()) {
        	logger.warn("File does not exist in image directory, skipping");
        } else {
            try {
                ImageInfo imageInfo = Sanselan.getImageInfo(file);
                Integer width = new Integer(imageInfo.getWidth());
                Integer height = new Integer(imageInfo.getHeight());
                logger.debug("Image " + imageFileName + " dimensions: " + width + " x " + height);

                if (width > MAX_IMAGE_DIMENSION || height > MAX_IMAGE_DIMENSION) {
                    
                    // shrink to no larger than MAX_IMAGE_DIMENSION * MAX_IMAGE_DIMENSION
                    MogrifyCmd mogrify = new MogrifyCmd();
                    if (searchPath != null) {
                    	mogrify.setSearchPath(searchPath);
                    }
                    IMOperation resize = new IMOperation();
                    resize.addImage(imageFileName);
                    logger.debug("resizing to no larger than " + MAX_IMAGE_DIMENSION.intValue()  + " * " + MAX_IMAGE_DIMENSION.intValue());
                    resize.resize(MAX_IMAGE_DIMENSION.intValue(), MAX_IMAGE_DIMENSION.intValue(),'>');
                    resize.addImage(imageFileName);
                    mogrify.run(resize);

                } else {
                    logger.info("No need to resize image as it is smaller than " + MAX_IMAGE_DIMENSION + "px x " + MAX_IMAGE_DIMENSION + "px");
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
