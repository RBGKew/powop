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
package org.emonocot.portal.logging;

import java.io.IOException;

import javax.servlet.Filter;
import javax.servlet.FilterChain;
import javax.servlet.FilterConfig;
import javax.servlet.ServletException;
import javax.servlet.ServletRequest;
import javax.servlet.ServletResponse;
import javax.servlet.http.HttpServletResponse;

import org.slf4j.MDC;
import org.springframework.http.HttpStatus;

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
    private FilterConfig filterConfig = null;

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
        if (request.getParameter(parameterName) != null
                && request.getParameter(parameterName).trim().length()>0) {
            MDC.put(LoggingConstants.MDC_CLIENT_NAME_KEY,
                    request.getParameter(parameterName));
            try {
                chain.doFilter(request, response);
            } finally {
                MDC.remove(LoggingConstants.MDC_CLIENT_NAME_KEY);
            }
        } else {
            ((HttpServletResponse) response).sendError(
                    HttpStatus.BAD_REQUEST.value(),
                    "Required parameter "
                    + parameterName + " not present in request");
            return;
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
