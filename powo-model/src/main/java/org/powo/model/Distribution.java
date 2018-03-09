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

import static org.gbif.ecat.voc.EstablishmentMeans.Introduced;
import static org.gbif.ecat.voc.EstablishmentMeans.Native;
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
import javax.persistence.JoinTable;
import javax.persistence.ManyToMany;
import javax.persistence.ManyToOne;
import javax.persistence.OneToMany;
import javax.persistence.Transient;
import javax.validation.constraints.Size;

import org.gbif.ecat.voc.EstablishmentMeans;
import org.gbif.ecat.voc.KnownTerm;
import org.gbif.ecat.voc.OccurrenceStatus;
import org.gbif.ecat.voc.ThreatStatus;
import org.hibernate.annotations.Cascade;
import org.hibernate.annotations.CascadeType;
import org.hibernate.annotations.Where;
import org.powo.model.constants.Location;
import org.powo.model.marshall.json.GeographicalRegionDeserializer;
import org.powo.model.marshall.json.ReferenceDeserializer;
import org.powo.model.marshall.json.ReferenceSerializer;

import com.fasterxml.jackson.annotation.JsonBackReference;
import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.databind.annotation.JsonDeserialize;
import com.fasterxml.jackson.databind.annotation.JsonSerialize;

/**
 * @see http://rs.gbif.org/extension/gbif/1.0/distribution.xml
 */
@Entity
public class Distribution extends OwnedEntity {

	private static final long serialVersionUID = -970244833684895241L;

	private Taxon taxon;

	private Location location;

	private String locality;

	private String occurrenceRemarks;

	private OccurrenceStatus occurrenceStatus;

	private EstablishmentMeans establishmentMeans;

	private ThreatStatus threatStatus;

	private Set<Annotation> annotations = new HashSet<Annotation>();

	private Set<Reference> references = new HashSet<Reference>();

	private Long id;

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
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	public Long getId() {
		return id;
	}

	/**
	 * Set the lowest level this georegion is concerned with.
	 *
	 * @param geoRegion
	 *            the geographical region this distribution is concerned with
	 */
	@JsonDeserialize(using = GeographicalRegionDeserializer.class)
	public void setLocation(Location geoRegion) {
		this.location = geoRegion;
	}

	/**
	 *
	 * @return the lowest level geo region this distribution is concerned with
	 */
	@Enumerated(value = EnumType.STRING)
	public Location getLocation() {
		return location;
	}

	/**
	 *
	 * @param newTaxon
	 *            Set the taxon that this distribution is about.
	 */
	@JsonBackReference("distribution-taxon")
	public void setTaxon(Taxon newTaxon) {
		this.taxon = newTaxon;
	}

	/**
	 * @return the annotations
	 */
	@OneToMany(fetch = FetchType.LAZY, orphanRemoval = true)
	@JoinColumn(name = "annotatedObjId")
	@Where(clause = "annotatedObjType = 'Distribution'")
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
	 *
	 * @return Get the taxon that this distribution is about.
	 */
	@ManyToOne(fetch = FetchType.LAZY)
	@JsonBackReference("distribution-taxon")
	public Taxon getTaxon() {
		return taxon;
	}



	/**
	 * @return the locality
	 */
	@Size(max = 255)
	public String getLocality() {
		return locality;
	}

	/**
	 * @param locality the locality to set
	 */
	public void setLocality(String locality) {
		this.locality = locality;
	}

	/**
	 * @return the occurrenceRemarks
	 */
	@Size(max = 255)
	public String getOccurrenceRemarks() {
		return occurrenceRemarks;
	}

	/**
	 * @param occurrenceRemarks the occurrenceRemarks to set
	 */
	public void setOccurrenceRemarks(String occurrenceRemarks) {
		this.occurrenceRemarks = occurrenceRemarks;
	}

	/**
	 * @return the occurrenceStatus
	 */
	@Enumerated(value = EnumType.STRING)
	public OccurrenceStatus getOccurrenceStatus() {
		return occurrenceStatus;
	}

	/**
	 * @param occurrenceStatus the occurrenceStatus to set
	 */
	public void setOccurrenceStatus(OccurrenceStatus occurrenceStatus) {
		this.occurrenceStatus = occurrenceStatus;
	}

	/**
	 * @return the establishmentMeans
	 */
	@Enumerated(value = EnumType.STRING)
	public EstablishmentMeans getEstablishmentMeans() {
		return establishmentMeans;
	}

	/**
	 * @param establishmentMeans the establishmentMeans to set
	 */
	public void setEstablishmentMeans(EstablishmentMeans establishmentMeans) {
		this.establishmentMeans = establishmentMeans;
	}

	/**
	 * @return the references
	 */
	@ManyToMany(fetch = FetchType.LAZY)
	@Cascade({ CascadeType.SAVE_UPDATE })
	@JoinTable(name = "Distribution_Reference", joinColumns = { @JoinColumn(name = "Distribution_id") }, inverseJoinColumns = { @JoinColumn(name = "references_id") })
	@JsonSerialize(contentUsing = ReferenceSerializer.class)
	public Set<Reference> getReferences() {
		return references;
	}

	/**
	 * @param references the references to set
	 */
	@JsonDeserialize(contentUsing = ReferenceDeserializer.class)
	public void setReferences(Set<Reference> references) {
		this.references = references;
	}

	@Enumerated(value = EnumType.STRING)
	public ThreatStatus getThreatStatus() {
		return threatStatus;
	}

	public void setThreatStatus(ThreatStatus threatStatus) {
		this.threatStatus = threatStatus;
	}

	@Transient
	@JsonIgnore
	public final String getClassName() {
		return "Distribution";
	}

	@Transient
	public KnownTerm getEstablishment() {
		if (getOccurrenceStatus() != null) {
			return getOccurrenceStatus();
		}

		if (getThreatStatus() != null) {
			return getThreatStatus();
		}

		if (getEstablishmentMeans() == null) {
			return Native;
		} else {
			switch (getEstablishmentMeans()) {
			case Introduced:
			case Invasive:
			case Managed:
			case Naturalised:
				return Introduced;
			case Uncertain:
			case Native:
			default:
				return Native;
			}
		}
	}

	@Override
	public String toString() {
		StringBuffer stringBuffer = new StringBuffer();
		if(location != null) {
			stringBuffer.append(location.toString());
		}
		if(occurrenceStatus != null) {
			stringBuffer.append(" " + occurrenceStatus.toString());
		}
		if(establishmentMeans != null) {
			stringBuffer.append(" " + establishmentMeans.toString());
		}
		return stringBuffer.toString();
	}

}
