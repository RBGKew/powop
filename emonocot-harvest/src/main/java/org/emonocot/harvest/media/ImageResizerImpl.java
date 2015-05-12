/*
 * This is eMonocot, a global online biodiversity information resource.
 *
 * Copyright © 2011–2015 The Board of Trustees of the Royal Botanic Gardens, Kew and The University of Oxford
 *
 * eMonocot is free software: you can redistribute it and/or modify it under the terms of the
 * GNU Affero General Public License as published by the Free Software Foundation, either version 3
 * of the License, or (at your option) any later version.
 *
 * eMonocot is distributed in the hope that it will be useful, but WITHOUT ANY WARRANTY; without even
 * the implied warranty of MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 * GNU Affero General Public License for more details.
 *
 * The complete text of the GNU Affero General Public License is in the source repository as the file
 * ‘COPYING’.  It is also available from <http://www.gnu.org/licenses/>.
 */
package org.emonocot.harvest.media;

import java.io.File;

import org.apache.sanselan.ImageInfo;
import org.apache.sanselan.Sanselan;
import org.emonocot.model.Image;
import org.emonocot.model.constants.AnnotationCode;
import org.emonocot.model.constants.AnnotationType;
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
public class ImageResizerImpl implements ItemProcessor<Image, Image>, ImageResizer {

    private Logger logger = LoggerFactory.getLogger(ImageResizerImpl.class);

    private final Integer MAX_IMAGE_DIMENSION = 1000;

    private String searchPath;

    private String imageDirectory;
    
    private ImageAnnotator imageAnnotator;
    
    public final void setImageMagickSearchPath(final String imageMagickSearchPath) {
        this.searchPath = imageMagickSearchPath;
    }

    public final void setImageDirectory(final String newImageDirectory) {
        this.imageDirectory = newImageDirectory;
    }

    /**
     * @param imageAnnotator the imageAnnotator to set
     */
    public void setImageAnnotator(ImageAnnotator imageAnnotator) {
        this.imageAnnotator = imageAnnotator;
    }

    /* (non-Javadoc)
	 * @see org.emonocot.harvest.media.ImageResizer#process(org.emonocot.model.Image)
	 */
    @Override
	public final Image process(final Image image) throws Exception {
        
        String imageFileName = imageDirectory + File.separatorChar  + image.getId() + '.' + image.getFormat();
        File file = new File(imageFileName);
        if(!file.exists()) {
        	logger.warn("File does not exist in image directory, skipping");
        	imageAnnotator.annotate(image, AnnotationType.Error, AnnotationCode.BadData, "The local file was not found, so cannot be resized");
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
                logger.error("There was an error resizing the image", e);
                imageAnnotator.annotate(image, AnnotationType.Error, AnnotationCode.BadData, "The file could not be resized");
            }
        }
        return image;
    }
}
