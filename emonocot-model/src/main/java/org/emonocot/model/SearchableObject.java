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
package org.emonocot.model;

import java.io.Serializable;

import javax.persistence.MappedSuperclass;
import javax.persistence.Transient;

import org.apache.solr.common.SolrInputDocument;
import org.joda.time.format.DateTimeFormat;
import org.joda.time.format.DateTimeFormatter;

import com.fasterxml.jackson.annotation.JsonIgnore;

/**
 *
 * @author ben
 *
 */
@MappedSuperclass
public abstract class SearchableObject extends BaseData implements Searchable {

	/**
	 *
	 */
	private static final long serialVersionUID = 4960825789689641206L;

	private static DateTimeFormatter dateTimeFormatter = DateTimeFormat.forPattern("yyyy-MM-dd'T'HH:mm:ss'Z'");

	/**
	 *
	 * @return the simple name of the implementing class
	 */
	@Transient
	@JsonIgnore
	public String getClassName() {
		return getClass().getSimpleName();
	}

	@Override
	@Transient
	@JsonIgnore
	public String getDocumentId() {
		return getClassName() + "_" + getId();
	}

	public SolrInputDocument toSolrInputDocument() {
		SolrInputDocument sid = new SolrInputDocument();
		sid.addField("id", getDocumentId());
		sid.addField("base.id_l", getId());
		sid.addField("base.class_searchable_b", true);
		sid.addField("base.class_s", getClass().getName());
		addField(sid,"base.access_rights_s", getAccessRights());

		if(getCreated() != null) {
			sid.addField("base.created_dt", dateTimeFormatter.print(getCreated()));
		}
		if(getModified() != null) {
			sid.addField("base.modified_dt", dateTimeFormatter.print(getModified()));
		}
		addField(sid,"base.license_s", getLicense());
		addField(sid,"base.rights_s", getRights());
		addField(sid,"base.rights_holder_s", getRightsHolder());
		if(getAuthority() != null) {
			sid.addField("base.authority_s", getAuthority().getIdentifier());
			sid.addField("searchable.sources_ss", getAuthority().getIdentifier());
		}
		return sid;
	}

	protected void addField(SolrInputDocument sid, String name, Serializable value) {
		if(value != null && !value.toString().isEmpty()) {
			sid.addField(name, value);
		}
	}

}
