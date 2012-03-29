package org.emonocot.portal.http;

import java.io.IOException;
import java.net.InetSocketAddress;
import java.net.Proxy;
import java.net.URI;

import org.apache.commons.codec.binary.Base64;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.HttpMethod;
import org.springframework.http.client.ClientHttpRequest;
import org.springframework.http.client.SimpleClientHttpRequestFactory;

/**
 *
 * @author ben
 *
 */
public class StaticUsernameAndPasswordHttpClientFactory extends
        SimpleClientHttpRequestFactory {

    /**
     *
     */
    private Logger logger = LoggerFactory.getLogger(StaticUsernameAndPasswordHttpClientFactory.class);

    /**
     *
     */
    private String username;

    /**
     *
     */
    private String password;

    /**
     *
     */
    private Integer proxyPort;

    /**
     *
     */
    private String proxyHost;

    /**
     * @param newProxyPort the proxyPort to set
     */
    public final void setProxyPort(final String newProxyPort) {
        try {
            this.proxyPort = Integer.decode(newProxyPort);
        } catch (NumberFormatException nfe) {
            logger.warn(nfe.getMessage());
        }
    }

    /**
     * @param newProxyHost the proxyHost to set
     */
    public final void setProxyHost(final String newProxyHost) {
        this.proxyHost = newProxyHost;
    }

    /**
     * @param newUsername the username to set
     */
    public final void setUsername(final String newUsername) {
        this.username = newUsername;
    }

    /**
     * @param newPassword the password to set
     */
    public final void setPassword(final String newPassword) {
        this.password = newPassword;
    }

    /**
     * @param uri Set the uri
     * @param httpMethod set the httpMethod
     * @return a client http request object
     * @throws IOException if there is a problem
     */
    public final ClientHttpRequest createRequest(final URI uri,
            final HttpMethod httpMethod) throws IOException {
        if (proxyHost != null && proxyPort != null) {
            Proxy proxy = new Proxy(Proxy.Type.HTTP, new InetSocketAddress(
                    proxyHost, proxyPort));
            super.setProxy(proxy);
        }
        ClientHttpRequest clientHttpRequest = super.createRequest(uri,
                httpMethod);
        String unencoded = username + ":" + password;
        String encoded = new String(Base64.encodeBase64(unencoded.getBytes()));
        clientHttpRequest.getHeaders().add("Authorization", "Basic " + encoded);

        return clientHttpRequest;
    }
}
