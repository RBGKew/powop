/*
 * This is eMonocot, a global online biodiversity information resource.
 *
 * Copyright © 2011–2015 The Board of Trustees of the Royal Botanic Gardens, Kew and The University of Oxford
 *
 * eMonocot is free software: you can redistribute it and/or modify it under the terms of the
 * GNU Affero General Public License as published by the Free Software Foundation, either version 3
 * of the License, or (at your option) any later version.
 *
 * eMonocot is distributed in the hope that it will be useful, but WITHOUT ANY WARRANTY; without even
 * the implied warranty of MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 * GNU Affero General Public License for more details.
 *
 * The complete text of the GNU Affero General Public License is in the source repository as the file
 * ‘COPYING’.  It is also available from <http://www.gnu.org/licenses/>.
 */
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
import java.util.HashMap;
import java.util.Map;

import org.apache.http.Header;
import org.apache.http.HttpEntity;
import org.apache.http.HttpHost;
import org.apache.http.HttpResponse;
import org.apache.http.client.ClientProtocolException;
import org.apache.http.client.HttpClient;
import org.apache.http.client.HttpResponseException;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.conn.params.ConnRoutePNames;
import org.apache.http.entity.BufferedHttpEntity;
import org.apache.http.impl.client.DefaultHttpClient;
import org.apache.http.impl.cookie.DateUtils;
import org.apache.http.message.BasicHeader;
import org.apache.http.util.EntityUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.batch.core.ExitStatus;
import org.springframework.batch.retry.RetryCallback;
import org.springframework.batch.retry.RetryContext;
import org.springframework.batch.retry.RetryListener;
import org.springframework.batch.retry.backoff.FixedBackOffPolicy;
import org.springframework.batch.retry.policy.SimpleRetryPolicy;
import org.springframework.batch.retry.support.RetryTemplate;

/**
 *
 * @author ben
 *
 */
public class GetResourceClient {

    static final int BUFFER = 2048;

    private Logger logger = LoggerFactory.getLogger(GetResourceClient.class);

    private HttpClient httpClient = new DefaultHttpClient();

    private String proxyHost;

    private Integer proxyPort;
    
    private Long backoffPeriod = 3000L;

	private int retryAttempts = 3;
	
	private RetryListener[] retryListeners = new RetryListener[] {};

    public void setRetryListeners(RetryListener[] retryListeners) {
		this.retryListeners = retryListeners;
	}

	public void setBackoffPeriod(Long backoffPeriod) {
		this.backoffPeriod = backoffPeriod;
	}

