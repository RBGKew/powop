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
package org.powo.harvest.common;

import java.io.File;
import java.io.FileFilter;
import java.util.Arrays;

import org.apache.commons.io.comparator.NameFileComparator;
import org.apache.commons.io.filefilter.FileFilterUtils;
import org.springframework.batch.item.ExecutionContext;
import org.springframework.batch.item.ItemReader;
import org.springframework.batch.item.ItemStream;
import org.springframework.batch.item.ItemStreamException;

public class DirectoryReader implements ItemReader<File>, ItemStream {

	private File [] files;

	private int currentCount;

	private String key = "file.in.directory.count";

	private String directoryName;

	@Override
	public void open(ExecutionContext executionContext)	throws ItemStreamException {
		File directory = new File(directoryName);
		if(directory.isDirectory()) {
			this.files = directory.listFiles((FileFilter) FileFilterUtils.fileFileFilter());
			if(files.length > 1) {
				Arrays.sort(files, new NameFileComparator());
			}
		} else {
			throw new IllegalArgumentException(directoryName + " is not a directory");
		}
		currentCount = executionContext.getInt(key, 0);
	}

	@Override
	public void update(ExecutionContext executionContext) throws ItemStreamException {
		executionContext.putInt(key, currentCount);
	}

	@Override
	public void close() throws ItemStreamException { }

	@Override
	public File read() throws Exception {
		int index = ++currentCount - 1;
		if(index == files.length) {
			return null;
		}
		return files[index];
	}

	public void setDirectoryName(String directoryName) {
		this.directoryName = directoryName;
	}
}
