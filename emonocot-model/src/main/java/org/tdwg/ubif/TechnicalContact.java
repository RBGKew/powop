package org.tdwg.ubif;

import com.thoughtworks.xstream.annotations.XStreamAsAttribute;

public class TechnicalContact {
	
	@XStreamAsAttribute
	private String literal;
	
	@XStreamAsAttribute
	private String email;

	public String getLiteral() {
		return literal;
	}

	public void setLiteral(String literal) {
		this.literal = literal;
	}

	public String getEmail() {
		return email;
	}

	public void setEmail(String email) {
		this.email = email;
	}
}
