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

import java.util.HashSet;
import java.util.Locale;
import java.util.Set;
import java.util.SortedSet;
import java.util.TreeSet;
import javax.persistence.Column;
import javax.persistence.ElementCollection;
import javax.persistence.Entity;
import javax.persistence.EnumType;
import javax.persistence.Enumerated;
import javax.persistence.FetchType;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.JoinTable;
import javax.persistence.Lob;
import javax.persistence.ManyToMany;
import javax.persistence.ManyToOne;
import javax.persistence.OneToMany;
import javax.persistence.Transient;
import javax.validation.constraints.Size;

import org.hibernate.annotations.Cascade;
import org.hibernate.annotations.CascadeType;
import org.hibernate.annotations.SortNatural;
import org.hibernate.annotations.Where;
import org.powo.model.constants.DescriptionType;
import com.fasterxml.jackson.annotation.JsonIgnore;

/**
 * @see http://rs.gbif.org/extension/gbif/1.0/description.xml
 */
@Entity
public class Description extends OwnedEntity {

	private static final long serialVersionUID = -177666938449346483L;

	private String description;

	@JsonIgnore
	private Taxon taxon;

	@JsonIgnore
	private SortedSet<DescriptionType> types;

	private String creator;

	private String contributor;

	private String audience;

	private Locale language;

	private String source;

	@JsonIgnore
	private Long id;

	@JsonIgnore
	private Set<Annotation> annotations = new HashSet<Annotation>();

	@JsonIgnore
	private Set<Reference> references = new HashSet<Reference>();

	/**
	 *
	 * @param newId
	 *            Set the identifier of this object.
	 */
	public void setId(Long newId) {
		this.id = newId;
	}

	/**
	 *
	 * @return Get the identifier for this object.
	 */
	@Id
	@GeneratedValue(strategy = GenerationType.SEQUENCE)
	public Long getId() {
		return id;
	}

	/**
	 *
	 * @param newTaxon
	 *            Set the taxon that this content is about.
	 */
	public void setTaxon(Taxon newTaxon) {
		this.taxon = newTaxon;
	}

	/**
	 *
	 * @return Return the subjects that this content is about.
	 */
	@ElementCollection
	@Column(name = "type")
	@Enumerated(EnumType.STRING)
	@SortNatural
	public SortedSet<DescriptionType> getTypes() {
		return types;
	}

	/**
	 * Convenience method for accessing the first description type.
	 * In many cases there will only be one
	 * 
	 * @return The first description type associated with this description
	 */
	@Transient
	@JsonIgnore
	public DescriptionType getType() {
		if(types != null && !types.isEmpty()) {
			return types.first();
		} else {
			return null;
		}
	}

	/**
	 *
	 * @param types
	 *            Set the subject that this content is about.
	 */
	public void setTypes(SortedSet<DescriptionType> types) {
		this.types = types;
	}

	/**
	 *
	 * @param type
	 *            Sets the primary type for this description
	 */
	public void setType(DescriptionType type) {
		if(types == null) {
			types = new TreeSet<>();
		}

		if(!types.isEmpty()) {
			types.remove(types.first());
		}
		types.add(type);
	}

	/**
	 *
	 * @return Get the taxon that this content is about.
	 */
	@ManyToOne(fetch = FetchType.LAZY)
	public Taxon getTaxon() {
		return taxon;
	}

	/**
	 * @return the creator
	 */
	@Size(max = 255)
	public String getCreator() {
		return creator;
	}

	/**
	 * @param creator the creator to set
	 */
	public void setCreator(String creator) {
		this.creator = creator;
	}

	/**
	 * @return the source
	 */
	@Size(max = 255)
	public String getSource() {
		return source;
	}

	/**
	 * @param source the source to set
	 */
	public void setSource(String source) {
		this.source = source;
	}

	/**
	 *
	 * @param newContent
	 *            Set the content of this object.
	 */
	public void setDescription(String newContent) {
		this.description = newContent;
	}

	/**
	 *
	 * @return the content as a string
	 */
	@Lob
	public String getDescription() {
		return description;
	}

	@Transient
	@JsonIgnore
	public final String getClassName() {
		return "Description";
	}

	/**
	 * @return the annotations
	 */
	@OneToMany(fetch = FetchType.LAZY, orphanRemoval = true)
	@JoinColumn(name = "annotatedObjId")
	@Where(clause = "annotatedObjType = 'Description'")
	@Cascade({ CascadeType.SAVE_UPDATE, CascadeType.MERGE, CascadeType.DELETE })
	public Set<Annotation> getAnnotations() {
		return annotations;
	}

	/**
	 * @param annotations
	 *            the annotations to set
	 */
	public void setAnnotations(Set<Annotation> annotations) {
		this.annotations = annotations;
	}

	/**
	 * @return the references
	 */
	@ManyToMany(fetch = FetchType.LAZY)
	@Cascade({ CascadeType.SAVE_UPDATE })
	@JoinTable(name = "Description_Reference", joinColumns = { @JoinColumn(name = "Description_id") }, inverseJoinColumns = { @JoinColumn(name = "references_id") })
	public Set<Reference> getReferences() {
		return references;
	}

	/**
	 * @param newReferences the references to set
	 */
	public void setReferences(Set<Reference> newReferences) {
		this.references = newReferences;
	}

	/**
	 * @return the contributor
	 */
	@Size(max = 255)
	public String getContributor() {
		return contributor;
	}

	/**
	 * @param contributor the contributor to set
	 */
	public void setContributor(String contributor) {
		this.contributor = contributor;
	}

	/**
	 * @return the audience
	 */
	@Size(max = 255)
	public String getAudience() {
		return audience;
	}

	/**
	 * @param audience the audience to set
	 */
	public void setAudience(String audience) {
		this.audience = audience;
	}

	/**
	 * @return the language
	 */
	public Locale getLanguage() {
		return language;
	}

	/**
	 * @param language the language to set
	 */
	public void setLanguage(Locale language) {
		this.language = language;
	}

	@Override
	public String toString() {
		StringBuffer stringBuffer = new StringBuffer();
		if(types != null) {
			stringBuffer.append(types.toString());
		}
		if(description != null) {
			if(description.length() > 32) {
				stringBuffer.append(": \"" + description.substring(0,32) +"...\"");
			} else {
				stringBuffer.append(": \"" + description +"\"");
			}
		}
		return stringBuffer.toString();
	}
}
