package org.emonocot.ws;

import org.junit.Before;
import org.junit.Ignore;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;

/**
 *
 * @author ben
 *
 */
@RunWith(SpringJUnit4ClassRunner.class)
@ContextConfiguration("/META-INF/spring/applicationContext-test.xml")
public class GetBhlProtologTest {

    /**
     *
     */
    private BhlProtologClient client = new BhlProtologClient();

    /**
	 * @param client the client to set
	 */
    @Autowired
	public final void setBhlProtologClient(BhlProtologClient client) {
		this.client = client;
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
