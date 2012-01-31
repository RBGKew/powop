package org.emonocot.common.logback;

import java.io.File;
import java.io.FileNotFoundException;
import java.net.URL;

import org.slf4j.LoggerFactory;
import org.springframework.util.ResourceUtils;
import org.springframework.util.SystemPropertyUtils;

import ch.qos.logback.classic.LoggerContext;
import ch.qos.logback.classic.joran.JoranConfigurator;
import ch.qos.logback.core.joran.spi.JoranException;
import ch.qos.logback.core.util.StatusPrinter;

/**
 * Convenience class that features simple methods for custom Log4J
 * configuration.
 *
 * <p>
 * Only needed for non-default Logback initialization with a custom config
 * location. By default, Logback will simply read its configuration from a
 * "logback.xml" or "logback_test.xml" file in the root of the class path.</p>
 *
 * <p>
 * For web environments, the analogous LogbackWebConfigurer class can be found
 * in the web package, reading in its configuration from context-params in
 * web.xml. In a J2EE web application, Logback is usually set up via
 * LogbackConfigListener or LogbackConfigServlet, delegating to
 * LogbackWebConfigurer underneath.</p>
 *
 * @author Juergen Hoeller
 * @author Davide Baroncelli
 * @since 27-set-2007 11.42.07
 * @see LogbackWebConfigurer
 * @see LogbackConfigListener
 * @see LogbackConfigServlet
 */
public final class LogbackConfigurer {
    /**
     *
     */
    private LogbackConfigurer() {
    }

    /**
     * Initialize logback from the given file.
     *
     * @param location
     *            the location of the config file: either a "classpath:"
     *            location (e.g. "classpath:logback.xml"), an absolute file URL
     *            (e.g. "file:C:/logback.xml), or a plain absolute path in the
     *            file system (e.g. "C:/logback.xml")
     * @throws java.io.FileNotFoundException
     *             if the location specifies an invalid file path
     */
    public static void initLogging(final String location)
            throws FileNotFoundException {
        String resolvedLocation = SystemPropertyUtils
                .resolvePlaceholders(location);
        URL url = ResourceUtils.getURL(resolvedLocation);
        LoggerContext loggerContext = (LoggerContext) LoggerFactory
                .getILoggerFactory();

        try {
            JoranConfigurator configurator = new JoranConfigurator();
            configurator.setContext(loggerContext);
            // the context was probably already configured by default
            // configuration
            // rules
            loggerContext.reset();
            configurator.doConfigure(url);
        } catch (JoranException je) {
            // StatusPrinter will handle this
        }
        StatusPrinter.printInCaseOfErrorsOrWarnings(loggerContext);
    }

    /**
     * Shut down logback.
     * <p>
     * This isn't strictly necessary, but recommended for shutting down logback
     * in a scenario where the host VM stays alive (for example, when shutting
     * down an application in a J2EE environment).
     */
    public static void shutdownLogging() {
        LoggerContext loggerContext = (LoggerContext) LoggerFactory
                .getILoggerFactory();
        loggerContext.stop();
    }

    /**
     * Set the specified system property to the current working directory.
     * <p>
     * This can be used e.g. for test environments, for applications that
     * leverage LogbackWebConfigurer's "webAppRootKey" support in a web
     * environment.
     *
     * @param key
     *            system property key to use, as expected in Logback
     *            configuration (for example: "demo.root", used as
     *            "${demo.root}/WEB-INF/demo.log")
     * @see LogbackWebConfigurer
     */
    public static void setWorkingDirSystemProperty(final String key) {
        System.setProperty(key, new File("").getAbsolutePath());
    }
}
