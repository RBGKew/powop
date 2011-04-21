package org.emonocot.ws.scratchpads;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
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

public class GetXMLDocClient {
	Logger logger = LoggerFactory.getLogger(GetXMLDocClient.class);
	
	private HttpClient httpClient = new DefaultHttpClient();
	
	public void setHttpClient(HttpClient httpClient) {
		this.httpClient = httpClient;
	}
	
	/**
	 * Executes a HTTP GET request with the If-Modified-Since header set to dateLastHarvested. If the resource
	 * has not been modified then the authority may respond with the HTTP status 304 NOT MODIFIED, in which case the 
	 * method will return an ExitStatus with an exit code 'NOT MODIFIED' and the job will terminate.
	 * 
	 * If the resource has been modified, the client will save the response in a document specified in temporaryFileName and will 
	 * an ExitStatus with an exit code 'COMPLETE'.
	 * 
	 * @param authorityName
	 * @param authorityURI
	 * @param dateLastHarvested
	 * @return
	 */
	public ExitStatus getDocument(String authorityName, String authorityURI, String dateLastHarvested, String temporaryFileName) {
		httpClient.getParams().setParameter("http.useragent", "org.emonocot.ws.scratchpads.GetXmlDocClient");
		BufferedReader bufferedReader = null;
		BufferedWriter bufferedWriter = null;
		InputStream inputStream = null;
		
		HttpGet httpGet = new HttpGet(authorityURI);
		httpGet.addHeader(new BasicHeader("If-Modified-Since",DateUtils.formatDate(new Date(Long.parseLong(dateLastHarvested)))));
		try {
			HttpResponse httpResponse = httpClient.execute(httpGet);

			switch(httpResponse.getStatusLine().getStatusCode()) {
			  case 304: // Not Modified
				  return new ExitStatus("NOT MODIFIED");
			  case 200: // OK
				  HttpEntity entity = httpResponse.getEntity();
				  if(entity != null) {
					  inputStream = entity.getContent();
					  bufferedReader = new BufferedReader(new InputStreamReader(inputStream));
					  bufferedWriter = new BufferedWriter(new FileWriter(new File(temporaryFileName)));
					  String line = null;
					  while((line = bufferedReader.readLine()) != null) {
						  bufferedWriter.write(line);
					  }
				  } else {
					  logger.info("Server returned " + httpResponse.getStatusLine() + " but HttpEntity is null");
					  return ExitStatus.FAILED;
				  }
				  return ExitStatus.COMPLETED;
			  default:
				  logger.info("Server returned unexpected status code " + httpResponse.getStatusLine() + " for document " + authorityURI); // This is not an error in this application but a server side error
				  return ExitStatus.FAILED;
			}
			
			
		} catch (ClientProtocolException cpe) {
			logger.error("Client Protocol Exception getting document " + authorityURI + " " + cpe.getLocalizedMessage());
			return ExitStatus.FAILED;
		} catch (IOException ioe) {
			logger.error("Input Output Exception getting document " + authorityURI + " " + ioe.getLocalizedMessage());
			return ExitStatus.FAILED;
		} finally{
			if(inputStream != null) {
				try {
				  inputStream.close();
				} catch(IOException ioe) {
					logger.error("Input Output Exception closing inputStream for " + authorityURI + " " + ioe.getLocalizedMessage());
				}
			}
			if(bufferedWriter != null) {
				try {
					 bufferedWriter.close();
				} catch(IOException ioe) {
					logger.error("Input Output Exception closing bufferedWriter for " + authorityURI + " " + ioe.getLocalizedMessage());
				}
			}
		}
	}

}
