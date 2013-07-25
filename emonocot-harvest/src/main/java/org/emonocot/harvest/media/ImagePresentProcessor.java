package org.emonocot.harvest.media;

import java.io.File;

import org.emonocot.api.ImageService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.batch.item.ItemProcessor;
import org.springframework.beans.factory.annotation.Autowired;

public class ImagePresentProcessor implements ItemProcessor<File, String> {
	
	private ImageService imageService;
	
	private Logger logger = LoggerFactory.getLogger(ImagePresentProcessor.class);
	
	@Autowired
	public void setImageService(ImageService imageService) {
		this.imageService = imageService;
	}	

	@Override
	public String process(File item) throws Exception {
		String name = item.getName();
		if(name.indexOf(".") != -1) {
			try {
		        Long id = new Long(name.substring(0, name.indexOf(".") - 1));
		        if(imageService.find(id) == null) {
				    return name;
			    } else {
			        return null;
			    }
			} catch(NumberFormatException nfe) {
				logger.error("Could not resolve image record for " + item.getName());
				return null;
			}
		} else {
			return null;
		}
		
	}

}
