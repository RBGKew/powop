package org.emonocot.portal.remoting;

import java.io.IOException;
import java.io.InputStreamReader;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.client.ClientHttpResponse;
import org.springframework.web.client.DefaultResponseErrorHandler;

/**
 *
 * @author ben
 *
 */
public class LoggingResponseErrorHandler extends DefaultResponseErrorHandler {
    /**
     *
     */
    private Logger logger = LoggerFactory
            .getLogger(LoggingResponseErrorHandler.class);
    /**
    *
    */
    static final int BUFFER = 2048;

    @Override
    public final void handleError(final ClientHttpResponse clientHttpResponse)
            throws IOException {
        InputStreamReader inputStreamReader = new InputStreamReader(
                clientHttpResponse.getBody());
        StringBuffer stringBuffer = new StringBuffer();
        char[] data = new char[BUFFER];
        while (inputStreamReader.read(data, 0, BUFFER) != -1) {
            stringBuffer.append(data);
        }

        inputStreamReader.close();
        logger.error(stringBuffer.toString());
    }
}
