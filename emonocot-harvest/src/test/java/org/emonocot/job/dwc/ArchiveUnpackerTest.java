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
package org.emonocot.job.dwc;

import static org.hamcrest.collection.IsArrayContaining.hasItemInArray;
import static org.junit.Assert.assertThat;

import java.io.File;

import org.emonocot.job.dwc.read.ArchiveUnpacker;
import org.junit.Test;
import org.springframework.core.io.ClassPathResource;
import org.springframework.core.io.Resource;

/**
 *
 * @author ben
 *
 */
public class ArchiveUnpackerTest {

	/**
	 *
	 */
	private Resource content = new ClassPathResource(
			"/org/emonocot/job/common/dwc.zip");

	/**
	 *
	 */
	private String unpackDirectoryName = System.getProperty("java.io.tmpdir")
			+ "/archive";

	/**
	 *
	 */
	private ArchiveUnpacker archiveUnpacker = new ArchiveUnpacker();

	/**
	 * @throws Exception if there is a problem accessing the file
	 */
	@Test
	public final void testUnpack() throws Exception {

		archiveUnpacker.unpackArchive(content.getFile().getAbsolutePath(),
				unpackDirectoryName);

		File unpackDirectory = new File(unpackDirectoryName);
		String[] actualFiles = unpackDirectory.list();
		String[] expectedFiles = new String[] {
				"description.txt",
				"distribution.txt",
				"eml.xml",
				"images.txt",
				"meta.xml",
				"taxa.txt"
		};
		for (String expectedFile : expectedFiles) {
			assertThat(actualFiles,
					hasItemInArray(expectedFile));
		}
	}

}
