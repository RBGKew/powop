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
package org.emonocot.harvest.common;

import java.io.BufferedOutputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.util.zip.ZipEntry;
import java.util.zip.ZipException;
import java.util.zip.ZipOutputStream;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.batch.core.ExitStatus;

/**
 * @author jk
 *
 */
public class FileResourceZipper {
	
	/**
	 * 
	 */
	private static final int BUFFER_SIZE = 1024;
	
	/**
	 * 
	 */
	private static Logger logger = LoggerFactory.getLogger(FileResourceZipper.class);
	
	/**
	 * @param source The file or directory of files to write to the zip file
	 * @param target The absolute filepath for the zip file to be written 
	 * @throws IOException 
	 */
	public static final void zip(final String source, final String target) throws IOException, ZipException {
        ZipOutputStream zipOutputStream = null;
        File sourceFile = null;
        //Either source or array of files if the source is a directory
        String[] filenames = null;
        
        try { //to wrap things we can de-reference immediately
            sourceFile = new File(source);
            if(!sourceFile.exists()) {
                throw new FileNotFoundException("Unable to find filesystem resource - " + source);
            }
            if(sourceFile.isDirectory()) {
                filenames = sourceFile.list();
            } else {
                filenames = new String[] {source};
            }
            
            FileOutputStream fileOut = new FileOutputStream(target);
            BufferedOutputStream bufferedOut = new BufferedOutputStream(fileOut, BUFFER_SIZE);
            zipOutputStream = new ZipOutputStream(bufferedOut);
        } catch (IOException e) {
            logger.error("Unable to initalise files for writing " + source + " to " + target, e);
            throw e;
        }

        try {
            byte[] buffer = new byte[BUFFER_SIZE];
            
            logger.debug("Attempting to add " + filenames.length + " file(s) to zip");
            for (String filename : filenames){
                FileInputStream fileIn = null;
                if (source.endsWith("/")) {
                    fileIn = new FileInputStream(source + filename);
                } else if (source.equals(filename)){
                    fileIn = new FileInputStream(filename);
                } else {
                    fileIn = new FileInputStream(source + "/" + filename);
                }
                zipOutputStream.putNextEntry(new ZipEntry(filename));
                int length;
                while ((length = fileIn.read(buffer)) != -1) {
                    zipOutputStream.write(buffer, 0, length);
                }
                fileIn.close();
            }
        } catch (IOException e) {
            logger.error("Error writing " + source + " to " + target, e);
            throw e;
        } finally {
            try {
                zipOutputStream.close();
            } catch (IOException e) {
                logger.error("Unable to close ZipOutputStream " + zipOutputStream, e);
                throw e;
            }
        }
	}
	
	/**
     * @param source The directory containing the DwC/A files
     * @param target The absolute filepath for where to write the archive
	 * @return
	 */
	public static final ExitStatus packArchive(final String source, final String target) {
	    
	    logger.info("Attempting to pack " + source + " to " +  target);
		try { //to wrap things we can de-reference immediately
			File sourceDir = new File(source);
			if(!sourceDir.exists()) {
				throw new FileNotFoundException("Unable to find filesystem resource - " + source);
			}
			if(!sourceDir.isDirectory()) {
				throw new IllegalArgumentException(source + " is not a folder");
			}
			
			zip(source, target);
			return ExitStatus.COMPLETED;
		} catch (Exception e) {
			logger.error("Error writing " + source + " to " + target, e);
			return ExitStatus.FAILED;
		}
    }

}
