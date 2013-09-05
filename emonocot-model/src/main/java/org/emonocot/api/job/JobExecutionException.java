package org.emonocot.api.job;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;

@JsonIgnoreProperties(ignoreUnknown = true)
public class JobExecutionException extends Throwable {
	
	/**
	 * 
	 */
	private static final long serialVersionUID = -6169550730623190349L;

	/**
	 *
	 */
	private String message;

	public JobExecutionException(String message) {
		this.message = message;
	}

	/**
	 * @return the message
	 */
	public String getMessage() {
		return message;
	}

	/**
	 * @param message the message to set
	 */
	public void setMessage(String message) {
		this.message = message;
	}
	
}
