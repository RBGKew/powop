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
package org.powo.model;

import javax.persistence.FetchType;
import javax.persistence.Lob;
import javax.persistence.ManyToOne;
import javax.persistence.MappedSuperclass;
import javax.validation.constraints.Size;

import org.hibernate.annotations.NaturalId;
import org.hibernate.annotations.Type;
import org.hibernate.validator.constraints.NotEmpty;
import org.hibernate.validator.constraints.URL;
import org.joda.time.DateTime;
import org.powo.model.marshall.json.DateTimeDeserializer;
import org.powo.model.marshall.json.DateTimeSerializer;
import org.powo.model.registry.Organisation;
import org.powo.model.registry.Resource;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonInclude.Include;
import com.fasterxml.jackson.databind.annotation.JsonDeserialize;
import com.fasterxml.jackson.databind.annotation.JsonSerialize;

/**
 *
 * @author ben
 *
 */
@MappedSuperclass
@JsonInclude(Include.NON_EMPTY)
public abstract class BaseData extends Base implements Annotated {

	private static final long serialVersionUID = 1L;

	private String license;

	private DateTime created;

	private DateTime modified;

	private String rights;

	@JsonIgnore
	private String rightsHolder;

	private String accessRights;

	@JsonIgnore
	private Organisation authority;

	@JsonIgnore
	private String uri;

	@JsonIgnore
	private Resource resource;

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
	public Organisation getAuthority() {
		return authority;
	}

	/**
	 *
	 * @param authority Set the authority
	 */
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
	@ManyToOne(fetch = FetchType.LAZY)
	public Resource getResource(){
		return resource;
	}

	public void setResource(Resource resource){
		this.resource = resource;
	}
}

