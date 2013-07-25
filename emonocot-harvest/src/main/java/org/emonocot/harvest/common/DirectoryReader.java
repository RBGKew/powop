package org.emonocot.harvest.common;

import java.io.File;
import java.util.Arrays;

import org.springframework.batch.item.database.AbstractPagingItemReader;

public class DirectoryReader extends AbstractPagingItemReader<File> {
	
	private String directoryName;
	
	public void setDirectoryName(String directoryName) {
		this.directoryName = directoryName;
	}

	@Override
	protected void doReadPage() {
		File directory = new File(directoryName);
		File[] files = directory.listFiles();
		int from = this.getPageSize() * this.getPage();
		int to = from + this.getPageSize();
		results = Arrays.asList(Arrays.copyOfRange(files, from, to));		
	}

	@Override
	protected void doJumpToPage(int itemIndex) {		
	}
}
