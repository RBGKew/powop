package org.emonocot.harvest.media;

import java.io.File;
import java.util.List;

import org.springframework.batch.item.ItemWriter;

public class ImageDeletingWriter implements ItemWriter<String> {
	
	private String imageDirectory;
	
	private String thumbnailDirectory;

	public void setImageDirectory(String imageDirectory) {
		this.imageDirectory = imageDirectory;
	}

	public void setThumbnailDirectory(String thumbnailDirectory) {
		this.thumbnailDirectory = thumbnailDirectory;
	}

	@Override
	public void write(List<? extends String> items) throws Exception {
		for(String name : items) {
			String thumbnailFileName = thumbnailDirectory + File.separatorChar + name;
            File thumbnailFile = new File(thumbnailFileName);
            String imageFileName = imageDirectory + File.separatorChar  + name;
            File file = new File(imageFileName);
            if(thumbnailFile.exists()) {
            	thumbnailFile.delete();
            }
            if(file.exists()) {
            	file.delete();
            }
		}
	}
}
