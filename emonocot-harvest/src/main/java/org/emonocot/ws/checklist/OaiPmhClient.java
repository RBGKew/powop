package org.emonocot.ws.checklist;

import java.io.BufferedInputStream;
import java.io.BufferedOutputStream;
import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;

import org.apache.http.HttpEntity;
import org.apache.http.HttpHost;
import org.apache.http.HttpResponse;
import org.apache.http.client.ClientProtocolException;
import org.apache.http.client.HttpClient;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.conn.params.ConnRoutePNames;
import org.apache.http.entity.BufferedHttpEntity;
import org.apache.http.impl.client.DefaultHttpClient;
import org.apache.http.impl.conn.tsccm.ThreadSafeClientConnManager;
import org.apache.http.params.HttpConnectionParams;
import org.apache.http.params.HttpParams;
import org.apache.http.params.HttpProtocolParams;
import org.emonocot.job.io.StaxEventItemReader;
import org.joda.time.DateTime;
import org.joda.time.DateTimeZone;
import org.joda.time.format.DateTimeFormatter;
import org.joda.time.format.ISODateTimeFormat;
import org.openarchives.pmh.OAIPMH;
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
     *
     */
    private static final DateTimeFormatter DATE_TIME_PRINTER = ISODateTimeFormat
            .dateTimeNoMillis();

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
        if (newProxyHost != null && newProxyHost != "") {
            this.proxyHost = newProxyHost;
        }
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
    private HttpClient httpClient = new DefaultHttpClient(
            new ThreadSafeClientConnManager());

    /**
     *
     */
    private Unmarshaller unmarshaller;

    /**
     *
     */
    private int connectionTimeoutMillis = 180 * 1000;

    /**
     *
     */
    private int socketTimeoutMillis = 360 * 1000;

    /**
     *
     * @param newSocketTimeout Set the socket timeout
     */
    public final void setSocketTimeoutMillis(final Integer newSocketTimeout) {
        this.socketTimeoutMillis = newSocketTimeout;
    }

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
     *            The name of the Source being harvested.
     * @param authorityUri
     *            The endpoint (uri) being harvested.
     * @param authorityLastHarvested
     *            The dateTime when this Source was last harvested.
     * @param temporaryFileName
     *            The name of the temporary file to store the response in.
     * @param requestSubsetName
     *            The string representation of a set (taxon) to harvest
     * @return An exit status indicating that the step was completed, failed, or
     *         if the Source responded with a NO RECORDS MATCH response
     *         indicating that no records have been modified
     */
    public final ExitStatus listRecords(final String authorityName,
            final String authorityUri, final String authorityLastHarvested,
            final String temporaryFileName, final String requestSubsetName) {
        System.out.println(authorityName +  " | " + authorityUri+  " | " + temporaryFileName
                +  " | " + authorityLastHarvested+  " | " + requestSubsetName);
        logger.info("ProxyHost " + proxyHost + " ProxyPort " + proxyPort);
        if (proxyHost != null && proxyPort != null) {
            logger.info("Setting Proxy");
            HttpHost proxy = new HttpHost(proxyHost, proxyPort);
            httpClient.getParams().setParameter(ConnRoutePNames.DEFAULT_PROXY,
                    proxy);
        }
        HttpParams httpParams = httpClient.getParams();
        HttpProtocolParams.setUserAgent(httpParams,
                "org.emonocot.ws.checklist.OaiPmhClient");
        HttpConnectionParams.setConnectionTimeout(httpParams,
                connectionTimeoutMillis);
        HttpConnectionParams.setSoTimeout(httpParams, socketTimeoutMillis);
        BufferedInputStream bufferedInputStream = null;
        BufferedOutputStream bufferedOutputStream = null;

        StringBuffer query = new StringBuffer("?");
        query.append("scratchpad=" + servicesClientIdentifier);
        String resumptionToken = null;
        if (stepExecution.getJobExecution().getExecutionContext()
                .containsKey("resumption.token")) {
            resumptionToken = (String) stepExecution.getJobExecution()
                .getExecutionContext().get("resumption.token");
        }
        logger.info("Source name: " + authorityName
                + " Source URI: " + authorityUri
                + " date: " + authorityLastHarvested
                + " tempFile: " + temporaryFileName
                + " resumptionToken: " + resumptionToken
                + "set: " + requestSubsetName);

        if (resumptionToken != null && resumptionToken.length() > 0
                && !resumptionToken.equals("null")) {
            query.append("&resumptionToken=" + resumptionToken
                    + "&verb=ListRecords");
        } else {
            query.append("&verb=ListRecords&metadataPrefix=rdf");
            if (authorityLastHarvested != null && authorityLastHarvested.length() > 0
                    && !authorityLastHarvested.equals("null")) {
                DateTime from = new DateTime(Long.parseLong(authorityLastHarvested));
                query.append("&from="
                        + DATE_TIME_PRINTER.print(from
                                .toDateTime(DateTimeZone.UTC)));
            }
            if (requestSubsetName != null && requestSubsetName.length() > 0) {
                query.append("&set=" + requestSubsetName);
            }
        }
        HttpGet httpGet = new HttpGet(authorityUri + query.toString());
        try {
            logger.info("Issuing " + httpGet.getRequestLine().toString());
            HttpResponse httpResponse = httpClient.execute(httpGet);
            logger.info("Got response "
                    + httpResponse.getStatusLine().toString());

            switch (httpResponse.getStatusLine().getStatusCode()) {
            case HttpURLConnection.HTTP_OK:
                logger.info("Got Status 200 OK");
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
                        + authorityUri); // This is not an error in this
                                         // application but a server side error
                BufferedHttpEntity bufferedEntity
                    = new BufferedHttpEntity(httpResponse.getEntity());
                InputStreamReader reader
                    = new InputStreamReader(bufferedEntity.getContent());
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
                    + authorityUri + " " + cpe.getLocalizedMessage());
            return ExitStatus.FAILED;
        } catch (IOException ioe) {
            logger.error("Input Output Exception getting document "
                    + authorityUri + " " + ioe.getLocalizedMessage());
            return ExitStatus.FAILED;
        } finally {
            if (bufferedInputStream != null) {
                try {
                    bufferedInputStream.close();
                } catch (IOException ioe) {
                    logger.error(
                            "Input Output Exception closing inputStream for "
                            + authorityUri + " " + ioe.getLocalizedMessage());
                }
            }
            if (bufferedOutputStream != null) {
                try {
                    bufferedOutputStream.close();
                } catch (IOException ioe) {
                    logger.error(
                            "Input Output Exception closing outputStream for "
                            + authorityUri + " " + ioe.getLocalizedMessage());
                }
            }
        }
    }

   /**
    *
    * @param recordIdentifier The identifier of the record you want to get
    *
    * @param authorityName
    *            The name of the Source being harvested.
    * @param authorityUri
    *            The endpoint (uri) being harvested.
    * @param temporaryFileName
    *            The name of the temporary file to store the response in.
    * @return An exit status indicating that the step was completed, failed, or
    *         if the Source responded with a NO RECORDS MATCH response
    *         indicating that no records have been modified
    * @throws Exception if there is a problem
    */
    public final ExitStatus getRecord(
            final String recordIdentifier,
            final String authorityName, final String authorityUri,
            final String temporaryFileName) throws Exception {
       if (proxyHost != null && proxyPort != null) {
           HttpHost proxy = new HttpHost(proxyHost, proxyPort);
           httpClient.getParams().setParameter(ConnRoutePNames.DEFAULT_PROXY,
                   proxy);
       }

       HttpParams httpParams = httpClient.getParams();
       HttpProtocolParams.setUserAgent(httpParams,
               "org.emonocot.ws.checklist.OaiPmhClient");
       HttpConnectionParams.setConnectionTimeout(httpParams,
               connectionTimeoutMillis);
       HttpConnectionParams.setSoTimeout(httpParams, socketTimeoutMillis);
       BufferedInputStream bufferedInputStream = null;
       BufferedOutputStream bufferedOutputStream = null;

       StringBuffer query = new StringBuffer("?");
       query.append("scratchpad=" + servicesClientIdentifier);

       logger.info("Source name: " + authorityName
               + " Source URI: " + authorityUri
               + " tempFile: " + temporaryFileName
               + " identifier " + recordIdentifier);


        query.append("&verb=GetRecord&metadataPrefix=rdf&identifier="
                + recordIdentifier);
       HttpGet httpGet = new HttpGet(authorityUri + query.toString());
       try {
           logger.info("Issuing " + httpGet.getRequestLine().toString());
           HttpResponse httpResponse = httpClient.execute(httpGet);
           logger.info("Got response "
                   + httpResponse.getStatusLine().toString());
           HttpEntity entity = null;

           switch (httpResponse.getStatusLine().getStatusCode()) {
           case HttpURLConnection.HTTP_OK:
               logger.info("Got Status 200 OK");
               entity = httpResponse.getEntity();
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
                   throw new Exception("Server returned "
                           + httpResponse.getStatusLine()
                           + " but HttpEntity is null");
               }
               return ExitStatus.COMPLETED;
           case HttpURLConnection.HTTP_BAD_REQUEST:
               logger.info("Got Status 400 BAD REQUEST");
               entity = httpResponse.getEntity();
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
                   throw new Exception("Server returned "
                           + httpResponse.getStatusLine()
                           + " but HttpEntity is null");
               }
               StaxEventItemReader<OAIPMH> errorReader = new StaxEventItemReader<OAIPMH>();
               errorReader.setFragmentRootElementName("{http://www.openarchives.org/OAI/2.0/}OAI-PMH");
               errorReader.setUnmarshaller(unmarshaller);
               errorReader.setResource(new FileSystemResource(temporaryFileName));
               errorReader.afterPropertiesSet();
               errorReader.open(stepExecution.getExecutionContext());
               OAIPMH oaipmh = errorReader.read();
               errorReader.close();
               throw new OaiPmhException(oaipmh.getError());
           default:
               logger.info("Server returned unexpected status code "
                       + httpResponse.getStatusLine() + " for document "
                       + authorityUri); // This is not an error in this
                                        // application but a server side error
               BufferedHttpEntity bufferedEntity
                   = new BufferedHttpEntity(httpResponse.getEntity());
               InputStreamReader reader
                   = new InputStreamReader(bufferedEntity.getContent());
               StringBuffer stringBuffer = new StringBuffer();
               int count;
               char[] content = new char[BUFFER];
               while ((count = reader.read(content, 0, BUFFER)) != -1) {
                  stringBuffer.append(content);
               }
               logger.info("Server Response was: " + stringBuffer.toString());
               httpGet.abort();
               throw new Exception("Server returned unexpected status code "
                       + httpResponse.getStatusLine() + " for document "
                       + authorityUri);
           }

       } catch (ClientProtocolException cpe) {
           logger.error("Client Protocol Exception getting document "
                   + authorityUri + " " + cpe.getLocalizedMessage());
           throw new Exception("Client Protocol Exception getting document "
                   + authorityUri + " " + cpe.getLocalizedMessage(), cpe);
       } catch (IOException ioe) {
           logger.error("Input Output Exception getting document "
                   + authorityUri + " " + ioe.getLocalizedMessage());
           throw new Exception("Input Output Exception getting document "
                   + authorityUri + " " + ioe.getLocalizedMessage(), ioe);
       } catch (Exception e) {
           logger.error("Exception reading document "
                   + temporaryFileName + " " + e.getLocalizedMessage());
           if (e.getClass().equals(OaiPmhException.class)) {
               throw e;
           } else {
                String message = e.getClass() + " reading document "
                        + temporaryFileName + " " + e.getLocalizedMessage()
                        + " for identifier " + recordIdentifier;
               logger.error(message);
                throw new OaiPmhException(message);
           }
    } finally {
           if (bufferedInputStream != null) {
               try {
                   bufferedInputStream.close();
               } catch (IOException ioe) {
                   logger.error(
                           "Input Output Exception closing inputStream for "
                           + authorityUri + " " + ioe.getLocalizedMessage());
               }
           }
           if (bufferedOutputStream != null) {
               try {
                   bufferedOutputStream.close();
               } catch (IOException ioe) {
                   logger.error(
                           "Input Output Exception closing outputStream for "
                           + authorityUri + " " + ioe.getLocalizedMessage());
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
            StaxEventItemReader<ResumptionToken> staxEventItemReader
                = new StaxEventItemReader<ResumptionToken>();
            staxEventItemReader
                    .setFragmentRootElementName(
                    "{http://www.openarchives.org/OAI/2.0/}resumptionToken");
            staxEventItemReader.setUnmarshaller(unmarshaller);
            staxEventItemReader.setResource(new FileSystemResource(
                    temporaryFileName));

            staxEventItemReader.afterPropertiesSet();
            staxEventItemReader.open(stepExecution.getExecutionContext());

            ResumptionToken resumptionToken = staxEventItemReader.read();
            staxEventItemReader.close();
            if (resumptionToken == null) {
                logger.info("Resumption Token Not Found");
                stepExecution.getJobExecution().getExecutionContext()
                        .remove("resumption.token");
                return new ExitStatus("NO RESUMPTION TOKEN");
            } else {
                stepExecution.getJobExecution().getExecutionContext()
                        .remove("resumption.token");
                logger.info("Found resumption token"
                        + resumptionToken.getValue() + " "
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

    /**
     * (non-Javadoc).
     *
     * @see org.springframework.batch.core.StepExecutionListener#afterStep(org.
     * springframework.batch.core.StepExecution)
     * @param newStepExecution set the step execution
     * @return an exit status
     */
    public final ExitStatus afterStep(final StepExecution newStepExecution) {
        return null;
    }

    /**
     * (non-Javadoc).
     *
     * @see org.springframework.batch.core.StepExecutionListener#beforeStep(org.
     * springframework.batch.core.StepExecution)
     * @param newStepExecution set the step execution
     */
    public final void beforeStep(final StepExecution newStepExecution) {
        this.stepExecution = newStepExecution;
    }

}
