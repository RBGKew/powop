package org.emonocot.harvest.media;

import java.io.File;
import java.util.List;

import org.springframework.batch.item.ItemWriter;

public class ImageDeletingWriter implements ItemWriter<String> {
	
	private String imageDirectory;

	public void setImageDirectory(String imageDirectory) {
		this.imageDirectory = imageDirectory;
	}

	@Override
	public void write(List<? extends String> items) throws Exception {
		for(String name : items) {			
            String imageFileName = imageDirectory + File.separatorChar  + name;
            File file = new File(imageFileName);
            if(file.exists()) {
            	file.delete();
            }
		}
	}
}
