package org.emonocot.checklist.logging;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNull;
import static org.junit.Assert.assertNotNull;

import java.io.IOException;

import javax.servlet.FilterChain;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.junit.Before;
import org.junit.Test;
import org.slf4j.MDC;
import org.springframework.mock.web.MockFilterChain;
import org.springframework.mock.web.MockFilterConfig;
import org.springframework.mock.web.MockHttpServletRequest;
import org.springframework.mock.web.MockHttpServletResponse;

/**
 *
 * @author ben
 *
 */
public class ClientParameterFilterTest {

    /**
     *
     */
    private ClientParameterFilter clientParameterFilter = null;

    /**
     *
     */
    private MockHttpServletRequest servletRequest;

    /**
     *
     */
    private MockHttpServletResponse servletResponse;

    /**
     * @throws ServletException if the filter cannot be initialized
     */
    @Before
    public final void setUp() throws ServletException {
        clientParameterFilter = new ClientParameterFilter();
        MockFilterConfig filterConfig = new MockFilterConfig();
        filterConfig.addInitParameter(
                ClientParameterFilter.CONFIG_PARAMETER_NAME, "scratchpad");
        clientParameterFilter.init(filterConfig);

        servletRequest = new MockHttpServletRequest();
        servletResponse = new MockHttpServletResponse();
    }

    /**
     * @throws ServletException if there is a problem
     * @throws IOException if there is a problem
     *
     */
    @Test
    public final void testFilterWithoutParameter()
        throws IOException, ServletException {
        FilterChain filterChain = new MockFilterChain() {
            public void doFilter(HttpServletRequest request,
                    HttpServletResponse response, FilterChain filterChain) {
                assertNull(
                        "The client name should not be set within the filter",
                        MDC.get(LoggingConstants.MDC_CLIENT_NAME_KEY));
            }
        };
        assertNull("The client name should not be set before the filter",
                MDC.get(LoggingConstants.MDC_CLIENT_NAME_KEY));
        clientParameterFilter
          .doFilter(servletRequest, servletResponse, filterChain);
        assertNull("The client name should not be set after the filter",
                MDC.get(LoggingConstants.MDC_CLIENT_NAME_KEY));
    }

    /**
     * @throws ServletException if there is a problem
     * @throws IOException if there is a problem
     *
     */
    @Test
    public final void testFilterWithParameter()
        throws IOException, ServletException {
        servletRequest.addParameter("scratchpad", "foobar");
        FilterChain filterChain = new MockFilterChain() {
            public void doFilter(HttpServletRequest request,
                    HttpServletResponse response, FilterChain filterChain) {
                assertNotNull(
                        "The client name should be set within the filter",
                        MDC.get(LoggingConstants.MDC_CLIENT_NAME_KEY));
                assertEquals(
                        "The client name should equal the value of the parameter",
                        "foobar",
                        MDC.get(LoggingConstants.MDC_CLIENT_NAME_KEY));
            }
        };
        assertNull("The client name should not be set before the filter",
                MDC.get(LoggingConstants.MDC_CLIENT_NAME_KEY));
        clientParameterFilter
          .doFilter(servletRequest, servletResponse, filterChain);
        assertNull("The client name should not be set after the filter",
                MDC.get(LoggingConstants.MDC_CLIENT_NAME_KEY));
    }
}
