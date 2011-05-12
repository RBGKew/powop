package org.openarchives.pmh;


public class NoRecordsMatchException extends RuntimeException {

	private static final long serialVersionUID = 1205909186178570527L;

	public NoRecordsMatchException(String string) {
		super(string);
	}

}
