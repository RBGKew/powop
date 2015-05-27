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

import static org.junit.Assert.*;

import java.util.HashMap;
import java.util.Map;

import org.junit.After;
import org.junit.Before;
import org.junit.Test;

/**
 * @author jk00kg
 *
 */
public class EmailAddressDelimitersTest {
    
    private Map<String, String[]> testData;

    /**
     * @throws java.lang.Exception
     */
    @Before
    public void setUp() throws Exception {
        testData = new HashMap<String, String[]>();
        testData.put("email1@example.com,email2@example.com,email3@example.com",
                new String[] {"email1@example.com", "email2@example.com", "email3@example.com"});
        testData.put("email1@example.com;email2@example.com;;email3@example.com",
                new String[] {"email1@example.com", "email2@example.com", "email3@example.com"});
        testData.put("email1@example.com email2@example.com; email3@example.com",
                new String[] {"email1@example.com", "email2@example.com", "email3@example.com"});
        testData.put("email1@example.com",
                new String[] {"email1@example.com"});
    }

    /**
     * @throws java.lang.Exception
     */
    @After
    public void tearDown() throws Exception {
    }

    @Test
    public final void test() {
        for(String input : testData.keySet()) {
            assertArrayEquals("Did not get the expected email addresses from " + input,
                    testData.get(input), EmailServiceImpl.ADDRESS_DELIMITERS.split(input));
        }
    }

}
