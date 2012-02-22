package org.emonocot.ws;

import org.junit.Before;
import org.junit.Test;

/**
 *
 * @author ben
 *
 */
public class GetBhlProtologTest {

    /**
     *
     */
    private BhlProtologClient client;

    /**
     *
     */
    @Before
    public final void setUp() {
        client = new BhlProtologClient();
        client.setWebServiceUrl("http://build.e-monocot.org/test/bhlResponse.json?page=");
    }

    /**
     *
     */
    @Test
    public final void testGetProtolog() {
        String pageNumber = "http://biodiversitylibrary.org/page/358991";
        String response = client.getProtolog(pageNumber);
        System.out.println(response);
    }
}
