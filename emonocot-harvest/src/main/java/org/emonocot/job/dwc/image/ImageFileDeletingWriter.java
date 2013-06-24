/**
 * 
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
