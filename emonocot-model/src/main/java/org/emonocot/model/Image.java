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

import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

import javax.persistence.CollectionTable;
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
import javax.persistence.ManyToMany;
import javax.persistence.ManyToOne;
import javax.persistence.OneToMany;
import javax.persistence.OrderBy;
import javax.persistence.Transient;
import javax.validation.constraints.Size;

import org.apache.solr.common.SolrInputDocument;
import org.emonocot.model.constants.DescriptionType;
import org.emonocot.model.constants.MediaType;
import org.emonocot.model.marshall.json.TaxonDeserializer;
import org.emonocot.model.marshall.json.TaxonSerializer;
import org.hibernate.annotations.Cascade;
import org.hibernate.annotations.CascadeType;
import org.hibernate.annotations.Where;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.databind.annotation.JsonDeserialize;
import com.fasterxml.jackson.databind.annotation.JsonSerialize;

/**
 * @see http://rs.gbif.org/extension/gbif/1.0/images.xml
 */
@Entity
public class Image extends Multimedia {

	private static Logger logger = LoggerFactory.getLogger(Image.class);

	private static final long serialVersionUID = 3341900807619517602L;

	private String subject;

	private Double latitude;

	private Double longitude;

	private Long id;

	private Taxon taxon;

	private String accessUri;

	private Set<Taxon> taxa = new HashSet<Taxon>();

	private List<Comment> comments = new ArrayList<>();

	private Set<Annotation> annotations = new HashSet<Annotation>();

	private String associatedObservationReference;

	private String associatedSpecimenReference;

	private String caption;

	private String providerManagedId;

	private Set<DescriptionType> subjectPart;

	private Taxon taxonCoverage;

	private String subType;

	private String worldRegion;

	private String countryCode;

	private String countryName;

	private String provinceState;

	private String sublocation;

	private Integer pixelXDimension;

	private Integer pixelYDimension;

	private Double rating;

	private String subjectCategoryVocabulary;

	@Size(max = 255)
	public String getSubject() {
		return subject;
	}

	public void setSubject(String keywords) {
		this.subject = keywords;
	}

	public void setId(Long newId) {
		this.id = newId;
	}

	@Id
	@GeneratedValue(generator = "table-hilo", strategy = GenerationType.TABLE)
	public Long getId() {
		return id;
	}

	public Double getLatitude() {
		return latitude;
	}

	public void setLatitude(Double latitude) {
		this.latitude = latitude;
	}

	public Double getLongitude() {
		return longitude;
	}

	public void setLongitude(Double longitude) {
		this.longitude = longitude;
	}

	/**
	 * @return the taxon
	 */
	@ManyToOne(fetch = FetchType.LAZY)
	public Taxon getTaxon() {
		return taxon;
	}

	/**
	 * @param taxon the taxon to set
	 */
	public void setTaxon(Taxon taxon) {
		this.taxon = taxon;
	}

	/* (non-Javadoc)
	 * @see org.emonocot.model.Multimedia#getTaxa()
	 */
	@ManyToMany(fetch = FetchType.LAZY)
	@JoinTable(name = "Taxon_Image", joinColumns = {@JoinColumn(name = "images_id")}, inverseJoinColumns = {@JoinColumn(name = "Taxon_id")})
	@Cascade({ CascadeType.SAVE_UPDATE, CascadeType.MERGE })
	@JsonSerialize(contentUsing = TaxonSerializer.class)
	public Set<Taxon> getTaxa() {
		return taxa;
	}

	/* (non-Javadoc)
	 * @see org.emonocot.model.Multimedia#setTaxa(java.util.Set)
	 */
	@Override
	@JsonDeserialize(contentUsing = TaxonDeserializer.class)
	public void setTaxa(Set<Taxon> taxa) {
		this.taxa = taxa;
	}

	/* (non-Javadoc)
	 * @see org.emonocot.model.Multimedia#getAnnotations()
	 */
	@Override
	@OneToMany(fetch = FetchType.LAZY, orphanRemoval = true)
	@JoinColumn(name = "annotatedObjId")
	@Where(clause = "annotatedObjType = 'Image'")
	@Cascade({ CascadeType.SAVE_UPDATE, CascadeType.MERGE, CascadeType.DELETE })
	@JsonIgnore
	public Set<Annotation> getAnnotations() {
		return annotations;
	}

	/* (non-Javadoc)
	 * @see org.emonocot.model.Multimedia#setAnnotations(java.util.Set)
	 */
	@Override
	public void setAnnotations(Set<Annotation> annotations) {
		this.annotations = annotations;
	}

	/* (non-Javadoc)
	 * @see org.emonocot.model.Multimedia#getComments()
	 */
	@OneToMany(fetch = FetchType.LAZY, orphanRemoval = true)
	@JoinColumn(name = "commentPage_id")
	@OrderBy("created DESC")
	@Where(clause = "commentPage_type = 'Image'")
	@Cascade({ CascadeType.SAVE_UPDATE, CascadeType.MERGE, CascadeType.DELETE })
	@JsonIgnore
	public List<Comment> getComments() {
		return comments;
	}

	/**
	 * @param comments - Comments made about this image
	 */
	@JsonIgnore
	public void setComments(List<Comment> comments) {
		this.comments = comments;
	}

	public String getAssociatedObservationReference() {
		return associatedObservationReference;
	}

