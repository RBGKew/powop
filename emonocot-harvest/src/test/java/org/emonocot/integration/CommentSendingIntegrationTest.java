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
package org.emonocot.integration;

import static org.junit.Assert.assertEquals;

import org.joda.time.DateTime;
import org.joda.time.Duration;
import org.joda.time.ReadableDuration;
import org.junit.After;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.test.annotation.DirtiesContext;
import org.springframework.test.annotation.DirtiesContext.ClassMode;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;

/**
 * @author jk00kg
 *
 */
@RunWith(SpringJUnit4ClassRunner.class)
@ContextConfiguration({"classpath:META-INF/spring/applicationContext-integration.xml",
"classpath:META-INF/spring/applicationContext-test.xml"})
@DirtiesContext(classMode = ClassMode.AFTER_CLASS)
public class CommentSendingIntegrationTest {

	private Logger logger = LoggerFactory.getLogger(CommentSendingIntegrationTest.class);

	/**
	 *
	 */
	@Autowired
	@Qualifier("testMessageHandler")
	private MessageHandler handler;

	/**
	 * @throws java.lang.Exception
	 */
	@Before
	public void setUp() throws Exception {
	}

	/**
	 * @throws java.lang.Exception
	 */
	@After
	public void tearDown() throws Exception {
	}

	@Test
	public final void testPoller() {
		block(new Duration(5000));
		logger.info("There are " + handler.messages.size() + " messages");
		assertEquals("We should be sending 4 messages", 4, handler.messages.size());
	}

	/**
	 * @param duration
	 */
	public void block(ReadableDuration duration) {
		DateTime started = new DateTime();
		logger.info("Started at " + started);
		DateTime endAt = started.plus(duration);
		logger.info("Will end at " + endAt);
		while (endAt.isAfterNow()) {
			//wait
		}
	}
}
