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
public class ImageThumbnailGeneratorImpl implements ItemProcessor<Image, Image>, ImageThumbnailGenerator {

    private Logger logger = LoggerFactory.getLogger(ImageThumbnailGeneratorImpl.class);

    private final Double THUMBNAIL_DIMENSION = 100D;

    private String searchPath;

    private String imageDirectory;
    
    private ImageAnnotator imageAnnotator;

    private String thumbnailDirectory;
    
    private Boolean skipUnmodified = Boolean.TRUE;
    
    public void setSkipUnmodified(Boolean skipUnmodified) {
    	if(skipUnmodified != null) {
    	    this.skipUnmodified = skipUnmodified;
    	}
    }

    /**
     *
     * @param newThumbnailDirectory set the thumbnail directory
     */
    public final void setThumbnailDirectory(final String newThumbnailDirectory) {
        this.thumbnailDirectory = newThumbnailDirectory;
    }

    /**
     *
     * @param imageMagickSearchPath set the image magick search path directory
     */
   public final void setImageMagickSearchPath(final String imageMagickSearchPath) {
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
     * @param imageAnnotator the imageAnnotator to set
     */
    public void setImageAnnotator(ImageAnnotator imageAnnotator) {
        this.imageAnnotator = imageAnnotator;
    }

    /* (non-Javadoc)
	 * @see org.emonocot.harvest.media.ImageThumbnailGenerator#process(org.emonocot.model.Image)
	 */
    @Override
	public final Image process(final Image image) throws Exception {
        String thumbnailFileName = thumbnailDirectory + File.separatorChar
                + image.getId() + '.' + image.getFormat();
        
        File thumbnailFile = new File(thumbnailFileName);
        String imageFileName = imageDirectory + File.separatorChar  + image.getId() + '.' + image.getFormat();
        File file = new File(imageFileName);
        if(!file.exists()) {
            logger.warn("File does not exist in image directory, skipping");
            imageAnnotator.annotate(image, AnnotationType.Error, AnnotationCode.BadData, "A thumbnail could not be generated as the local file was not found.");
        } else if(thumbnailFile.exists() && skipUnmodified) {
            logger.info("Thumbnail File exists in image directory, skipping");
        } else {
            try {
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
                logger.error("Unable to generate thumbnail for image " + image.getIdentifier(), e);
                imageAnnotator.annotate(image, AnnotationType.Error, AnnotationCode.BadData, "There was an error generating a thumbnail");
            }
        }
        return image;
    }

}
