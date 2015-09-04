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
package org.emonocot.portal.convert;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.fail;

import org.emonocot.model.auth.User;
import org.emonocot.model.marshall.json.CustomObjectMapperFactory;
import org.emonocot.portal.controller.form.AceDto;
import org.junit.Before;
import org.junit.Test;
import org.springframework.security.acls.domain.BasePermission;

import com.fasterxml.jackson.databind.ObjectMapper;

public class AceDtoConversionTest {

	private ObjectMapper objectMapper;

	@Before
	public void setUp() {
		CustomObjectMapperFactory objectMapperFactory = new CustomObjectMapperFactory();
		objectMapper = objectMapperFactory.getObject();
	}

	/**
	 *
	 * @throws Exception
	 *             if there is a problem serializing the object
	 */
	@Test
	public void testWriteAce() throws Exception {
		AceDto ace = new AceDto();
		User user = new User();
		ace.setPermission(BasePermission.CREATE);

		ace.setObject("testIdentifier");
		ace.setPrincipal("userIdentifier");
		try {
			objectMapper.writeValueAsString(ace);
		} catch (Exception e) {
			fail("No exception expected here");
		}
	}

	/**
	 *
	 * @throws Exception
	 *             if there is a problem serializing the object
	 */
	@Test
	public void testAce() throws Exception {

		AceDto aceDto = objectMapper.readValue("{\"principal\":\"userIdentifier\",\"object\":\"testIdentifier\",\"permission\":\"CREATE\"}", AceDto.class);

		assertEquals("testIdentifier", aceDto.getObject());
		assertEquals("userIdentifier", aceDto.getPrincipal());
		assertEquals(BasePermission.CREATE, aceDto.getPermission());
	}

}
