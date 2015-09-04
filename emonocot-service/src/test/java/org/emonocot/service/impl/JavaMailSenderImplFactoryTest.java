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
package org.emonocot.service.impl;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNull;

import java.util.Properties;

import org.junit.Test;
import org.springframework.mail.javamail.JavaMailSenderImpl;

/**
 * @author jk00kg
 *
 */
public class JavaMailSenderImplFactoryTest {

	/**
	 *
	 */
	@Test
	public void testGetObjectWithoutBlanks() throws Exception {
		JavaMailSenderImplFactory factory = new JavaMailSenderImplFactory();
		factory.setUsername("");
		factory.setPassword("");
		Properties props = new Properties();
		factory.setJavaMailProperties(props);
		JavaMailSenderImpl sender = (JavaMailSenderImpl) factory.getObject();
		assertNull("Username should be null, not blank", sender.getUsername());
		assertNull("Password should be null, not blank", sender.getPassword());
		assertEquals("There should be no properties", 0, sender.getJavaMailProperties().size());
	}

	@Test
	public void testGetObjectWithParams() throws Exception {
		JavaMailSenderImplFactory factory = new JavaMailSenderImplFactory();
		String user = "foo";
		factory.setUsername(user);
		String pass = "bar";
		factory.setPassword(pass);
		Properties props = new Properties();
		props.put(user, pass);
		factory.setJavaMailProperties(props);
		JavaMailSenderImpl sender = (JavaMailSenderImpl) factory.getObject();
		assertEquals("Username should be " + user, user, sender.getUsername());
		assertEquals("Password should be " + pass, pass, sender.getPassword());
		assertEquals("Properties should be set", props, sender.getJavaMailProperties());
	}

}
