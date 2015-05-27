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
package org.emonocot.job.dwc.image;

import java.io.File;
import java.util.List;

import org.emonocot.model.Image;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.batch.item.ItemWriter;


/**
 * @author jk00kg
 *
 */
public class ImageFileDeletingWriter implements ItemWriter<Image> {
    
    private Logger logger = LoggerFactory.getLogger(ImageFileDeletingWriter.class);

    /**
     * 
     */
    private String imageDirectory;

    /**
     * @param imageDirectory the imageDirectory to set
     */
    public void setImageDirectory(String imageDirectory) {
        this.imageDirectory = imageDirectory;
    }

    /* (non-Javadoc)
     * @see org.springframework.batch.item.ItemWriter#write(java.util.List)
     */
    @Override
    public void write(List<? extends Image> items) throws Exception {
        for(Image i : items) {
            File file = new File(imageDirectory + File.separator + i.getId() + "." + i.getFormat());
            if(!file.exists()){
                logger.warn("Tried to delete non-existent file " + file.getCanonicalPath()
                    + " for image " + i);
            } else {
                try{
                    file.delete(); //Don't worry about successfulness
                } catch (SecurityException e) {
                    logger.error("The file " + file + " could be deleted.");
                    throw e;
                }
            }
            file = new File(imageDirectory + File.separator + "thumbnails" + File.separator + i.getId() + "." + i.getFormat());
            if(!file.exists()){
                logger.warn("Tried to delete non-existent file " + file.getCanonicalPath()
                    + " for image " + i);
            } else {
                try{
                    file.delete(); //Don't worry about successfulness
                } catch (SecurityException e) {
                    logger.error("The file " + file + " cannot be deleted.");
                    throw e;
                }
            }
        }
        
    }

}
