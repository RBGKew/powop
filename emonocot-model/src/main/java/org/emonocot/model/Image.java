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

import javax.persistence.Column;
import javax.persistence.Entity;
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
import org.emonocot.model.constants.MediaType;
import org.emonocot.model.marshall.json.TaxonDeserializer;
import org.emonocot.model.marshall.json.TaxonSerializer;
import org.geotools.geometry.jts.JTSFactoryFinder;
import org.hibernate.annotations.Cascade;
import org.hibernate.annotations.CascadeType;
import org.hibernate.annotations.Type;
import org.hibernate.annotations.Where;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.databind.annotation.JsonDeserialize;
import com.fasterxml.jackson.databind.annotation.JsonSerialize;
import com.vividsolutions.jts.geom.Coordinate;
import com.vividsolutions.jts.geom.GeometryFactory;
import com.vividsolutions.jts.geom.Point;
import com.vividsolutions.jts.io.WKTWriter;

/**
 *
 * @author ben
 *
 */
@Entity
public class Image extends Multimedia {

	private static Logger logger = LoggerFactory.getLogger(Image.class);

	private static final long serialVersionUID = 3341900807619517602L;

	private String spatial;

	private String subject;

	private Point location;

	private Double latitude;

	private Double longitude;

	private Long id;

	private Taxon taxon;

	private Set<Taxon> taxa = new HashSet<Taxon>();

	private List<Comment> comments = new ArrayList<>();

	private Set<Annotation> annotations = new HashSet<Annotation>();

	/**
	 * REMEMBER: spatial is a reserved word in mysql!
	 * @return the location as a string
	 */
	@Column(name = "locality")
	@Size(max = 255)
	public String getSpatial() {
		return spatial;
	}

	public void setSpatial(final String locality) {
		this.spatial = locality;
	}

	@Size(max = 255)
	public String getSubject() {
		return subject;
	}

	public void setSubject(String keywords) {
		this.subject = keywords;
	}

	@Type(type = "spatialType")
	public Point getLocation() {
		return location;
	}

	public void setLocation(Point location) {
		this.location = location;
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
		updateLocation();
	}

	public Double getLongitude() {
		return longitude;
	}

	public void setLongitude(Double longitude) {
		this.longitude = longitude;
		updateLocation();
	}

	private void updateLocation() {
		if(this.latitude != null && this.longitude != null) {
			GeometryFactory geometryFactory = JTSFactoryFinder.getGeometryFactory(null);
			this.location = geometryFactory.createPoint(new Coordinate(this.longitude, this.latitude));
		} else {
			this.location = null;
		}
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

	/* (non-Javadoc)
	 * @see org.emonocot.model.Multimedia#getType()
	 */
	@Override
	@Transient
	public MediaType getType() {
		return MediaType.StillImage;
	}

	@Override
	public SolrInputDocument toSolrInputDocument() {
		SolrInputDocument sid = super.toSolrInputDocument();

		StringBuilder summary = new StringBuilder().append(getAudience())
				.append(" ").append(getCreator()).append(" ")
				.append(getDescription()).append(" ").append(getPublisher())
				.append(" ").append(getReferences()).append(" ")
				.append(getSpatial()).append(" ").append(getSubject())
				.append(" ").append(getTitle()).append(" ");
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
		if (getLocation() != null) {
			try {
				WKTWriter wktWriter = new WKTWriter();
				sid.addField("geo", wktWriter.write(getLocation()));
			} catch (Exception e) {
				logger.error(e.getLocalizedMessage());
			}
		}
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
}
