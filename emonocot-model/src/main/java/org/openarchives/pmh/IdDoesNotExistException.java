package org.openarchives.pmh;

import java.io.Serializable;

public class IdDoesNotExistException extends Exception {
	
	private static final long serialVersionUID = 7918907769810176520L;
	
	private Serializable identifier;

	public IdDoesNotExistException(Serializable identifier) {
		this.identifier = identifier;
	}
	
	@Override
	public String getMessage() {
		return "Could not find object with identifier " + identifier;
	}
	
	public Serializable getIdentifier() {
		return identifier;
	}
}