	public void setRetryAttempts(int retryAttempts) {
		this.retryAttempts = retryAttempts;
	}

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
     * case the method will return an ExitStatus with an exit code 'NOT_MODIFIED'
     * and the job will terminate.
     *
     * If the resource has been modified, the client will save the response in a
     * document specified in temporaryFileName and will an ExitStatus with an
     * exit code 'COMPLETE'.
     *
     * @param authorityName
     *            The name of the Source being harvested.
     * @param resource
     *            The endpoint (uri) being harvested.
     * @param ifModifiedSince
     *            The dateTime when this Source was last harvested.
     * @param temporaryFileName
     *            The name of the temporary file to store the response in
     * @return An exit status indicating that the step was completed, failed, or
     *         if the Source responded with a 304 NOT MODIFIED response
     *         indicating that no records have been modified
     */
    public final ExitStatus getResource(final String resource, final String ifModifiedSince,
            final String temporaryFileName) {
        if (proxyHost != null && proxyPort != null) {
            HttpHost proxy = new HttpHost(proxyHost, proxyPort);
            httpClient.getParams()
                .setParameter(ConnRoutePNames.DEFAULT_PROXY, proxy);
        }

        httpClient.getParams().setParameter("http.useragent", "org.emonocot.ws.GetResourceClient");
        RetryTemplate retryTemplate = new RetryTemplate();
        
        FixedBackOffPolicy backOffPolicy = new FixedBackOffPolicy();
        backOffPolicy.setBackOffPeriod(backoffPeriod);
        SimpleRetryPolicy retryPolicy = new SimpleRetryPolicy();
        retryPolicy.setMaxAttempts(retryAttempts);
        Map<Class<? extends Throwable>,Boolean> retryableExceptions = new HashMap<Class<? extends Throwable>,Boolean>();
        retryableExceptions.put(ClientProtocolException.class, Boolean.TRUE);
        retryableExceptions.put(IOException.class, Boolean.TRUE);
        retryPolicy.setRetryableExceptions(retryableExceptions);
        
        retryTemplate.setListeners(retryListeners);
        retryTemplate.setBackOffPolicy(backOffPolicy);
        retryTemplate.setRetryPolicy(retryPolicy);
        
        try {
			return retryTemplate.execute(new RetryCallback<ExitStatus> () {

				@Override
				public ExitStatus doWithRetry(RetryContext context)
						throws Exception {
					InputStreamReader inputStreamReader = null;
			        OutputStreamWriter outputStreamWriter = null;

			        HttpGet httpGet = new HttpGet(resource);
			        httpGet.setHeader("If-Modified-Since", DateUtils
			                .formatDate(new Date(Long.parseLong(ifModifiedSince))));
			        try {
			            HttpResponse httpResponse = httpClient.execute(httpGet);

			            switch(httpResponse.getStatusLine().getStatusCode()) {
			              case HttpURLConnection.HTTP_NOT_MODIFIED:
			                  return new ExitStatus("NOT_MODIFIED");
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
			                    logger.warn("Server returned "
			                            + httpResponse.getStatusLine()
			                            + " but HttpEntity is null");
			                    throw new IOException("Server returned "
			                            + httpResponse.getStatusLine()
			                            + " but HttpEntity is null");
			                  }
			                  return ExitStatus.COMPLETED;
			              default:
			                logger.warn("Server returned unexpected status code "
			                        + httpResponse.getStatusLine() + " for document "
			                        + resource); // This is not an error in this
			                                         // application but a server side error
			                EntityUtils.consumeQuietly(httpResponse.getEntity());
			                throw new IOException("Server returned unexpected status code "
			                        + httpResponse.getStatusLine() + " for document "
			                        + resource);
			            }

			        } catch (ClientProtocolException cpe) {
			        	if(cpe instanceof HttpResponseException) {
			        		HttpResponseException hre = (HttpResponseException) cpe;
			        		logger.error("HttpResponse Exception getting document "
			                        + resource + " " + hre.getMessage()
			                        + " with status code " + hre.getStatusCode());
			        	} else {
			                logger.error("Client Protocol Exception getting document "
			                    + resource + " " + cpe.getMessage());
			        	}
			            throw cpe;
			        } catch (IOException ioe) {
			            logger.error("Input Output Exception getting document "
			                    + resource + " " + ioe.getLocalizedMessage(), ioe);
			            throw ioe;
			        } finally {
			            httpGet.releaseConnection();
			            if (inputStreamReader != null) {
			                try {
			                    inputStreamReader.close();
			                } catch (IOException ioe) {
			                    logger.error(
			                            "Input Output Exception closing inputStream for "
			                            + resource + " " + ioe.getLocalizedMessage(), ioe);
			                }
			            }
			            if (outputStreamWriter != null) {
			                try {
			                     outputStreamWriter.close();
			                } catch (IOException ioe) {
			                    logger.error(
			                            "Input Output Exception closing outputStream for "
			                            + resource + " " + ioe.getLocalizedMessage(), ioe);
			                }
			            }
			        }
				}
				
			});
		} catch (Exception e) {
			logger.error("Retry processing failed " + e.getMessage(), e);
			return ExitStatus.FAILED;
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
     * @param resource
     *            The endpoint (uri) being harvested.
     * @param ifModifiedSince
     *            The dateTime when this Source was last harvested.
     * @param temporaryFileName
     *            The name of the temporary file to store the response in
     * @return An exit status indicating that the step was completed, failed, or
     *         if the Source responded with a 304 NOT MODIFIED response
     *         indicating that no records have been modified
     */
    public final ExitStatus getBinaryResource(final String resource, final String ifModifiedSince,
            final String temporaryFileName) {
        if (proxyHost != null && proxyPort != null) {
            HttpHost proxy = new HttpHost(proxyHost, proxyPort);
            httpClient.getParams()
                .setParameter(ConnRoutePNames.DEFAULT_PROXY, proxy);
        }

        httpClient.getParams().setParameter("http.useragent", "org.emonocot.ws.GetResourceClient");
        RetryTemplate retryTemplate = new RetryTemplate();
        retryTemplate.setListeners(retryListeners);
        FixedBackOffPolicy backOffPolicy = new FixedBackOffPolicy();
        backOffPolicy.setBackOffPeriod(backoffPeriod);
        SimpleRetryPolicy retryPolicy = new SimpleRetryPolicy();
        retryPolicy.setMaxAttempts(retryAttempts);
        Map<Class<? extends Throwable>,Boolean> retryableExceptions = new HashMap<Class<? extends Throwable>,Boolean>();
        retryableExceptions.put(ClientProtocolException.class, Boolean.TRUE);
        retryableExceptions.put(IOException.class, Boolean.TRUE);
        retryPolicy.setRetryableExceptions(retryableExceptions);
        
        retryTemplate.setBackOffPolicy(backOffPolicy);
        retryTemplate.setRetryPolicy(retryPolicy);
        try {
			return retryTemplate.execute(new RetryCallback<ExitStatus> () {

				@Override
				public ExitStatus doWithRetry(RetryContext context)
						throws Exception {
					BufferedInputStream bufferedInputStream = null;
			        BufferedOutputStream bufferedOutputStream = null;

			        HttpGet httpGet = new HttpGet(resource.replace(" ", "%20"));
			        httpGet.addHeader(new BasicHeader("If-Modified-Since", DateUtils
			                .formatDate(new Date(Long.parseLong(ifModifiedSince)))));
			        try {
			            HttpResponse httpResponse = httpClient.execute(httpGet);

			            switch(httpResponse.getStatusLine().getStatusCode()) {
			              case HttpURLConnection.HTTP_NOT_MODIFIED:
			                  return new ExitStatus("NOT_MODIFIED");
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
			                    logger.warn("Server returned "
			                            + httpResponse.getStatusLine()
			                            + " but HttpEntity is null");
			                      throw new IOException("Server returned "
				                            + httpResponse.getStatusLine()
				                            + " but HttpEntity is null");
			                  }
			                  return ExitStatus.COMPLETED;
			            default:
			                logger.warn("Server returned unexpected status code "
			                        + httpResponse.getStatusLine() + " for document "
			                        + resource); // This is not an error in this
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
			                logger.warn("Server Response was: " + stringBuffer.toString());
			                httpGet.abort();
			                throw new IOException("Server returned unexpected status code "
			                        + httpResponse.getStatusLine() + " for document "
			                        + resource);
			            }

			        } catch (ClientProtocolException cpe) {
			        	if(cpe instanceof HttpResponseException) {
			        		HttpResponseException hre = (HttpResponseException) cpe;
			        		logger.error("HttpResponse Exception getting document "
			                        + resource + " " + hre.getMessage()
			                        + " with status code " + hre.getStatusCode());
			        	} else {
			                logger.error("Client Protocol Exception getting document "
			                    + resource + " " + cpe.getMessage());
			        	}
			            throw cpe;
			        } catch (IOException ioe) {
			            logger.error("Input Output Exception getting document "
			                    + resource + " " + ioe.getLocalizedMessage());
			            throw ioe;
			        } finally {
                        httpGet.releaseConnection();
			            if (bufferedInputStream != null) {
			                try {
			                    bufferedInputStream.close();
			                } catch (IOException ioe) {
			                    logger.error(
			                            "Input Output Exception closing inputStream for "
			                            + resource + " " + ioe.getLocalizedMessage());
			                }
			            }
			            if (bufferedOutputStream != null) {
			                try {
			                    bufferedOutputStream.close();
			                } catch (IOException ioe) {
			                    logger.error(
			                            "Input Output Exception closing outputStream for "
			                            + resource + " " + ioe.getLocalizedMessage());
			                }
			            }
			        }
				}
				
			});
		} catch (Exception e) {
			logger.error("Retry processing failed " + e.getMessage());
			return ExitStatus.FAILED;
			
		}
    }
    
