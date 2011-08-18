package org.emonocot.ws.checklist;

import java.io.BufferedInputStream;
import java.io.BufferedOutputStream;
import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.net.HttpURLConnection;

import org.apache.http.HttpEntity;
import org.apache.http.HttpHost;
import org.apache.http.HttpResponse;
import org.apache.http.client.ClientProtocolException;
import org.apache.http.client.HttpClient;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.conn.params.ConnRoutePNames;
import org.apache.http.impl.client.DefaultHttpClient;
import org.emonocot.job.io.StaxEventItemReader;
import org.joda.time.DateTime;
import org.joda.time.DateTimeZone;
import org.joda.time.format.DateTimeFormatter;
import org.joda.time.format.ISODateTimeFormat;
import org.openarchives.pmh.ResumptionToken;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.batch.core.ExitStatus;
import org.springframework.batch.core.StepExecution;
import org.springframework.batch.core.StepExecutionListener;
import org.springframework.batch.item.ParseException;
import org.springframework.batch.item.UnexpectedInputException;
import org.springframework.core.io.FileSystemResource;
import org.springframework.oxm.Unmarshaller;

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
     * should be dateTimeNoMillis() but the grassbase webapp is not configured
     * properly (my fault!)
     */
    private static final DateTimeFormatter DATE_TIME_PRINTER = ISODateTimeFormat
            .dateTime();

    /**
    *
    */
    static final int BUFFER = 2048;

    /**
    *
    */
    private Logger logger = LoggerFactory.getLogger(OaiPmhClient.class);

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
     */
    private String servicesClientIdentifier;

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
     * @param servicesClientIdentifier
     *            Sets an identifier for this client sent in service operations
     */
    public final void setServicesClientIdentifier(
            final String servicesClientIdentifier) {
        this.servicesClientIdentifier = servicesClientIdentifier;
    }

    /**
    * 
    */
    private HttpClient httpClient = new DefaultHttpClient();

    /**
     *
     */
    private Unmarshaller unmarshaller;

    /**
     * 
     * @param newUnmarshaller
     *            Set the unmarshaller to use
     */
    public final void setUnmarshaller(final Unmarshaller newUnmarshaller) {
        this.unmarshaller = newUnmarshaller;
    }

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
     * @param set
     *            The string representation of a set (taxon) to harvest
     * @return An exit status indicating that the step was completed, failed, or
     *         if the authority responded with a NO RECORDS MATCH response
     *         indicating that no records have been modified
     */
    public final ExitStatus listRecords(final String authorityName,
            final String authorityURI, final String dateLastHarvested,
            final String temporaryFileName, final String resumptionToken,
            final String set) {
        if (proxyHost != null && proxyPort != null) {
            HttpHost proxy = new HttpHost(proxyHost, proxyPort);
            httpClient.getParams().setParameter(ConnRoutePNames.DEFAULT_PROXY,
                    proxy);
        }

        httpClient.getParams().setParameter("http.useragent",
                "org.emonocot.ws.checklist.OaiPmhClient");
        BufferedInputStream bufferedInputStream = null;
        BufferedOutputStream bufferedOutputStream = null;

        StringBuffer query = new StringBuffer("?");
        query.append("scratchpad=" + servicesClientIdentifier);

        if (resumptionToken != null && resumptionToken.length() > 0) {
            query.append("&resumptionToken=" + resumptionToken
                    + "&verb=ListRecords");
        } else {
            query.append("&verb=ListRecords&metadataPrefix=rdf");
            if (dateLastHarvested != null && dateLastHarvested.length() > 0) {
                DateTime from = new DateTime(Long.parseLong(dateLastHarvested));
                query.append("&from="
                        + DATE_TIME_PRINTER.print(from
                                .toDateTime(DateTimeZone.UTC)));
            }
            if (set != null && set.length() > 0) {
                query.append("&set=" + set);
            }
        }
        HttpGet httpGet = new HttpGet(authorityURI + query.toString());
        try {
            logger.info("Issuing " + httpGet.getRequestLine().toString());
            HttpResponse httpResponse = httpClient.execute(httpGet);
            logger.info("Got response "
                    + httpResponse.getStatusLine().toString());

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
                    while ((count = bufferedInputStream.read(data, 0, BUFFER)) != -1) {
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
                    logger.error("Input Output Exception closing inputStream for "
                            + authorityURI + " " + ioe.getLocalizedMessage());
                }
            }
            if (bufferedOutputStream != null) {
                try {
                    bufferedOutputStream.close();
                } catch (IOException ioe) {
                    logger.error("Input Output Exception closing outputStream for "
                            + authorityURI + " " + ioe.getLocalizedMessage());
                }
            }
        }
    }

    /**
     * 
     * @param temporaryFileName
     *            Set the temporary file name
     * @return an exit status indicating that the harvesting should continue or
     *         end now
     */
    public final ExitStatus resumptionTokenPresent(
            final String temporaryFileName) {

        try {
            StaxEventItemReader<ResumptionToken> staxEventItemReader = new StaxEventItemReader<ResumptionToken>();
            staxEventItemReader
                    .setFragmentRootElementName("{http://www.openarchives.org/OAI/2.0/}resumptionToken");
            staxEventItemReader.setUnmarshaller(unmarshaller);
            staxEventItemReader.setResource(new FileSystemResource(
                    temporaryFileName));

            staxEventItemReader.afterPropertiesSet();
            staxEventItemReader.open(stepExecution.getExecutionContext());

            ResumptionToken resumptionToken = staxEventItemReader.read();
            staxEventItemReader.close();
            if (resumptionToken == null) {
                stepExecution.getJobExecution().getExecutionContext()
                        .remove("resumption.token");
                return new ExitStatus("NO RESUMPTION TOKEN");
            } else {
                stepExecution.getJobExecution().getExecutionContext()
                        .remove("resumption.token");
                logger.info(resumptionToken.getValue() + " "
                        + resumptionToken.getCompleteListSize() + " "
                        + resumptionToken.getCursor());
                stepExecution.getJobExecution().getExecutionContext()
                        .put("resumption.token", resumptionToken.getValue());
                return new ExitStatus("RESUMPTION TOKEN PRESENT");
            }
        } catch (UnexpectedInputException e) {
            logger.error(e.getMessage());
            for (StackTraceElement ste : e.getStackTrace()) {
                logger.error(ste.toString());
            }
            return ExitStatus.FAILED;
        } catch (ParseException e) {
            logger.error(e.getMessage());
            for (StackTraceElement ste : e.getStackTrace()) {
                logger.error(ste.toString());
            }
            return ExitStatus.FAILED;
        } catch (Exception e) {
            logger.error(e.getMessage());
            for (StackTraceElement ste : e.getStackTrace()) {
                logger.error(ste.toString());
            }
            return ExitStatus.FAILED;
        }
    }

    /* (non-Javadoc)
     * @see org.springframework.batch.core.StepExecutionListener#afterStep(org.
     * springframework.batch.core.StepExecution)
     */
    public final ExitStatus afterStep(final StepExecution newStepExecution) {
        return null;
    }

    /* (non-Javadoc)
     * @see org.springframework.batch.core.StepExecutionListener#beforeStep(org.
     * springframework.batch.core.StepExecution)
     */
    public final void beforeStep(final StepExecution newStepExecution) {
        this.stepExecution = newStepExecution;
    }

}
