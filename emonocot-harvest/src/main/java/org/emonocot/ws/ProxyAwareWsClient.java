package org.emonocot.ws;

import java.io.IOException;
import java.io.InputStream;
import java.net.InetSocketAddress;
import java.net.MalformedURLException;
import java.net.Proxy;
import java.net.URL;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 *
 * @author ben
 *
 */
public abstract class ProxyAwareWsClient {
   /**
    *
    */
   private Logger logger = LoggerFactory.getLogger(ProxyAwareWsClient.class);

    /**
     *
     */
    private String proxyHost;

    /**
     *
     */
    private Integer proxyPort;

    /**
     *
     * @param newProxyHost
     *            Set the proxy host
     */
    public final void setProxyHost(final String newProxyHost) {
        this.proxyHost = newProxyHost;
    }

    /**
     *
     * @param newProxyPort
     *            Set the proxy port
     */
    public final void setProxyPort(final String newProxyPort) {
        try {
            this.proxyPort = Integer.decode(newProxyPort);
        } catch (NumberFormatException nfe) {
            logger.warn(nfe.getMessage());
        }
    }

    /**
     *
     * @param urlString
     *            the url you would like to open
     * @return an input stream
     */
    protected final InputStream getInputStream(final String urlString) {
        URL url = null;
        try {
            url = new URL(urlString);
            if (proxyHost != null && proxyPort != null) {
                Proxy proxy = new Proxy(Proxy.Type.HTTP, new InetSocketAddress(
                        proxyHost, proxyPort));
                return url.openConnection(proxy).getInputStream();
            } else {
                return url.openStream();
            }
        } catch (MalformedURLException e) {
            throw new IllegalArgumentException(urlString
                    + " is not a valid page url");
        } catch (IOException ioe) {
            throw new RuntimeException(
                    "IOException connecting to " + urlString, ioe);
        }
    }

}