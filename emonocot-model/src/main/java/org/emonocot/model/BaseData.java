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

import javax.persistence.FetchType;
import javax.persistence.Lob;
import javax.persistence.ManyToOne;
import javax.persistence.MappedSuperclass;
import javax.validation.constraints.Size;

import com.fasterxml.jackson.databind.annotation.JsonDeserialize;
import com.fasterxml.jackson.databind.annotation.JsonSerialize;

import org.emonocot.model.marshall.json.DateTimeDeserializer;
import org.emonocot.model.marshall.json.DateTimeSerializer;
import org.emonocot.model.marshall.json.OrganisationDeserialiser;
import org.emonocot.model.marshall.json.OrganisationSerializer;
import org.emonocot.model.registry.Organisation;
import org.hibernate.annotations.NaturalId;
import org.hibernate.annotations.Type;
import org.hibernate.validator.constraints.NotEmpty;
import org.hibernate.validator.constraints.URL;
import org.joda.time.DateTime;

/**
 *
 * @author ben
 *
 */
@MappedSuperclass
public abstract class BaseData extends Base implements Annotated {

	private static final long serialVersionUID = 1L;

	private String license;

	private DateTime created;

	private DateTime modified;

	private String rights;

	private String rightsHolder;

	private String accessRights;

	private Organisation authority;

	private String uri;

	/**
	 *
	 * @return The unique identifier of the object
	 */
	@NaturalId
	@NotEmpty
	public String getIdentifier() {
		return identifier;
	}

	/**
	 *
	 * @param identifier  Set the unique identifier of the object
	 */
	public void setIdentifier(String identifier) {
		this.identifier = identifier;
	}

	/**
	 *
	 * @return the primary authority
	 */
	@ManyToOne(fetch = FetchType.LAZY)
	@JsonSerialize(using = OrganisationSerializer.class)
	public Organisation getAuthority() {
		return authority;
	}

	/**
	 *
	 * @param authority Set the authority
	 */
	@JsonDeserialize(using = OrganisationDeserialiser.class)
	public void setAuthority(Organisation authority) {
		this.authority = authority;
	}

	/**
	 *
	 * @return Get the license of this object.
	 */
	public String getLicense() {
		return license;
	}

	/**
	 *
	 * @return Get the time this object was created.
	 */
	@Type(type="dateTimeUserType")
	@JsonSerialize(using = DateTimeSerializer.class)
	public DateTime getCreated() {
		return created;
	}

	/**
	 *
	 * @return Get the time this object was last modified.
	 */
	@Type(type="dateTimeUserType")
	@JsonSerialize(using = DateTimeSerializer.class)
	public DateTime getModified() {
		return modified;
	}

	/**
	 *
	 * @param newCreated
	 *            Set the created time for this object.
	 */
	@JsonDeserialize(using = DateTimeDeserializer.class)
	public void setCreated(DateTime newCreated) {
		this.created = newCreated;
	}

	/**
	 *
	 * @param newModified
	 *            Set the modified time for this object.
	 */
	@JsonDeserialize(using = DateTimeDeserializer.class)
	public void setModified(DateTime newModified) {
		this.modified = newModified;
	}

	/**
	 *
	 * @param newLicense
	 *            Set the license for this object.
	 */
	public void setLicense(String newLicense) {
		this.license = newLicense;
	}

	/**
	 * @return the rights
	 */
	@Lob
	public String getRights() {
		return rights;
	}

	/**
	 * @param rights the rights to set
	 */
	public void setRights(String rights) {
		this.rights = rights;
	}

	/**
	 * @return the rightsHolder
	 */
	public String getRightsHolder() {
		return rightsHolder;
	}

	/**
	 * @param rightsHolder the rightsHolder to set
	 */
	public void setRightsHolder(String rightsHolder) {
		this.rightsHolder = rightsHolder;
	}

	/**
	 * @return the accessRights
	 */
	public String getAccessRights() {
		return accessRights;
	}

	/**
	 * @param accessRights the accessRights to set
	 */
	public void setAccessRights(String accessRights) {
		this.accessRights = accessRights;
	}

	@URL
	@Size(max = 255)
	public String getUri() {
		return uri;
	}

	public void setUri(String uri) {
		this.uri = uri;
	}
}
