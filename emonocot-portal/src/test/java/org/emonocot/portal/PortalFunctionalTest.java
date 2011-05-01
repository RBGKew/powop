package org.emonocot.portal;

import org.junit.After;
import org.junit.Before;
import org.junit.Test;

import com.thoughtworks.selenium.DefaultSelenium;
import com.thoughtworks.selenium.SeleneseTestCase;

public class PortalFunctionalTest extends SeleneseTestCase {
	@Before
	public void setUp() throws Exception {
		selenium = new DefaultSelenium("localhost", 4444, "*firefox /usr/lib/firefox-3.6/firefox", "http://129.67.24.160/latest/portal/");
		
		selenium.start();
	}

	@Test
	public void testSimple () throws Exception {
		assertTrue(selenium.isTextPresent(""));
	}

	@After
	public void tearDown() throws Exception {
		selenium.stop();
	}
}
