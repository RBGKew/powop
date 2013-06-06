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
