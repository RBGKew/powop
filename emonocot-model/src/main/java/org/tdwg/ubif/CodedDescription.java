package org.tdwg.ubif;

import org.tdwg.ubif.marshall.xml.Ignore;

import com.thoughtworks.xstream.annotations.XStreamAlias;
import com.thoughtworks.xstream.annotations.XStreamAsAttribute;

public class CodedDescription {
	
	@XStreamAsAttribute
	private String id;
	
	@XStreamAlias("Representation")
	private Representation representation;
	
	@XStreamAlias("SummaryData")
	private Ignore summaryData;

	public Representation getRepresentation() {
		return representation;
	}

	public void setRepresentation(Representation representation) {
		this.representation = representation;
	}

	public String getId() {
		return id;
	}

	public void setId(String id) {
		this.id = id;
	}
}
