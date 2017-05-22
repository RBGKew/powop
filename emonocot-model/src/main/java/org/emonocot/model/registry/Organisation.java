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
package org.emonocot.model.registry;

import java.util.List;
import java.util.Set;

import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.Lob;
import javax.persistence.OneToMany;
import javax.persistence.Transient;

import org.emonocot.model.Annotation;
import org.emonocot.model.Base;
import org.hibernate.annotations.Cascade;
import org.hibernate.annotations.CascadeType;
import org.hibernate.annotations.Fetch;
import org.hibernate.annotations.FetchMode;
import org.hibernate.annotations.Where;
import org.hibernate.validator.constraints.Length;
import org.hibernate.validator.constraints.NotEmpty;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonInclude.Include;
import com.google.common.collect.Ordering;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.ToString;

/**
 * Class that represents the authority an object is harvested from.
 *
 * @author ben
 *
 */
@Entity
@Getter
@Setter
@ToString(exclude = {"resources", "annotations"})
@JsonInclude(Include.NON_NULL)
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class Organisation extends Base implements Comparable<Organisation> {

	private static final long serialVersionUID = -2463044801110563816L;

	@Id
	@GeneratedValue(generator = "table-hilo", strategy = GenerationType.TABLE)
	private Long id;

	@Lob
	@Length(max = 1431655761)
	private String description;

	@NotEmpty
	private String abbreviation;

	private String publisherName;

	private String publisherEmail;

	private String subject;

	@NotEmpty
	private String title;

	private String bibliographicCitation;

	private String creator;

	protected String identifier;

	@OneToMany(mappedBy = "organisation", fetch = FetchType.EAGER)
	@Cascade(CascadeType.DELETE)
	@Fetch(FetchMode.SELECT)
	private List<Resource> resources;

	@OneToMany(fetch = FetchType.LAZY, orphanRemoval = true)
	@JoinColumn(name = "annotatedObjId")
	@Where(clause = "annotatedObjType = 'Organisation'")
	@Cascade({ CascadeType.SAVE_UPDATE, CascadeType.MERGE, CascadeType.DELETE })
	@JsonIgnore
	private Set<Annotation> annotations;

	@Transient
	@JsonIgnore
	public String getClassName() {
		return "Organisation";
	}

	@Override
	public int compareTo(Organisation o) {
		return Ordering.natural().nullsLast().compare(getTitle(), o.getTitle());
	}
}
