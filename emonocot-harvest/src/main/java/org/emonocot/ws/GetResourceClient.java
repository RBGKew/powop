package org.emonocot.ws;

import java.io.BufferedInputStream;
import java.io.BufferedOutputStream;
import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.net.HttpURLConnection;
import java.util.Date;

import org.apache.http.HttpEntity;
import org.apache.http.HttpResponse;
import org.apache.http.client.ClientProtocolException;
import org.apache.http.client.HttpClient;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.impl.client.DefaultHttpClient;
import org.apache.http.impl.cookie.DateUtils;
import org.apache.http.message.BasicHeader;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.batch.core.ExitStatus;

/**
 *
 * @author ben
 *
 */
public class GetResourceClient {
   /**
    *
    */
    static final int BUFFER = 2048;

    /**
     *
     */
    private Logger logger = LoggerFactory.getLogger(GetResourceClient.class);

    /**
     *
     */
    private HttpClient httpClient = new DefaultHttpClient();

    /**
     *
     * @param newHttpClient Set the http client instance to use.
     */
    public final void setHttpClient(final HttpClient newHttpClient) {
        this.httpClient = newHttpClient;
    }

    /**
     * Executes a HTTP GET request with the If-Modified-Since header set to
     * dateLastHarvested. If the resource has not been modified then the
     * authority may respond with the HTTP status 304 NOT MODIFIED, in which
     * case the method will return an ExitStatus with an exit code 'NOT
     * MODIFIED' and the job will terminate.
     *
     * If the resource has been modified, the client will save the response in a
     * document specified in temporaryFileName and will an ExitStatus with an
     * exit code 'COMPLETE'.
     *
     * @param authorityName
     *            The name of the authority being harvested.
     * @param authorityURI
     *            The endpoint (uri) being harvested.
     * @param dateLastHarvested
     *            The dateTime when this authority was last harvested.
     * @param temporaryFileName
     *            The name of the temporary file to store the response in
     * @return An exit status indicating that the step was completed, failed, or
     *         if the authority responded with a 304 NOT MODIFIED response
     *         indicating that no records have been modified
     */
    public final ExitStatus getResource(final String authorityName,
            final String authorityURI, final String dateLastHarvested,
            final String temporaryFileName) {
        httpClient.getParams().setParameter("http.useragent",
                "org.emonocot.ws.scratchpads.GetXmlDocClient");
        BufferedInputStream bufferedInputStream = null;
        BufferedOutputStream bufferedOutputStream = null;

        HttpGet httpGet = new HttpGet(authorityURI);
        httpGet.addHeader(new BasicHeader("If-Modified-Since", DateUtils
                .formatDate(new Date(Long.parseLong(dateLastHarvested)))));
        try {
            HttpResponse httpResponse = httpClient.execute(httpGet);

            switch(httpResponse.getStatusLine().getStatusCode()) {
              case HttpURLConnection.HTTP_NOT_MODIFIED:
                  return new ExitStatus("NOT MODIFIED");
              case HttpURLConnection.HTTP_OK:
                  HttpEntity entity = httpResponse.getEntity();
                  if (entity != null) {
                    bufferedInputStream
                        = new BufferedInputStream(entity.getContent());
                    bufferedOutputStream
                        = new BufferedOutputStream(new FileOutputStream(
                            new File(temporaryFileName)));
                    int count;
                    byte[] data = new byte[BUFFER];
                    while ((count
                          = bufferedInputStream.read(data, 0, BUFFER)) != -1) {
                          bufferedOutputStream.write(data, 0, count);
                    }
                    bufferedOutputStream.flush();
                    bufferedOutputStream.close();
                    bufferedInputStream.close();
                  } else {
                    logger.info("Server returned "
                            + httpResponse.getStatusLine()
                            + " but HttpEntity is null");
                      return ExitStatus.FAILED;
                  }
                  return ExitStatus.COMPLETED;
              default:
                logger.info("Server returned unexpected status code "
                        + httpResponse.getStatusLine() + " for document "
                        + authorityURI); // This is not an error in this
                                         // application but a server side error
                  return ExitStatus.FAILED;
            }

        } catch (ClientProtocolException cpe) {
            logger.error("Client Protocol Exception getting document "
                    + authorityURI + " " + cpe.getLocalizedMessage());
            return ExitStatus.FAILED;
        } catch (IOException ioe) {
            logger.error("Input Output Exception getting document "
                    + authorityURI + " " + ioe.getLocalizedMessage());
            return ExitStatus.FAILED;
        } finally {
            if (bufferedInputStream != null) {
                try {
                    bufferedInputStream.close();
                } catch (IOException ioe) {
                    logger.error(
                            "Input Output Exception closing inputStream for "
                            + authorityURI + " " + ioe.getLocalizedMessage());
                }
            }
            if (bufferedOutputStream != null) {
                try {
                     bufferedOutputStream.close();
                } catch (IOException ioe) {
                    logger.error(
                            "Input Output Exception closing outputStream for "
                            + authorityURI + " " + ioe.getLocalizedMessage());
                }
            }
        }
    }

}
