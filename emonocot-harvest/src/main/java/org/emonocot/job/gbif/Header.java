package org.emonocot.job.gbif;

import java.util.List;
import java.util.ArrayList;

public class Header {
	
	private String help;
	
	private String request;
	
	private String statements;
	
	private String stylesheet;
	
	private List<Parameter> parameters = new ArrayList<Parameter>();
	
	private Summary summary;
	
	public void setHelp(String help) {
		this.help = help;
	}
	
	public String getHelp() {
		return help;
	}
	
	public void setRequest(String request) {
		this.request = request;
	}
	
	public String getRequest() {
		return request;
	}
	
	public void setStatements(String statements) {
		this.statements = statements;
	}
	
	public String getStatements() {
		return statements;
	}
	
	public void setStylesheet(String stylesheet) {
		this.stylesheet = stylesheet;
	}
	
	public String getStylesheet() {
		return stylesheet;
	}
	
	public void setParameters(List<Parameter> parameters) {
		this.parameters = parameters;
	}
	
	public List<Parameter> getParameters() {
		return parameters;
	}
	
	public void setSummary(Summary summary) {
		this.summary = summary;
	}
	
	public Summary getSummary() {
		return summary;
	}
}
