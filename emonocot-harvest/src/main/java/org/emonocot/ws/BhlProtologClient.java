package org.emonocot.ws;

import java.io.BufferedReader;
import java.io.ByteArrayInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;

import org.apache.tika.Tika;
import org.apache.tika.exception.TikaException;
import org.codehaus.jackson.JsonParseException;
import org.codehaus.jackson.map.JsonMappingException;
import org.codehaus.jackson.map.ObjectMapper;
import org.codehaus.jackson.node.ArrayNode;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 *
 * @author ben
 *
 */
public class BhlProtologClient extends ProxyAwareWsClient {
    /**
    *
    */
    Logger logger = LoggerFactory.getLogger(BhlProtologClient.class);

    /**
     *
     */
    private static final int BHL_OCR_TEXT_INDEX = 4;

   /**
     *
     */
    private String webServiceUrl = "http://biodiversitylibrary.org/services/pagesummaryservice.ashx?op=FetchPageUrl&pageID=";

    /**
     *
     */
    private static final String BHL_PAGE_PREFIX = "http://biodiversitylibrary.org/page/";

    /**
     * @param newWebServiceUrl
     *            Set the web service url
     */
    public final void setWebServiceUrl(final String newWebServiceUrl) {
        this.webServiceUrl = newWebServiceUrl;
    }

    /**
     *
     */
    private ObjectMapper objectMapper = new ObjectMapper();

    /**
     *
     * @param bhlPage
     *            The bhl HTML page for the protolog
     * @return the protolog
     */
    public final String getProtolog(final String bhlPage) {
        String pageNumber = bhlPage.substring(BHL_PAGE_PREFIX.length());

        InputStream input = null;
        StringBuffer stringBuffer = new StringBuffer();
        try {
            input = getInputStream(webServiceUrl + pageNumber);
            BufferedReader reader = new BufferedReader(new InputStreamReader(
                    input));
            String inputLine = null;
            while ((inputLine = reader.readLine()) != null) {
                stringBuffer.append(inputLine);
            }
        } catch (IOException ioe) {
            throw new RuntimeException("Problem connecting to BHL", ioe);
        } finally {
            try {
                input.close();
            } catch (IOException e) {
            }
        }

        try {
            ArrayNode root = objectMapper.readValue(stringBuffer.toString()
                    .getBytes(), ArrayNode.class);
            Tika tika = new Tika();
            String html = "<html><body>"
                    + root.get(BHL_OCR_TEXT_INDEX).toString()
                    + "</body></html>";
            ByteArrayInputStream inputStream = new ByteArrayInputStream(
                    html.getBytes());
            String content = tika.parseToString(inputStream);
            inputStream.close();
            return content;
        } catch (JsonParseException jpe) {
            throw new RuntimeException(
                    "Problem parsing BHL Webservice JSON response", jpe);
        } catch (JsonMappingException jme) {
            throw new RuntimeException(
                    "Problem mapping JSON response to java object", jme);
        } catch (TikaException te) {
            throw new RuntimeException("Problem parsing protolog content", te);
        } catch (IOException ioe) {
            throw new RuntimeException(
                    "IOException converting bhl webservice response", ioe);
        }
    }
}
