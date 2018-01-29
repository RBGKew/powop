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

import java.io.Serializable;
import java.util.HashSet;
import java.util.Set;

import javax.persistence.Column;
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
import javax.persistence.OneToMany;
import javax.persistence.Transient;
import javax.validation.constraints.Size;

import org.apache.solr.common.SolrInputDocument;
import org.gbif.ecat.voc.Rank;
import org.gbif.ecat.voc.Sex;
import org.gbif.ecat.voc.TypeStatus;
import org.geotools.geometry.jts.JTSFactoryFinder;
import org.hibernate.annotations.Cascade;
import org.hibernate.annotations.CascadeType;
import org.hibernate.annotations.Where;
import org.powo.model.constants.TypeDesignationType;
import org.powo.model.marshall.json.TaxonDeserializer;
import org.powo.model.marshall.json.TaxonSerializer;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.context.ApplicationContext;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.databind.annotation.JsonDeserialize;
import com.fasterxml.jackson.databind.annotation.JsonSerialize;
import com.vividsolutions.jts.geom.Coordinate;
import com.vividsolutions.jts.geom.GeometryFactory;
import com.vividsolutions.jts.geom.Point;
import com.vividsolutions.jts.io.WKTWriter;

/**
 * @see http://rs.gbif.org/extension/gbif/1.0/typesandspecimen.xml
 */
@Entity
public class TypeAndSpecimen extends BaseData implements NonOwned, Searchable {

	private static final long serialVersionUID = -843014945343629009L;

	private static final Logger logger = LoggerFactory.getLogger(TypeAndSpecimen.class);

	private Long id;

	private TypeStatus typeStatus;

	private TypeDesignationType typeDesignationType;

	private String typeDesignatedBy;

	private String scientificName;

	private Rank taxonRank;

	private String bibliographicCitation;

	private String institutionCode;

	private String collectionCode;

	private String catalogNumber;

	private String locality;

	private Sex sex;

	private String recordedBy;

	private String source;

	private String verbatimEventDate;

	private String verbatimLabel;

	private String verbatimLongitude;

	private String verbatimLatitude;

	private Double decimalLatitude;

	private Double decimalLongitude;

	@Column
	private Point location;

	/**
	 * @return
	 */
	 private Set<Taxon> taxa = new HashSet<Taxon>();

	private Set<Annotation> annotations = new HashSet<Annotation>();


	@Override
	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	public Long getId() {
		return id;
	}

	/**
	 * @return the decimalLatitude
	 */
	public Double getDecimalLatitude() {
		return decimalLatitude;
	}

	/**
	 * @param decimalLatitude the decimalLatitude to set
	 */
	public void setDecimalLatitude(Double decimalLatitude) {
		this.decimalLatitude = decimalLatitude;
		updateLocation();
	}

	/**
	 * @return the location
	 */
	public Point getLocation() {
		return location;
	}

	/**
	 * @param location the location to set
	 */
	public void setLocation(Point location) {
		this.location = location;
	}

	/**
	 * @return the decimalLongitude
	 */
	public Double getDecimalLongitude() {
		return decimalLongitude;
	}

	/**
	 * @param decimalLongitude the decimalLongitude to set
	 */
	public void setDecimalLongitude(Double decimalLongitude) {
		this.decimalLongitude = decimalLongitude;
		updateLocation();
	}

	/**
	 *
	 * @param id Set the id
	 */
	public void setId(Long id) {
		this.id = id;
	}

	@Enumerated(value = EnumType.STRING)
	public TypeStatus getTypeStatus() {
		return typeStatus;
	}

	public void setTypeStatus(TypeStatus typeStatus) {
		this.typeStatus = typeStatus;
	}

	@Enumerated(value = EnumType.STRING)
	public TypeDesignationType getTypeDesignationType() {
		return typeDesignationType;
	}

	public void setTypeDesignationType(TypeDesignationType typeDesignationType) {
		this.typeDesignationType = typeDesignationType;
	}

