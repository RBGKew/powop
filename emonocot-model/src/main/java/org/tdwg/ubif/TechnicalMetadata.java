package org.tdwg.ubif;

import com.thoughtworks.xstream.annotations.XStreamAlias;
import com.thoughtworks.xstream.annotations.XStreamAsAttribute;

public class TechnicalMetadata {
	
	@XStreamAsAttribute
	private String created;
	
	@XStreamAlias("Generator")
	private Generator generator;
	
	@XStreamAlias("TechnicalContact")
	private TechnicalContact technicalContact;

	public String getCreated() {
		return created;
	}

	public void setCreated(String created) {
		this.created = created;
	}

	public Generator getGenerator() {
		return generator;
	}

	public void setGenerator(Generator generator) {
		this.generator = generator;
	}

	public TechnicalContact getTechnicalContact() {
		return technicalContact;
	}

	public void setTechnicalContact(TechnicalContact technicalContact) {
		this.technicalContact = technicalContact;
	}
}
