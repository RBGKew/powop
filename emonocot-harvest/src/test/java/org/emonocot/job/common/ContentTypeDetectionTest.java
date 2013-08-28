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
