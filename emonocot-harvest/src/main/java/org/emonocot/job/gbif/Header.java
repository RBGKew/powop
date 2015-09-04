/*
 * This is eMonocot, a global online biodiversity information resource.
 *
 * Copyright © 2011–2015 The Board of Trustees of the Royal Botanic Gardens, Kew and The University of Oxford
 *
 * eMonocot is free software: you can redistribute it and/or modify it under the terms of the
 * GNU Affero General Public License as published by the Free Software Foundation, either version 3
 * of the License, or (at your option) any later version.
 *
 * eMonocot is distributed in the hope that it will be useful, but WITHOUT ANY WARRANTY; without even
 * the implied warranty of MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 * GNU Affero General Public License for more details.
 *
 * The complete text of the GNU Affero General Public License is in the source repository as the file
 * ‘COPYING’.  It is also available from <http://www.gnu.org/licenses/>.
 */
package org.emonocot.job.gbif;

import java.util.ArrayList;
import java.util.List;

public class Header {

	private String help;

	private String request;

	private String statements;

	private String stylesheet;

	private String nextRequestUrl;

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

	public String getNextRequestUrl() {
		return nextRequestUrl;
	}

	public void setNextRequestUrl(String nextRequestUrl) {
		this.nextRequestUrl = nextRequestUrl;
	}
}
