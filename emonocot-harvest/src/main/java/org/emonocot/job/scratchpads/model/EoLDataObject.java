package org.emonocot.job.scratchpads.model;

import java.util.UUID;

import org.emonocot.job.scratchpads.model.convert.EoLAgentConverter;
import org.emonocot.model.taxon.Taxon;

import com.thoughtworks.xstream.annotations.XStreamConverter;

public class EoLDataObject {
	// maps to the dataType element
	private String dataType;
	
	// maps to the agent element
	@XStreamConverter(EoLAgentConverter.class)
	private EoLAgent agent;
	
    // maps to the dcterms:created element
	private String created;
	
	// maps to the dcterms:modified element
	private String modified;
	
	// maps to the dc:title element
	private String title;
	
	// maps to the license element
	private String license;
	
	// maps to the dc:source element
	private String source;
	
	// maps to the subject element
	private String subject;
	
	// maps to the dc:description element
	private String description;
	
	private Taxon taxon;
	
	public EoLAgent getAgent() {
		return agent;
	}

	public String getCreated() {
		return created;
	}

	public String getModified() {
		return modified;
	}

	public String getTitle() {
		return title;
	}

	public String getLicense() {
		return license;
	}

	public String getSource() {
		return source;
	}

	public String getSubject() {
		return subject;
	}

	public String getDescription() {
		return description;
	}

	public String getDataType() {
		return dataType;
	}
	
	public void setTaxon(Taxon taxon) {
		this.taxon = taxon;
	}

	public Taxon getTaxon() {
		return taxon;
	}

	public void setCreated(String created) {
		this.created = created;
	}

	public void setModified(String modified) {
		this.modified = modified;		
	}

	public void setLicense(String license) {
		this.license = license;		
	}

	public void setSubject(String subject) {
		this.subject = subject;
	}

	public void setSource(String source) {
		this.source = source;		
	}

	public void setDescription(String description) {
		this.description = description;		
	}

	public void setAgent(EoLAgent agent) {
		this.agent = agent;
	}
}
