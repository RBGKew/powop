package org.emonocot.ws.checklist;

import java.io.BufferedInputStream;
import java.io.BufferedOutputStream;
import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.net.HttpURLConnection;

import org.apache.http.HttpEntity;
import org.apache.http.HttpResponse;
import org.apache.http.client.ClientProtocolException;
import org.apache.http.client.HttpClient;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.impl.client.DefaultHttpClient;
import org.emonocot.ws.GetResourceClient;
import org.joda.time.DateTime;
import org.joda.time.format.DateTimeFormatter;
import org.joda.time.format.ISODateTimeFormat;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.batch.core.ExitStatus;
import org.springframework.batch.core.StepExecution;
import org.springframework.batch.core.StepExecutionListener;

/**
 *
 * @author ben
 *
 */
public class OaiPmhClient implements StepExecutionListener {
   /**
    *
    */
    private StepExecution stepExecution;

    /**
     *
     */
    private static final DateTimeFormatter DATE_TIME_PRINTER
        = ISODateTimeFormat.dateTimeNoMillis();

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
     * @param newHttpClient
     *            Set the http client instance to use.
     */
    public final void setHttpClient(final HttpClient newHttpClient) {
        this.httpClient = newHttpClient;
    }

    /**
     *
     * @param authorityName
     *            The name of the authority being harvested.
     * @param authorityURI
     *            The endpoint (uri) being harvested.
     * @param dateLastHarvested
     *            The dateTime when this authority was last harvested.
     * @param temporaryFileName
     *            The name of the temporary file to store the response in.
     * @param resumptionToken
     *            The resumption token if present.
     * @return An exit status indicating that the step was completed, failed, or
     *         if the authority responded with a NO RECORDS MATCH response
     *         indicating that no records have been modified
     */
    public final ExitStatus listRecords(final String authorityName,
            final String authorityURI, final String dateLastHarvested,
            final String temporaryFileName, final String resumptionToken) {


        httpClient.getParams().setParameter("http.useragent",
                "org.emonocot.ws.checklist.OaiPmhClient");
        BufferedInputStream bufferedInputStream = null;
        BufferedOutputStream bufferedOutputStream = null;

        HttpGet httpGet = new HttpGet(authorityURI);

        if (resumptionToken != null) {
            httpGet.getParams().setParameter("resumptionToken",
                    resumptionToken);
        } else {
            if (dateLastHarvested != null) {
                httpGet.getParams().setParameter(
                        "from",
                        DATE_TIME_PRINTER.print(new DateTime(Long
                                .parseLong(dateLastHarvested))));
            }
            httpGet.getParams().setParameter("verb", "ListRecords");
            httpGet.getParams().setParameter("metadataPrefix", "rdf");
        }

        try {
            HttpResponse httpResponse = httpClient.execute(httpGet);
            logger.info("Issuing " + httpGet.getRequestLine().toString()
                    + " response " + httpResponse.getStatusLine().toString());

            switch (httpResponse.getStatusLine().getStatusCode()) {
            case HttpURLConnection.HTTP_OK:
                HttpEntity entity = httpResponse.getEntity();
                if (entity != null) {
                    bufferedInputStream = new BufferedInputStream(
                            entity.getContent());
                    bufferedOutputStream = new BufferedOutputStream(
                            new FileOutputStream(new File(temporaryFileName)));
                    int count;
                    byte[] data = new byte[BUFFER];
                    while ((count = bufferedInputStream.read(
                            data, 0, BUFFER)) != -1) {
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

    /**
     *
     * @param resumptionToken
     *            Set the resumption token, or null
     * @return an exit status indicating that the harvesting should continue or
     *         end now
     */
    public final ExitStatus resumptionTokenPresent(
            final String resumptionToken) {
        if (resumptionToken == null) {
            return new ExitStatus("NO RESUMPTION TOKEN");
        } else {
            return new ExitStatus("RESUMPTION TOKEN PRESENT");
        }
    }

    @Override
    public final ExitStatus afterStep(final StepExecution newStepExecution) {
        return null;
    }

    @Override
    public final void beforeStep(final StepExecution newStepExecution) {
        this.stepExecution = newStepExecution;
    }

}