	public void setAssociatedObservationReference(String associatedObservationReference) {
		this.associatedObservationReference = associatedObservationReference;
	}

	public String getAssociatedSpecimenReference() {
		return associatedSpecimenReference;
	}

	public void setAssociatedSpecimenReference(String associatedSpecimenReference) {
		this.associatedSpecimenReference = associatedSpecimenReference;
	}

	public String getCaption() {
		return caption;
	}

	public void setCaption(String caption) {
		this.caption = caption;
	}

	public String getProviderManagedId() {
		return providerManagedId;
	}

	public void setProviderManagedId(String providerManagedId) {
		this.providerManagedId = providerManagedId;
	}

	@ElementCollection
	@CollectionTable(name="image_SubjectPart", joinColumns=@JoinColumn(name="image_id"))
	@Column(name="subjectPart")
	@Enumerated(value = EnumType.STRING)
	public Set<DescriptionType> getSubjectPart() {
		return subjectPart;
	}

	public void setSubjectPart(Set<DescriptionType> subjectPart) {
		this.subjectPart = subjectPart;
	}

	@ManyToOne(fetch = FetchType.LAZY)
	@Cascade({ CascadeType.SAVE_UPDATE })
	@JsonSerialize(using = TaxonSerializer.class)
	public Taxon getTaxonCoverage() {
		return taxonCoverage;
	}

	@JsonDeserialize(using = TaxonDeserializer.class)
	public void setTaxonCoverage(Taxon taxonCoverage) {
		this.taxonCoverage = taxonCoverage;
	}

	@Override
	public SolrInputDocument toSolrInputDocument() {
		SolrInputDocument sid = super.toSolrInputDocument();

		StringBuilder summary = new StringBuilder().append(getAudience())
				.append(" ").append(getCreator()).append(" ")
				.append(getDescription()).append(" ").append(getPublisher())
				.append(" ").append(getReferences()).append(" ")
				.append(getSubject()).append(" ").append(getTitle()).append(" ");
		if(getTaxon() != null) {
			addField(sid,"taxon.family_ss", getTaxon().getFamily());
			addField(sid,"taxon.genus_ss", getTaxon().getGenus());
			addField(sid,"taxon.order_s", getTaxon().getOrder());
			addField(sid,"taxon.subfamily_ss", getTaxon().getSubfamily());
			addField(sid,"taxon.subgenus_s", getTaxon().getSubgenus());
			addField(sid,"taxon.subtribe_ss", getTaxon().getSubtribe());
			addField(sid,"taxon.tribe_ss", getTaxon().getTribe());
			summary.append(" ").append(getTaxon().getClazz())
			.append(" ").append(getTaxon().getClazz())
			.append(" ").append(getTaxon().getFamily())
			.append(" ").append(getTaxon().getGenus())
			.append(" ").append(getTaxon().getKingdom())
			.append(" ").append(getTaxon().getOrder())
			.append(" ").append(getTaxon().getPhylum())
			.append(" ").append(getTaxon().getSubfamily())
			.append(" ").append(getTaxon().getSubgenus())
			.append(" ").append(getTaxon().getSubtribe())
			.append(" ").append(getTaxon().getTribe());
		}
		sid.addField("searchable.solrsummary_t", summary);
		return sid;
	}

	@Override
	public String toString() {
		StringBuffer stringBuffer = new StringBuffer();
		stringBuffer.append(identifier);
		if(getTitle() != null) {
			stringBuffer.append(": \"" + getTitle() + "\"");
		}
		return stringBuffer.toString();
	}

	public String getAccessUri() {
		return accessUri;
	}

	public void setAccessUri(String accessUrl) {
		this.accessUri = accessUrl;
	}

	public String getSubType() {
		return subType;
	}

	public void setSubType(String subType) {
		this.subType = subType;
	}

	public String getWorldRegion() {
		return worldRegion;
	}

	public void setWorldRegion(String worldRegion) {
		this.worldRegion = worldRegion;
	}

	public String getCountryCode() {
		return countryCode;
	}

	public void setCountryCode(String countryCode) {
		this.countryCode = countryCode;
	}

	public String getCountryName() {
		return countryName;
	}

	public void setCountryName(String countryName) {
		this.countryName = countryName;
	}

	public String getProvinceState() {
		return provinceState;
	}

	public void setProvinceState(String provinceState) {
		this.provinceState = provinceState;
	}

	public String getSublocation() {
		return sublocation;
	}

	public void setSublocation(String sublocation) {
		this.sublocation = sublocation;
	}

	public Integer getPixelXDimension() {
		return pixelXDimension;
	}

	public void setPixelXDimension(Integer pixelXDimension) {
		this.pixelXDimension = pixelXDimension;
	}

	public Integer getPixelYDimension() {
		return pixelYDimension;
	}

	public void setPixelYDimension(Integer pixelYDimension) {
		this.pixelYDimension = pixelYDimension;
	}

	public Double getRating() {
		return rating;
	}

	public void setRating(Double rating) {
		this.rating = rating;
	}

	public String getSubjectCategoryVocabulary() {
		return subjectCategoryVocabulary;
	}

	public void setSubjectCategoryVocabulary(String subjectCategoryVocabulary) {
		this.subjectCategoryVocabulary = subjectCategoryVocabulary;
	}
}
