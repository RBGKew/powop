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
package org.powo.job.dwc.read;

import java.io.BufferedInputStream;
import java.io.BufferedOutputStream;
import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.util.Enumeration;
import java.util.zip.ZipEntry;
import java.util.zip.ZipFile;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.batch.core.ExitStatus;

/**
 *
 * @author ben
 *
 */
public class ArchiveUnpacker {
	/**
	 *
	 */
	private Logger logger = LoggerFactory.getLogger(ArchiveUnpacker.class);

	/**
	 *
	 */
	static final int BUFFER = 2048;

	/**
	 *
	 * @param archiveName
	 *            The name of the archive file to unpack
	 * @param unpackDirectory
	 *            The directory to unpack the file into
	 * @return an exit status indicating whether this
	 *         step was successfully completed
	 */
	public final ExitStatus unpackArchive(final String archiveName,
			final String unpackDirectory) {
		try {
			File directory = new File(unpackDirectory);
			if (!directory.exists()) {
				directory.mkdir();
			}
			BufferedOutputStream bufferedOutputStream = null;
			BufferedInputStream bufferedInputStream = null;
			ZipEntry entry;
			ZipFile zipfile = new ZipFile(archiveName);
			Enumeration entries = zipfile.entries();
			while (entries.hasMoreElements()) {
				entry = (ZipEntry) entries.nextElement();
				if(entry.getName().indexOf("/") != -1) {
					// entry is in a subdir
					String subDirectoryName = entry.getName().substring(0, entry.getName().indexOf("/"));
					File subDirectory = new File(directory,subDirectoryName);
					if(!subDirectory.exists()) {
						subDirectory.mkdirs();
					}
				}
				logger.debug("Extracting: " + entry + " from " + archiveName);
				bufferedInputStream = new BufferedInputStream(
						zipfile.getInputStream(entry));
				int count;
				byte[] data = new byte[BUFFER];
				FileOutputStream fileOutputStream = new FileOutputStream(
						new File(directory, entry.getName()));
				bufferedOutputStream = new BufferedOutputStream(
						fileOutputStream, BUFFER);
				while (
						(count
								= bufferedInputStream.read(data, 0, BUFFER)) != -1) {
					bufferedOutputStream.write(data, 0, count);
				}
				bufferedOutputStream.flush();
				bufferedOutputStream.close();
				bufferedInputStream.close();
			}
		} catch (IOException ioe) {
			logger.error("Input Output Exception unpacking "
					+ archiveName + " " + ioe.getLocalizedMessage());
			return ExitStatus.FAILED;
		}

		return ExitStatus.COMPLETED;
	}

}
