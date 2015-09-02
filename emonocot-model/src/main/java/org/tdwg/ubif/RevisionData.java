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
package org.tdwg.ubif;

import java.util.ArrayList;
import java.util.List;

import org.joda.time.DateTime;
import org.tdwg.ubif.marshall.xml.DateTimeConverter;

import com.thoughtworks.xstream.annotations.XStreamAlias;
import com.thoughtworks.xstream.annotations.XStreamConverter;

/**
 *
 * @author ben
 *
 */
public class RevisionData {

	@XStreamAlias("Creators")
	private List<Agent> creators = new ArrayList<Agent>();

	@XStreamAlias("DateCreated")
	@XStreamConverter(DateTimeConverter.class)
	private DateTime dateCreated;

	@XStreamAlias("DateModified")
	@XStreamConverter(DateTimeConverter.class)
	private DateTime dateModified;

	/**
	 * @return the creators
	 */
	public final List<Agent> getCreators() {
		return creators;
	}

	/**
	 * @param newCreators the creators to set
	 */
	public final void setCreators(final List<Agent> newCreators) {
		this.creators = newCreators;
	}

	/**
	 * @return the dateCreated
	 */
	public final DateTime getDateCreated() {
		return dateCreated;
	}

	/**
	 * @param newDateCreated the dateCreated to set
	 */
	public final void setDateCreated(final DateTime newDateCreated) {
		this.dateCreated = newDateCreated;
	}

	public DateTime getDateModified() {
		return dateModified;
	}

	public void setDateModified(DateTime dateModified) {
		this.dateModified = dateModified;
	}
}
