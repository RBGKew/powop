package org.emonocot.easymock.matcher;

import org.apache.http.client.methods.HttpGet;
import org.easymock.EasyMock;
import org.easymock.IArgumentMatcher;

/**
 * @author jk00kg
 *
 */
public class HttpGetMatcher implements IArgumentMatcher {

    /**
     *
     */
    private HttpGet expected;
    /**
     *
     */
    private HttpGet actual;

    /**
     *
     * @param expectedGet the expected HttpGet method
     */
    public HttpGetMatcher(HttpGet expectedGet) {
        this.expected = expectedGet;
    }

    /**
     *
     * @param get the expected http get
     * @return null
     */
    public static final HttpGet eqHttpGet(HttpGet get) {
        EasyMock.reportMatcher(new HttpGetMatcher(get));
        return null;
    }

    /**
     * (non-Javadoc).
     * @param stringBuffer the string buffer to append to
     * @see org.easymock.IArgumentMatcher#appendTo(java.lang.StringBuffer)
     */
    public final void appendTo(final StringBuffer stringBuffer) {
        stringBuffer.append("HttpGetMatcher expected "
                + expected.getRequestLine().toString());
        if (actual != null) {
            stringBuffer.append(
                    "  Actual Get was " + actual.getRequestLine().toString());
        }
    }

    /**
     * (non-Javadoc).
     * @param o the object to match
     * @return true if the object matches, false otherwise
     * @see org.easymock.IArgumentMatcher#matches(java.lang.Object)
     */
    public final boolean matches(final Object o) {
        if (o == null || !(o instanceof HttpGet)) {
            return false;
        }
        actual = (HttpGet) o;
        String actualRequestString = actual.getRequestLine().toString();
        return actualRequestString.equals(expected.getRequestLine()
                .toString());
    }

}
