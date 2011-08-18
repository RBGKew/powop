/**
 * 
 */
package org.emonocot.easymock.matcher;

import java.util.Map;

import org.apache.http.client.methods.HttpGet;
import org.easymock.EasyMock;
import org.easymock.IArgumentMatcher;

/**
 * @author Laptop
 * 
 */
public class HttpGetMatcher implements IArgumentMatcher {

    /**
     * 
     */
    private HttpGet expectedGet;
    private HttpGet actual;

    public HttpGetMatcher(HttpGet expectedGet) {
        this.expectedGet = expectedGet;
    }

    public static final HttpGet eqHttpGet(HttpGet get) {
        EasyMock.reportMatcher(new HttpGetMatcher(get));
        return null;
    }

    /*
     * (non-Javadoc)
     * 
     * @see org.easymock.IArgumentMatcher#appendTo(java.lang.StringBuffer)
     */
    public void appendTo(StringBuffer sb) {
        sb.append("HttpGetMatcher expected "
                + expectedGet.getRequestLine().toString());
        if (actual != null)
            sb.append("  Actual Get was " + actual.getRequestLine().toString());
    }

    /*
     * (non-Javadoc)
     * 
     * @see org.easymock.IArgumentMatcher#matches(java.lang.Object)
     */
    public boolean matches(Object o) {
        if (o == null || !(o instanceof HttpGet))
            return false;
        actual = (HttpGet) o;
        String actualRequestString = actual.getRequestLine().toString();
        return actualRequestString.equals(expectedGet.getRequestLine()
                .toString());
    }

}
