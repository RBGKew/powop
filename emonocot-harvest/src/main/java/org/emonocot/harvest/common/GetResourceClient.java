package org.emonocot.harvest.common;

import java.io.BufferedInputStream;
import java.io.BufferedOutputStream;
import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.OutputStreamWriter;
import java.net.HttpURLConnection;
import java.util.Date;

import org.apache.http.HttpEntity;
import org.apache.http.HttpHost;
import org.apache.http.HttpResponse;
import org.apache.http.client.ClientProtocolException;
import org.apache.http.client.HttpClient;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.conn.params.ConnRoutePNames;
import org.apache.http.entity.BufferedHttpEntity;
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
     */
    private String proxyHost;

    /**
     *
     */
    private Integer proxyPort;

    /**
     *
     * @param newProxyHost Set the proxy host
     */
    public final void setProxyHost(final String newProxyHost) {
        this.proxyHost = newProxyHost;
    }

    /**
     *
     * @param newProxyPort Set the proxy port
     */
    public final void setProxyPort(final String newProxyPort) {
		if (newProxyPort != null && !newProxyPort.isEmpty()) {
			try {
				this.proxyPort = Integer.decode(newProxyPort);
			} catch (NumberFormatException nfe) {
				logger.warn(nfe.getMessage());
			}
		}
    }

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
     * Source may respond with the HTTP status 304 NOT MODIFIED, in which
     * case the method will return an ExitStatus with an exit code 'NOT
     * MODIFIED' and the job will terminate.
     *
     * If the resource has been modified, the client will save the response in a
     * document specified in temporaryFileName and will an ExitStatus with an
     * exit code 'COMPLETE'.
     *
     * @param authorityName
     *            The name of the Source being harvested.
     * @param authorityURI
     *            The endpoint (uri) being harvested.
     * @param dateLastHarvested
     *            The dateTime when this Source was last harvested.
     * @param temporaryFileName
     *            The name of the temporary file to store the response in
     * @return An exit status indicating that the step was completed, failed, or
     *         if the Source responded with a 304 NOT MODIFIED response
     *         indicating that no records have been modified
     */
    public final ExitStatus getResource(final String authorityName,
            final String authorityURI, final String dateLastHarvested,
            final String temporaryFileName) {

        logger.debug(authorityName + " " + authorityURI);
        if (proxyHost != null && proxyPort != null) {
            HttpHost proxy = new HttpHost(proxyHost, proxyPort);
            httpClient.getParams()
                .setParameter(ConnRoutePNames.DEFAULT_PROXY, proxy);
        }

        httpClient.getParams().setParameter("http.useragent",
                "org.emonocot.ws.GetResourceClient");
        InputStreamReader inputStreamReader = null;
        OutputStreamWriter outputStreamWriter = null;

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
                    inputStreamReader = new InputStreamReader(
                        new BufferedInputStream(entity.getContent()),
                        "UTF-8");
                    outputStreamWriter = new OutputStreamWriter(
                        new BufferedOutputStream(new FileOutputStream(
                            new File(temporaryFileName))), "UTF-8");
                    int count;
                    char[] data = new char[BUFFER];
                    while ((count
                          = inputStreamReader.read(data, 0, BUFFER)) != -1) {
                          outputStreamWriter.write(data, 0, count);
                    }
                    outputStreamWriter.flush();
                    outputStreamWriter.close();
                    inputStreamReader.close();
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
            if (inputStreamReader != null) {
                try {
                    inputStreamReader.close();
                } catch (IOException ioe) {
                    logger.error(
                            "Input Output Exception closing inputStream for "
                            + authorityURI + " " + ioe.getLocalizedMessage());
                }
            }
            if (outputStreamWriter != null) {
                try {
                     outputStreamWriter.close();
                } catch (IOException ioe) {
                    logger.error(
                            "Input Output Exception closing outputStream for "
                            + authorityURI + " " + ioe.getLocalizedMessage());
                }
            }
        }
    }

    /**
     * Executes a HTTP GET request with the If-Modified-Since header set to
     * dateLastHarvested. If the resource has not been modified then the
     * Source may respond with the HTTP status 304 NOT MODIFIED, in which
     * case the method will return an ExitStatus with an exit code 'NOT
     * MODIFIED' and the job will terminate.
     *
     * If the resource has been modified, the client will save the response in a
     * document specified in temporaryFileName and will an ExitStatus with an
     * exit code 'COMPLETE'.
     *
     * @param authorityName
     *            The name of the Source being harvested.
     * @param authorityURI
     *            The endpoint (uri) being harvested.
     * @param dateLastHarvested
     *            The dateTime when this Source was last harvested.
     * @param temporaryFileName
     *            The name of the temporary file to store the response in
     * @return An exit status indicating that the step was completed, failed, or
     *         if the Source responded with a 304 NOT MODIFIED response
     *         indicating that no records have been modified
     */
    public final ExitStatus getBinaryResource(final String authorityName,
            final String authorityURI, final String dateLastHarvested,
            final String temporaryFileName) {
        if (proxyHost != null && proxyPort != null) {
            HttpHost proxy = new HttpHost(proxyHost, proxyPort);
            httpClient.getParams()
                .setParameter(ConnRoutePNames.DEFAULT_PROXY, proxy);
        }

        httpClient.getParams().setParameter("http.useragent",
                "org.emonocot.ws.GetResourceClient");
        BufferedInputStream bufferedInputStream = null;
        BufferedOutputStream bufferedOutputStream = null;

        HttpGet httpGet = new HttpGet(authorityURI.replace(" ", "%20"));
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
                      bufferedInputStream =
                          new BufferedInputStream(entity.getContent());
                      bufferedOutputStream =
                          new BufferedOutputStream(new FileOutputStream(
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
                BufferedHttpEntity bufferedEntity = new BufferedHttpEntity(
                        httpResponse.getEntity());
                InputStreamReader reader = new InputStreamReader(
                        bufferedEntity.getContent());
                StringBuffer stringBuffer = new StringBuffer();
                int count;
                char[] content = new char[BUFFER];
                while ((count = reader.read(content, 0, BUFFER)) != -1) {
                    stringBuffer.append(content);
                }
                logger.info("Server Response was: " + stringBuffer.toString());
                httpGet.abort();
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