	@Size(max = 255)
	public String getTypeDesignatedBy() {
		return typeDesignatedBy;
	}

	public void setTypeDesignatedBy(String typeDesignatedBy) {
		this.typeDesignatedBy = typeDesignatedBy;
	}

	@Size(max = 255)
	public String getScientificName() {
		return scientificName;
	}

	public void setScientificName(String scientificName) {
		this.scientificName = scientificName;
	}

	@Enumerated(value = EnumType.STRING)
	public Rank getTaxonRank() {
		return taxonRank;
	}

	public void setTaxonRank(Rank taxonRank) {
		this.taxonRank = taxonRank;
	}

	@Size(max = 255)
	public String getBibliographicCitation() {
		return bibliographicCitation;
	}

	public void setBibliographicCitation(String bibliographicCitation) {
		this.bibliographicCitation = bibliographicCitation;
	}

	@Size(max = 255)
	public String getInstitutionCode() {
		return institutionCode;
	}

	public void setInstitutionCode(String institutionCode) {
		this.institutionCode = institutionCode;
	}

	@Size(max = 255)
	public String getCollectionCode() {
		return collectionCode;
	}

	public void setCollectionCode(String collectionCode) {
		this.collectionCode = collectionCode;
	}

	@Size(max = 255)
	public String getCatalogNumber() {
		return catalogNumber;
	}

	public void setCatalogNumber(String catalogNumber) {
		this.catalogNumber = catalogNumber;
	}

	@Lob
	public String getLocality() {
		return locality;
	}

	public void setLocality(String locality) {
		this.locality = locality;
	}

	@Enumerated(value = EnumType.STRING)
	public Sex getSex() {
		return sex;
	}

	public void setSex(Sex sex) {
		this.sex = sex;
	}

	@Size(max = 255)
	public String getRecordedBy() {
		return recordedBy;
	}

	public void setRecordedBy(String recordedBy) {
		this.recordedBy = recordedBy;
	}

	@Size(max = 255)
	public String getSource() {
		return source;
	}

	public void setSource(String source) {
		this.source = source;
	}

	@Size(max = 255)
	public String getVerbatimEventDate() {
		return verbatimEventDate;
	}

	public void setVerbatimEventDate(String verbatimEventDate) {
		this.verbatimEventDate = verbatimEventDate;
	}

	@Size(max = 255)
	public String getVerbatimLabel() {
		return verbatimLabel;
	}

	public void setVerbatimLabel(String verbatimLabel) {
		this.verbatimLabel = verbatimLabel;
	}

	@Size(max = 255)
	public String getVerbatimLongitude() {
		return verbatimLongitude;
	}

	public void setVerbatimLongitude(String verbatimLongitude) {
		this.verbatimLongitude = verbatimLongitude;
	}

	@Size(max = 255)
	public String getVerbatimLatitude() {
		return verbatimLatitude;
	}

	public void setVerbatimLatitude(String verbatimLatitude) {
		this.verbatimLatitude = verbatimLatitude;
	}

	@ManyToMany(fetch = FetchType.LAZY)
	@JoinTable(name = "Taxon_TypeAndSpecimen", joinColumns = {@JoinColumn(name = "typesAndSpecimens_id")}, inverseJoinColumns = {@JoinColumn(name = "Taxon_id")})
	@Cascade({ CascadeType.SAVE_UPDATE, CascadeType.MERGE })
	@JsonSerialize(contentUsing = TaxonSerializer.class)
	public Set<Taxon> getTaxa() {
		return taxa;
	}

	@JsonDeserialize(contentUsing = TaxonDeserializer.class)
	public void setTaxa(Set<Taxon> taxa) {
		this.taxa = taxa;
	}

	@Transient
	@JsonIgnore
	public final String getClassName() {
		return "TypeAndSpecimen";
	}

	@OneToMany(fetch = FetchType.LAZY, orphanRemoval = true)
	@JoinColumn(name = "annotatedObjId")
	@Where(clause = "annotatedObjType = 'TypeAndSpecimen'")
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

