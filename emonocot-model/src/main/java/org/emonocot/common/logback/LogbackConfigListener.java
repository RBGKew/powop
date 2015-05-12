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
package org.emonocot.common.logback;

import javax.servlet.ServletContextEvent;
import javax.servlet.ServletContextListener;

/**
 * Bootstrap listener for custom Logback initialization in a web environment.
 * Delegates to LogbackWebConfigurer (see its javadoc for configuration
 * details).
 * <p/>
 * <b>WARNING: Assumes an expanded WAR file</b>, both for loading the
 * configuration file and for writing the log files. If you want to keep your
 * WAR unexpanded or don't need application-specific log files within the WAR
 * directory, don't use Logback setup within the application (thus, don't use
 * Log4jConfigListener or LogbackConfigServlet). Instead, use a global, VM-wide
 * Log4J setup (for example, in JBoss) or JDK 1.4's
 * <code>java.util.logging</code> (which is global too).
 * <p/>
 * <p>
 * This listener should be registered before ContextLoaderListener in web.xml,
 * when using custom Logback initialization.
 * <p/>
 * <p>
 * For Servlet 2.2 containers and Servlet 2.3 ones that do not initalize
 * listeners before servlets, use Log4jConfigServlet. See the
 * ContextLoaderServlet javadoc for details.</p>
 *
 * @author Juergen Hoeller
 * @author Davide Baroncelli
 * @since 27-set-2007 13.57.04
 * @see LogbackWebConfigurer
 * @see LogbackConfigListener
 * @see org.springframework.web.context.ContextLoaderListener
 * @see org.springframework.web.context.ContextLoaderServlet
 * @see org.springframework.web.util.WebAppRootListener
 */
public class LogbackConfigListener implements ServletContextListener {

    /**
     * @param event Set the servlet context event
     */
    public final void contextInitialized(final ServletContextEvent event) {
        LogbackWebConfigurer.initLogging(event.getServletContext());
    }

    /**
     * @param event Set the servlet context event
     */
    public final void contextDestroyed(final ServletContextEvent event) {
        LogbackWebConfigurer.shutdownLogging(event.getServletContext());
    }

}
