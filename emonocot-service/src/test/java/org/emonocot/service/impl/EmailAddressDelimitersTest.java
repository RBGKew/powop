/**
 * 
 */
package org.emonocot.service.impl;

import static org.junit.Assert.*;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
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
