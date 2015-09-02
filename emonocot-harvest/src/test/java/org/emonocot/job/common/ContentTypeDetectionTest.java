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
package org.emonocot.job.common;

import static org.junit.Assert.assertEquals;

import java.io.IOException;

import org.apache.tika.Tika;
import org.junit.Before;
import org.junit.Test;
import org.springframework.core.io.ClassPathResource;
import org.springframework.core.io.Resource;

public class ContentTypeDetectionTest {

	private Tika tika;

	Resource lucidKey = new ClassPathResource("org/emonocot/job/common/testKey.xml");
	Resource xperKey = new ClassPathResource("org/emonocot/job/common/testXperKey.xml");
	Resource nexusFile = new ClassPathResource("org/emonocot/job/common/test.nex");
	Resource newickFile = new ClassPathResource("org/emonocot/job/common/test.nwk");

	@Before
	public void setUp() {
		tika = new Tika();
	}

	@Test
	public void test() throws IOException {
		assertEquals("application/sdd+xml", tika.detect(lucidKey.getURL()));
		assertEquals("application/sdd+xml", tika.detect(xperKey.getURL()));
		assertEquals("application/nexus", tika.detect(nexusFile.getURL()));
		assertEquals("application/newick", tika.detect(newickFile.getURL()));
	}

}