    public ExitStatus postBody(final String authorityURI, final Map<String,String> params, final StringBuffer response, final Map<String,String> responseHeaders) {
    	if (proxyHost != null && proxyPort != null) {
            HttpHost proxy = new HttpHost(proxyHost, proxyPort);
            httpClient.getParams()
                .setParameter(ConnRoutePNames.DEFAULT_PROXY, proxy);
        }

        httpClient.getParams().setParameter("http.useragent", "org.emonocot.ws.GetResourceClient");
        RetryTemplate retryTemplate = new RetryTemplate();
        retryTemplate.setListeners(retryListeners);
        FixedBackOffPolicy backOffPolicy = new FixedBackOffPolicy();
        backOffPolicy.setBackOffPeriod(backoffPeriod);
        SimpleRetryPolicy retryPolicy = new SimpleRetryPolicy();
        retryPolicy.setMaxAttempts(retryAttempts);
        Map<Class<? extends Throwable>,Boolean> retryableExceptions = new HashMap<Class<? extends Throwable>,Boolean>(); 
        retryableExceptions.put(ClientProtocolException.class, Boolean.TRUE);
        retryableExceptions.put(IOException.class, Boolean.TRUE);
        retryPolicy.setRetryableExceptions(retryableExceptions);
        
        retryTemplate.setBackOffPolicy(backOffPolicy);
        retryTemplate.setRetryPolicy(retryPolicy);
        try {
			return retryTemplate.execute(new RetryCallback<ExitStatus> () {

				@Override
				public ExitStatus doWithRetry(RetryContext context)
						throws Exception {
					InputStreamReader reader = null;

			        HttpPost httpPost = new HttpPost(authorityURI.replace(" ", "%20"));

			        try {
			            HttpResponse httpResponse = httpClient.execute(httpPost);

			            switch(httpResponse.getStatusLine().getStatusCode()) {
			              case HttpURLConnection.HTTP_CREATED:
			              case HttpURLConnection.HTTP_OK:
			            	  for(Header header : httpResponse.getAllHeaders()) {
			            		  responseHeaders.put(header.getName(), header.getValue());
			            	  }
			                  HttpEntity entity = httpResponse.getEntity();
			                  if (entity != null) {
			                	  BufferedHttpEntity bufferedEntity = new BufferedHttpEntity(
					                        httpResponse.getEntity());
			                	  reader = new InputStreamReader(
					                        bufferedEntity.getContent());
			                     
			                    int count;
			                    char[] content = new char[BUFFER];
			                    while ((count = reader.read(content, 0, BUFFER)) != -1) {
				                    response.append(content);
				                }			                    
			                  } else {
			                    logger.warn("Server returned "
			                            + httpResponse.getStatusLine()
			                            + " but HttpEntity is null");
			                      throw new IOException("Server returned "
				                            + httpResponse.getStatusLine()
				                            + " but HttpEntity is null");
			                  }
			                  return ExitStatus.COMPLETED;
			            default:
			                logger.warn("Server returned unexpected status code "
			                        + httpResponse.getStatusLine() + " for document "
			                        + authorityURI); // This is not an error in this
			                                         // application but a server side error
			                BufferedHttpEntity bufferedEntity = new BufferedHttpEntity(
			                        httpResponse.getEntity());
			                reader = new InputStreamReader(
			                        bufferedEntity.getContent());
			                StringBuffer stringBuffer = new StringBuffer();
			                int count;
			                char[] content = new char[BUFFER];
			                while ((count = reader.read(content, 0, BUFFER)) != -1) {
			                    stringBuffer.append(content);
			                }
			                logger.warn("Server Response was: " + stringBuffer.toString());
			                httpPost.abort();
			                throw new IOException("Server returned unexpected status code "
			                        + httpResponse.getStatusLine() + " for document "
			                        + authorityURI);
			            }

			        } catch (ClientProtocolException cpe) {
			        	if(cpe instanceof HttpResponseException) {
			        		HttpResponseException hre = (HttpResponseException) cpe;
			        		logger.error("HttpResponse Exception getting document "
			                        + authorityURI + " " + hre.getMessage()
			                        + " with status code " + hre.getStatusCode());
			        	} else {
			                logger.error("Client Protocol Exception getting document "
			                    + authorityURI + " " + cpe.getMessage(), cpe);
			        	}
			            throw cpe;
			        } catch (IOException ioe) {
			            logger.error("Input Output Exception getting document "
			                    + authorityURI + " " + ioe.getLocalizedMessage(), ioe);
			            throw ioe;
			        } finally {
                        httpPost.releaseConnection();
			            if (reader != null) {
			                try {
			                    reader.close();
			                } catch (IOException ioe) {
			                    logger.error(
			                            "Input Output Exception closing inputStream for "
			                            + authorityURI + " " + ioe.getLocalizedMessage(), ioe);
			                }
			            }
			        }
				}
			});
		} catch (Exception e) {
			logger.error("Retry processing failed " + e.getMessage(), e);
			return ExitStatus.FAILED;
			
		} 
    }
}
