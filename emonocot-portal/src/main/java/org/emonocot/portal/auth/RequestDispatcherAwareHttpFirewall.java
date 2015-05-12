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
package org.emonocot.portal.auth;

import java.io.IOException;

import javax.servlet.RequestDispatcher;
import javax.servlet.ServletException;
import javax.servlet.ServletRequest;
import javax.servlet.ServletResponse;
import javax.servlet.http.HttpServletRequest;

import org.springframework.security.web.firewall.DefaultHttpFirewall;
import org.springframework.security.web.firewall.FirewalledRequest;

/**
 *
 * @author ben
 *
 */
public class RequestDispatcherAwareHttpFirewall extends DefaultHttpFirewall {
    /**
     * @param request set the servlet request
     * @return the firewalled request
     */
    public final FirewalledRequest getFirewalledRequest(
            final HttpServletRequest request) {
        return new ForwardAndIncludeAwareHttpFirewalledRequest(
                super.getFirewalledRequest(request));
    }

    /**
     *
     * @author ben
     *
     */
    private static class ForwardAndIncludeAwareHttpFirewalledRequest extends
            FirewalledRequest {
        /**
         *
         * @param request Set the firewalled request
         */
        public ForwardAndIncludeAwareHttpFirewalledRequest(
                final FirewalledRequest request) {
            super(request);
        }

        /**
         * @param path set the path
         * @return a request dispatcher
         */
        public RequestDispatcher getRequestDispatcher(final String path) {
            return new FirewalledRequestAwareRequestDispatcher(path);
        }

        /**
         *
         */
        public void reset() {
            getFirewalledRequest().reset();
        }

        /**
         *
         * @return the firewalled request
         */
        private FirewalledRequest getFirewalledRequest() {
            return (FirewalledRequest) getRequest();
        }

        /**
         *
         * @author ben
         *
         */
        private class FirewalledRequestAwareRequestDispatcher implements
                RequestDispatcher {
            /**
             *
             */
            private final String path;

            /**
             *
             * @param newPath Set the path
             */
            public FirewalledRequestAwareRequestDispatcher(
                    final String newPath) {
                this.path = newPath;
            }

            /**
             * @param request
             *            Set the servlet request
             * @param response
             *            Set the servlet response
             * @throws IOException
             *             if there is a problem recieving or sending data
             * @throws ServletException
             *             if there is a problem forwarding the request
             */
            public void forward(final ServletRequest request,
                    final ServletResponse response) throws ServletException,
                    IOException {
                reset();
                getDispatcher().forward(request, response);
            }

            /**
             * @param request
             *            Set the servlet request
             * @param response
             *            Set the servlet response
             * @throws IOException
             *             if there is a problem recieving or sending data
             * @throws ServletException
             *             if there is a problem including the request
             */
            public void include(final ServletRequest request,
                    final ServletResponse response) throws ServletException,
                    IOException {
                getDispatcher().include(request, response);
            }

            /**
             *
             * @return the request dispatcher
             */
            private RequestDispatcher getDispatcher() {
                return ForwardAndIncludeAwareHttpFirewalledRequest.super
                        .getRequestDispatcher(path);
            }
        }
    }
}
