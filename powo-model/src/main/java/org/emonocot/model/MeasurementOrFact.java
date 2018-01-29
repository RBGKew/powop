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

import java.util.HashSet;
import java.util.Set;

import javax.persistence.Entity;
import javax.persistence.EnumType;
import javax.persistence.Enumerated;
import javax.persistence.FetchType;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.OneToMany;
import javax.persistence.Transient;
import javax.validation.constraints.Size;

import org.emonocot.model.constants.MeasurementUnit;
import org.emonocot.model.marshall.json.DateTimeDeserializer;
import org.emonocot.model.marshall.json.DateTimeSerializer;
import org.emonocot.model.marshall.json.TermDeserializer;
import org.emonocot.model.marshall.json.TermSerializer;
import org.gbif.dwc.terms.Term;
import org.hibernate.annotations.Cascade;
import org.hibernate.annotations.CascadeType;
import org.hibernate.annotations.Type;
import org.hibernate.annotations.Where;
import org.joda.time.DateTime;

import com.fasterxml.jackson.annotation.JsonBackReference;
import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.databind.annotation.JsonDeserialize;
import com.fasterxml.jackson.databind.annotation.JsonSerialize;

/**
 * @see http://rs.gbif.org/extension/dwc/measurements_or_facts.xml
 */
@Entity
public class MeasurementOrFact extends OwnedEntity {

	private static final long serialVersionUID = -1400551717852313792L;

	private Long id;

	private Term measurementType;

	private String measurementValue;

	private String measurementAccuracy;

	private MeasurementUnit measurementUnit;

	private DateTime measurementDeterminedDate;

	private String measurementDeterminedBy;

	private String measurementMethod;

	private String measurementRemarks;

	private String bibliographicCitation;

	private Taxon taxon;

	private Set<Annotation> annotations = new HashSet<Annotation>();

	private String source;

	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	public Long getId() {
		return id;
	}

	@Transient
	@JsonIgnore
	public final String getClassName() {
		return "MeasurementOrFact";
	}

	@JsonSerialize(using = TermSerializer.class)
	@Type(type="termUserType")
	public Term getMeasurementType() {
		return measurementType;
	}

	@JsonDeserialize(using = TermDeserializer.class)
	public void setMeasurementType(Term measurementType) {
		this.measurementType = measurementType;
	}

	@Size(max = 255)
	public String getMeasurementValue() {
		return measurementValue;
	}

	public void setMeasurementValue(String measurementValue) {
		this.measurementValue = measurementValue;
	}

	@Size(max = 255)
	public String getMeasurementAccuracy() {
		return measurementAccuracy;
	}

	public void setMeasurementAccuracy(String measurementAccuracy) {
		this.measurementAccuracy = measurementAccuracy;
	}

	@Enumerated(value = EnumType.STRING)
	public MeasurementUnit getMeasurementUnit() {
		return measurementUnit;
	}

	public void setMeasurementUnit(MeasurementUnit measurementUnit) {
		this.measurementUnit = measurementUnit;
	}

	@Type(type="dateTimeUserType")
	@JsonSerialize(using = DateTimeSerializer.class)
	public DateTime getMeasurementDeterminedDate() {
		return measurementDeterminedDate;
	}

	@JsonDeserialize(using = DateTimeDeserializer.class)
	public void setMeasurementDeterminedDate(DateTime measurementDeterminedDate) {
		this.measurementDeterminedDate = measurementDeterminedDate;
	}

	@Size(max = 255)
	public String getMeasurementDeterminedBy() {
		return measurementDeterminedBy;
	}

	public void setMeasurementDeterminedBy(String measurementDeterminedBy) {
		this.measurementDeterminedBy = measurementDeterminedBy;
	}

	@Size(max = 255)
	public String getMeasurementMethod() {
		return measurementMethod;
	}

	public void setMeasurementMethod(String measurementMethod) {
		this.measurementMethod = measurementMethod;
	}

	@Size(max = 255)
	public String getMeasurementRemarks() {
		return measurementRemarks;
	}

	public void setMeasurementRemarks(String measurementRemarks) {
		this.measurementRemarks = measurementRemarks;
	}

	@ManyToOne(fetch = FetchType.LAZY)
	@JsonBackReference("measurementsOrFacts-taxon")
	public Taxon getTaxon() {
		return taxon;
	}

	@JsonBackReference("measurementsOrFacts-taxon")
	public void setTaxon(Taxon taxon) {
		this.taxon = taxon;
	}

	public void setId(Long id) {
		this.id = id;
	}

	@OneToMany(fetch = FetchType.LAZY, orphanRemoval = true)
	@JoinColumn(name = "annotatedObjId")
	@Where(clause = "annotatedObjType = 'MeasurementOrFact'")
	@Cascade({ CascadeType.SAVE_UPDATE, CascadeType.MERGE, CascadeType.DELETE })
	@JsonIgnore
	public Set<Annotation> getAnnotations() {
		return annotations;
	}

	/**
	 * @param annotations
	 *            the annotations to set
	 */
	@JsonIgnore
	public void setAnnotations(Set<Annotation> annotations) {
		this.annotations = annotations;
	}

	/**
	 * @return the bibliographicCitation
	 */
	public String getBibliographicCitation() {
		return bibliographicCitation;
	}

	/**
	 * @param bibliographicCitation the bibliographicCitation to set
	 */
	public void setBibliographicCitation(String bibliographicCitation) {
		this.bibliographicCitation = bibliographicCitation;
	}

	@Override
	public String toString() {
		StringBuffer stringBuffer = new StringBuffer();
		if(measurementType != null) {
			stringBuffer.append(measurementType.toString());
		}
		if(measurementValue != null) {
			stringBuffer.append(": \"" + measurementValue + "\"");
		}
		return stringBuffer.toString();
	}

	public void setSource(String source) {
		this.source = source;
	}

	public String getSource() {
		return source;
	}
	
	@Transient
	public boolean isType(Term type){
		if(this.measurementType == type){
			return true;
		}
		return false;	
	}
}
