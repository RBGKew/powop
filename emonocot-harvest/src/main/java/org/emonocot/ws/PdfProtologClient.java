package org.emonocot.ws;

import java.io.IOException;
import java.io.InputStream;

import org.apache.tika.Tika;
import org.apache.tika.exception.TikaException;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 *
 * @author ben
 *
 */
public class PdfProtologClient extends ProxyAwareWsClient {
    /**
    *
    */
    private Logger logger = LoggerFactory.getLogger(PdfProtologClient.class);

    /**
     *
     */
    private Tika tika = new Tika();

    /**
     *
     * @param identifier
     *            The url for the pdf
     * @return the protolog
     */
    public final String getProtolog(final String identifier) {
        InputStream input = null;
        try {
            input = super.getInputStream(identifier);
            String content = tika.parseToString(input);
            input.close();
            return content;
        } catch (IOException ioe) {
            throw new RuntimeException("Problem getting pdf content", ioe);
        } catch (TikaException te) {
            throw new RuntimeException("Problem parsing pdf", te);
        } finally {
            try {
            input.close();
            } catch (IOException ioe) { }
        }
    }
}
