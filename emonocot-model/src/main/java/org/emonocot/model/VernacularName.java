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
import java.util.Locale;
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
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Size;

import com.fasterxml.jackson.annotation.JsonBackReference;
import com.fasterxml.jackson.annotation.JsonIgnore;
import org.emonocot.model.constants.Location;
import org.gbif.ecat.voc.LifeStage;
import org.gbif.ecat.voc.Sex;
import org.hibernate.annotations.Cascade;
import org.hibernate.annotations.CascadeType;
import org.hibernate.annotations.Where;

@Entity
public class VernacularName extends OwnedEntity {

	private static final long serialVersionUID = 5439026066792559240L;

	private Long id;

	private String vernacularName;

	private String source;

	private Locale language;

	private String temporal;

	private Location location;

	private String locality;

	private String countryCode;

	private Sex sex;

	private LifeStage lifeStage;

	private Boolean preferredName = Boolean.FALSE;

	private Boolean plural = Boolean.TRUE;

	private String organismPart;

	private String taxonRemarks;

	private Set<Annotation> annotations = new HashSet<Annotation>();

	private Taxon taxon;

	@Size(max = 255)
	public String getVernacularName() {
		return vernacularName;
	}

	public void setVernacularName(String vernacularName) {
		this.vernacularName = vernacularName;
	}

	@Size(max = 255)
	public String getTemporal() {
		return temporal;
	}

	public void setTemporal(String temporal) {
		this.temporal = temporal;
	}

	@Override
	@ManyToOne(fetch = FetchType.LAZY)
	@JsonBackReference("vernacularNames-taxon")
	public Taxon getTaxon() {
		return taxon;
	}

	@Override
	@JsonBackReference("vernacularNames-taxon")
	public void setTaxon(Taxon taxon) {
		this.taxon = taxon;
	}

	@Override
	@Id
	@GeneratedValue(generator = "table-hilo", strategy = GenerationType.TABLE)
	public Long getId() {
		return id;
	}

	@Size(max = 255)
	public String getSource() {
		return source;
	}

	public void setSource(String source) {
		this.source = source;
	}

	public Locale getLanguage() {
		return language;
	}

	public void setLanguage(Locale language) {
		this.language = language;
	}

	@Enumerated(value = EnumType.STRING)
	public Location getLocation() {
		return location;
	}

	public void setLocation(Location location) {
		this.location = location;
	}

	@Size(max = 255)
	public String getLocality() {
		return locality;
	}

	public void setLocality(String locality) {
		this.locality = locality;
	}

	@Size(max = 255)
	public String getCountryCode() {
		return countryCode;
	}

	public void setCountryCode(String countryCode) {
		this.countryCode = countryCode;
	}

	@Enumerated(value = EnumType.STRING)
	public Sex getSex() {
		return sex;
	}

	public void setSex(Sex sex) {
		this.sex = sex;
	}

	@Enumerated(value = EnumType.STRING)
	public LifeStage getLifeStage() {
		return lifeStage;
	}

	public void setLifeStage(LifeStage lifeStage) {
		this.lifeStage = lifeStage;
	}

	@NotNull
	public Boolean getPreferredName() {
		return preferredName;
	}

	public void setPreferredName(Boolean preferredName) {
		this.preferredName = preferredName;
	}

	@NotNull
	public Boolean getPlural() {
		return plural;
	}

	public void setPlural(Boolean plural) {
		this.plural = plural;
	}

	@Size(max = 255)
	public String getOrganismPart() {
		return organismPart;
	}

	public void setOrganismPart(String organismPart) {
		this.organismPart = organismPart;
	}

	@Size(max = 255)
	public String getTaxonRemarks() {
		return taxonRemarks;
	}

	public void setTaxonRemarks(String taxonRemarks) {
		this.taxonRemarks = taxonRemarks;
	}

	public void setId(Long id) {
		this.id = id;
	}

	@Transient
	@JsonIgnore
	public final String getClassName() {
		return "VernacularName";
	}

	@Override
	@OneToMany(fetch = FetchType.LAZY, orphanRemoval = true)
	@JoinColumn(name = "annotatedObjId")
	@Where(clause = "annotatedObjType = 'VernacularName'")
	@Cascade({ CascadeType.SAVE_UPDATE, CascadeType.MERGE, CascadeType.DELETE })
	@JsonIgnore
	public Set<Annotation> getAnnotations() {
		return annotations;
	}

	/**
	 * @param annotations
	 *            the annotations to set
	 */
	@Override
	public void setAnnotations(Set<Annotation> annotations) {
		this.annotations = annotations;
	}

	@Override
	public String toString() {
		StringBuffer stringBuffer = new StringBuffer();
		stringBuffer.append(vernacularName);
		if (language != null && !language.getDisplayLanguage().isEmpty()) {
			stringBuffer.append(" (" + language.getDisplayLanguage() + ")");
		}
		return stringBuffer.toString();
	}
}