	private void updateLocation() {
		if(this.decimalLatitude != null && this.decimalLongitude != null) {
			GeometryFactory geometryFactory = JTSFactoryFinder.getGeometryFactory(null);
			this.location = geometryFactory.createPoint(new Coordinate(this.decimalLongitude, this.decimalLatitude));
		} else {
			this.location = null;
		}
	}

	@Override
	public String toString() {
		StringBuffer stringBuffer = new StringBuffer();
		if(institutionCode != null) {
			stringBuffer.append(institutionCode);
		}
		if(catalogNumber != null) {
			stringBuffer.append(" " + catalogNumber);
		}
		if(collectionCode != null) {
			stringBuffer.append(" " + collectionCode);
		}
		return stringBuffer.toString();
	}

	@Override
	@Transient
	@JsonIgnore
	public String getDocumentId() {
		return getClassName() + "_" + getId();
	}

	protected void addField(SolrInputDocument sid, String name, Serializable value) {
		if(value != null && !value.toString().isEmpty()) {
			sid.addField(name, value);
		}
	}

	@Override
	public SolrInputDocument toSolrInputDocument(ApplicationContext ctx) {
		SolrInputDocument sid = new SolrInputDocument();
		sid.addField("id", getDocumentId());
		sid.addField("base.id_l", getId());
		sid.addField("base.class_searchable_b", false);
		sid.addField("base.class_s", getClass().getName());
		sid.addField("searchable.label_sort", toString());

		StringBuilder summary = new StringBuilder().append(getCatalogNumber()).append(" ")
				.append(getCollectionCode()).append(" ").append(getInstitutionCode()).append(" ").append(getLocality())
				.append(" ").append(getRecordedBy()).append(" ").append(getScientificName()).append(" ").append(getSex())
				.append(" ").append(getTypeDesignatedBy()).append(" ").append(getVerbatimEventDate()).append(" ").append(getVerbatimLabel())
				.append(" ").append(getVerbatimLatitude()).append(" ").append(getVerbatimLongitude()).append(" ").append(getTaxonRank())
				.append(" ").append(getTypeDesignationType()).append(" ").append(getTypeStatus());

		if(!getTaxa().isEmpty()) {
			boolean first = true;
			for(Taxon t : getTaxa()) {

				addField(sid,"taxon.family_ss", t.getFamily());
				addField(sid,"taxon.subfamily_ss", t.getSubfamily());
				addField(sid,"taxon.subtribe_ss", t.getSubtribe());
				addField(sid,"taxon.tribe_ss", t.getTribe());
				addField(sid,"taxon.genus_ss", t.getGenus());

				if(first) {
					//addField(sid,"taxon.class_s", t.getClazz());
					//addField(sid,"taxon.kingdom_s", t.getKingdom());
					//addField(sid,"taxon.phylum_s", getTaxon().getPhylum());
					addField(sid,"taxon.order_s", t.getOrder());
					addField(sid,"taxon.subgenus_s", t.getSubgenus());
				}
				summary.append(" ").append(t.getClazz())
				.append(" ").append(t.getClazz())
				.append(" ").append(t.getFamily())
				.append(" ").append(t.getGenus())
				.append(" ").append(t.getKingdom())
				.append(" ").append(t.getOrder())
				.append(" ").append(t.getPhylum())
				.append(" ").append(t.getSubfamily())
				.append(" ").append(t.getSubgenus())
				.append(" ").append(t.getSubtribe())
				.append(" ").append(t.getTribe());
				first = false;
			}
		}

		if(getAuthority() != null) {
			sid.addField("base.authority_s", getAuthority().getIdentifier());
			summary.append(" ").append(getAuthority().getIdentifier());
		}
		if (getLocation() != null) {
			try {
				WKTWriter wktWriter = new WKTWriter();
				sid.addField("geo", wktWriter.write(getLocation()));
			} catch (Exception e) {
				logger.error(e.getLocalizedMessage());
			}
		}

		sid.addField("searchable.solrsummary_t",summary.toString());

		return sid;
	}

}
