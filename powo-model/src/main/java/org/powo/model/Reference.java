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

import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.JoinTable;
import javax.persistence.Lob;
import javax.persistence.ManyToMany;
import javax.persistence.OneToMany;
import javax.persistence.SequenceGenerator;
import javax.persistence.Transient;
import javax.validation.constraints.Size;

import org.hibernate.annotations.Cascade;
import org.hibernate.annotations.CascadeType;
import org.hibernate.annotations.Where;
import org.powo.model.constants.ReferenceType;
import org.powo.model.marshall.json.TaxonDeserializer;
import org.powo.model.marshall.json.TaxonSerializer;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.databind.annotation.JsonDeserialize;
import com.fasterxml.jackson.databind.annotation.JsonSerialize;

import lombok.ToString;

/**
 * Schema definition: http://rs.gbif.org/extension/gbif/1.0/references.xml
 */
@Entity
@ToString(of = {"bibliographicCitation", "subject", "date"})
public class Reference extends BaseData implements NonOwned {

	private static final long serialVersionUID = -5928234699377084008L;

	private String title;

	private String date;

	private ReferenceType type;

	private String creator;

	private String source;

	private String bibliographicCitation;

	private Locale language;

	private Set<Taxon> taxa = new HashSet<Taxon>();

	private Set<Annotation> annotations = new HashSet<Annotation>();

	private Long id;

	private String description;

	private String subject;

	private String taxonRemarks;

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
	@GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "referenceSequenceGenerator")
	@SequenceGenerator(name = "referenceSequenceGenerator", allocationSize = 1000, sequenceName = "seq_reference")
	public Long getId() {
		return id;
	}

	/**
	 *
	 * @return the author
	 */
	@Size(max = 255)
	public String getCreator() {
		return creator;
	}

	/**
	 *
	 * @param newCreator
	 *            Set the author
	 */
	public void setCreator(String newCreator) {
		this.creator = newCreator;
	}

	/**
	 *
	 * @return the publication this reference was published in
	 */
	@Size(max = 255)
	public String getSource() {
		return source;
	}

	/**
	 *
	 * @param newSource
	 *            Set the publication this reference was published in
	 */
	public void setSource(String newSource) {
		this.source = newSource;
	}

	/**
	 *
	 * @return the full citation
	 */
	@Lob
	public String getBibliographicCitation() {
		return bibliographicCitation;
	}

	/**
	 *
	 * @param newCitation
	 *            Set the full citation
	 */
	public void setBibliographicCitation(String newCitation) {
		this.bibliographicCitation = newCitation;
	}

	/**
	 *
	 * @param newTitle
	 *            set the title
	 */
	public void setTitle(String newTitle) {
		this.title = newTitle;
	}

	/**
	 *
	 * @param newReferenceType
	 *            set the reference type
	 */
	public void setType(ReferenceType newReferenceType) {
		this.type = newReferenceType;
	}

	/**
	 * @return the datePublished
	 */
	@Size(max = 255)
	public String getDate() {
		return date;
	}

	/**
	 * @param newDatePublished
	 *            the datePublished to set
	 */
	public void setDate(String newDate) {
		this.date = newDate;
	}

	/**
	 * @return the title
	 */
	@Size(max = 255)
	public String getTitle() {
		return title;
	}

	/**
	 * @return the type
	 */
	public ReferenceType getType() {
		return type;
	}

	/**
	 * The list of all taxa associated with this reference.
	 *
	 * @return a set of taxa
	 */
	@ManyToMany(fetch = FetchType.LAZY)
	@JoinTable(name = "Taxon_Reference", joinColumns = {@JoinColumn(name = "references_id")}, inverseJoinColumns = {@JoinColumn(name = "Taxon_id")})
	@Cascade({ CascadeType.SAVE_UPDATE, CascadeType.MERGE })
	@JsonSerialize(contentUsing = TaxonSerializer.class)
	public Set<Taxon> getTaxa() {
		return taxa;
	}

	/**
	 *
	 * @param taxa
	 *            Set the taxa associated with this reference
	 */
	@JsonDeserialize(contentUsing = TaxonDeserializer.class)
	public void setTaxa(Set<Taxon> taxa) {
		this.taxa = taxa;
	}

	/**
	 * @return the annotations
	 */
	@OneToMany(fetch = FetchType.LAZY, orphanRemoval = true)
	@JoinColumn(name = "annotatedObjId")
	@Where(clause = "annotatedObjType = 'Reference'")
	@Cascade({ CascadeType.SAVE_UPDATE, CascadeType.MERGE, CascadeType.DELETE })
	@JsonIgnore
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
	 * @return the abstract
	 */
	@Lob
	public String getDescription() {
		return description;
	}

	/**
	 * @param description the abstract to set
	 */
	public void setDescription(String description) {
		this.description = description;
	}

	/**
	 * @return the keywords
	 */
	@Size(max = 255)
	public String getSubject() {
		return subject;
	}

	/**
	 * @param subject the keywords to set
	 */
	public void setSubject(String subject) {
		this.subject = subject;
	}

	@Transient
	@JsonIgnore
	public String getClassName() {
		return "Reference";
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

	/**
	 * @return the taxonRemarks
	 */
	@Size(max = 255)
	public String getTaxonRemarks() {
		return taxonRemarks;
	}

	/**
	 * @param taxonRemarks the taxonRemarks to set
	 */
	public void setTaxonRemarks(String taxonRemarks) {
		this.taxonRemarks = taxonRemarks;
	}
}
