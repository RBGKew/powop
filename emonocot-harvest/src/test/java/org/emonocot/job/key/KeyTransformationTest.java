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
package org.emonocot.job.key;

import java.io.File;
import java.io.IOException;
import java.util.HashMap;
import java.util.Map;

import org.junit.Before;
import org.junit.Test;
import org.springframework.core.io.ClassPathResource;
import org.springframework.core.io.FileSystemResource;
import org.springframework.core.io.Resource;

/**
 *
 * @author ben
 *
 */
public class KeyTransformationTest {

	private Resource inputFile = new ClassPathResource(
			"org/emonocot/job/sdd/testKey.xml");

	private Resource xsltFile = new ClassPathResource(
			"org/emonocot/job/key/sddToJSON.xsl");

	private Resource taxonNameFile = new ClassPathResource(
			"org/emonocot/job/sdd/taxon-file.xml");

	private Resource imageFile = new ClassPathResource(
			"org/emonocot/job/sdd/image-file.xml");

	private XmlTransformingTasklet xmlTransformingTasklet;

	/**
	 * @throws IOException if we cannot create a tmp file
	 */
	@Before
	public final void setUp() throws IOException {
		xmlTransformingTasklet = new XmlTransformingTasklet();
		xmlTransformingTasklet.setInputFile(inputFile);
		xmlTransformingTasklet.setXsltFile(xsltFile);
		File output = File.createTempFile("output","json");
		FileSystemResource outputFile = new FileSystemResource(output);
		Map<String, String> parameters = new HashMap<String, String>();
		parameters.put("taxonFileName", taxonNameFile.getFile()
				.getAbsolutePath());
		parameters.put("imageFileName", imageFile.getFile()
				.getAbsolutePath());
		xmlTransformingTasklet.setParameters(parameters);
		xmlTransformingTasklet.setOutputFile(outputFile);
	}

	/**
	 * @throws Exception if there is a problem running the test
	 */
	@Test
	public final void testKeyTransformation() throws Exception {
		xmlTransformingTasklet.execute(null, null);
	}

}
