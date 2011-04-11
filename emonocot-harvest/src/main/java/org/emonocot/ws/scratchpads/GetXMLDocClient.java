package org.emonocot.ws.scratchpads;

import java.util.Date;

import org.springframework.batch.core.ExitStatus;

public class GetXMLDocClient {
	
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
	public ExitStatus getDocument(String authorityName, String authorityURI, Date dateLastHarvested, String temporaryFileName) {
		return null;
	}

}
