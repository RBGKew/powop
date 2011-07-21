package org.emonocot.checklist.logging;

import java.io.IOException;

import javax.servlet.Filter;
import javax.servlet.FilterChain;
import javax.servlet.FilterConfig;
import javax.servlet.ServletException;
import javax.servlet.ServletRequest;
import javax.servlet.ServletResponse;

import org.slf4j.MDC;

/**
 *
 * @author ben
 *
 */
public class ClientParameterFilter implements Filter {
    /**
     * The name of the parameter to set in web.xml.
     */
    public static final String CONFIG_PARAMETER_NAME = "parameterName";

    /**
     * The default parameter.
     */
    public static final String DEFAULT_PARAMETER_NAME = "client";

    /**
     *
     */
    private String parameterName = ClientParameterFilter.DEFAULT_PARAMETER_NAME;

    /**
     *
     */
    public void destroy() {
        // do nothing
    }

    /**
     * @param request the servlet request
     * @param response the servlet response
     * @param chain the filter chain
     * @throws IOException if there is a problem
     * @throws ServletException if there is a problem
     */
    public final void doFilter(final ServletRequest request,
            final ServletResponse response, final FilterChain chain)
            throws IOException, ServletException {
        if (request.getParameter(parameterName) != null) {
            MDC.put(LoggingConstants.MDC_CLIENT_NAME_KEY,
                    request.getParameter(parameterName));
            try {
                chain.doFilter(request, response);
            } finally {
                MDC.remove(LoggingConstants.MDC_CLIENT_NAME_KEY);
            }
        } else {
            chain.doFilter(request, response);
        }
    }

    /**
     * @param config the filter configuration
     * @throws ServletException if there is a problem
     */
    public final void init(final FilterConfig config) throws ServletException {
        if (config.getInitParameter(
                ClientParameterFilter.CONFIG_PARAMETER_NAME) != null) {
            this.parameterName
                = config.getInitParameter(
                        ClientParameterFilter.CONFIG_PARAMETER_NAME);
        }
    }
}
