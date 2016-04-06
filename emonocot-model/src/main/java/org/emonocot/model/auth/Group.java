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
package org.emonocot.model.auth;

import java.util.Collection;
import java.util.HashSet;
import java.util.Set;

import javax.persistence.ElementCollection;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.ManyToMany;
import javax.persistence.Transient;

import org.apache.solr.common.SolrInputDocument;
import org.emonocot.model.marshall.json.UserDeserializer;
import org.emonocot.model.marshall.json.UserSerializer;
import org.hibernate.annotations.Cascade;
import org.hibernate.annotations.CascadeType;
import org.springframework.security.core.GrantedAuthority;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.databind.annotation.JsonDeserialize;
import com.fasterxml.jackson.databind.annotation.JsonSerialize;

/**
 *
 * @author ben
 *
 */
@Entity
public class Group extends Principal {

	/**
	 *
	 */
	private static final long serialVersionUID = 2290081016272464862L;

	/**
	 *
	 */
	private Set<Permission> permissions = new HashSet<Permission>();

	/**
	 *
	 */
	private Set<User> members = new HashSet<User>();

	/**
	 * @return the members
	 */
	@ManyToMany(fetch = FetchType.LAZY, mappedBy = "groups")
	@Cascade({CascadeType.SAVE_UPDATE })
	@JsonSerialize(contentUsing = UserSerializer.class)
	public Set<User> getMembers() {
		return members;
	}

	/**
	 * @param members the members to set
	 */
	@JsonDeserialize(contentUsing = UserDeserializer.class)
	public void setMembers(Set<User> members) {
		this.members = members;
	}

	/**
	 *
	 * @return a collection of granted authorities
	 */
	@Transient
	@JsonIgnore
	public Collection<GrantedAuthority> getGrantedAuthorities() {
		return (Collection) permissions;
	}

	/**
	 *
	 * @param user the user to add to the group
	 * @return true if the user was added to the group
	 */
	public final boolean addMember(final User user) {
		user.getGroups().add(this);
		return this.members.add(user);
	}

	/**
	 *
	 * @param groupName Set the group name
	 */
	@JsonIgnore
	public void setName(final String groupName) {
		setIdentifier(groupName);
	}

	/**
	 *
	 * @return the name of this group
	 */
	@JsonIgnore
	@Transient
	public String getName() {
		return getIdentifier();
	}

	/**
	 *
	 * @param user the user you wish to remove
	 * @return true if the user has been removed, false otherwise
	 */
	public final boolean removeMember(final User user) {
		user.getGroups().remove(this);
		return this.members.remove(user);
	}

	/**
	 *
	 * @return the permissions associated with the group
	 */
	@ElementCollection
	public Set<Permission> getPermissions() {
		return permissions;
	}

	/**
	 *
	 * @param permissions set the permissions associated with the group
	 */
	public void setPermissions(Set<Permission> permissions) {
		this.permissions = permissions;
	}

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

	@Override
	public SolrInputDocument toSolrInputDocument() {
		SolrInputDocument sid = new SolrInputDocument();
		sid.addField("id", getClassName() + "_" + getId());
		sid.addField("base.id_l", getId());
		sid.addField("base.class_searchable_b", false);
		sid.addField("base.class_s", getClass().getName());
		//sid.addField("group.name_t", getName());
		sid.addField("searchable.label_sort", getName());
		StringBuilder summary = new StringBuilder().append(getName());
		sid.addField("searchable.solrsummary_t", summary.toString());
		return sid;
	}
}
