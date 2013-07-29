package org.emonocot.harvest.common;

import java.io.File;
import java.util.Arrays;

import org.springframework.batch.item.database.AbstractPagingItemReader;

public class DirectoryReader extends AbstractPagingItemReader<File> {
	
	private File[] files;
	
	private String directoryName;
	
	public void setDirectoryName(String directoryName) {
		this.directoryName = directoryName;
	}
	
	@Override
	protected void doOpen() {
		File directory = new File(directoryName);
		files = directory.listFiles();
	}
	
	@Override
	protected void doClose() {
		files = null;
	}

	@Override
	protected void doReadPage() {		
		int from = this.getPageSize() * this.getPage();
		int to = from + this.getPageSize();
		results = Arrays.asList(Arrays.copyOfRange(files, from, to));		
	}

	@Override
	protected void doJumpToPage(int itemIndex) {		
	}
}
