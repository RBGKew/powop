package org.emonocot.model;

import java.util.HashSet;
import java.util.Set;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.EnumType;
import javax.persistence.Enumerated;
import javax.persistence.FetchType;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.JoinTable;
import javax.persistence.Lob;
import javax.persistence.ManyToMany;
import javax.persistence.ManyToOne;
import javax.persistence.OneToMany;

import org.apache.solr.common.SolrInputDocument;
import org.codehaus.jackson.annotate.JsonIgnore;
import org.codehaus.jackson.map.annotate.JsonDeserialize;
import org.codehaus.jackson.map.annotate.JsonSerialize;
import org.emonocot.model.constants.ImageFormat;
import org.emonocot.model.marshall.json.TaxonDeserializer;
import org.emonocot.model.marshall.json.TaxonSerializer;
import org.geotools.geometry.jts.JTSFactoryFinder;
import org.hibernate.annotations.Cascade;
import org.hibernate.annotations.CascadeType;
import org.hibernate.annotations.Type;
import org.hibernate.annotations.Where;

import com.vividsolutions.jts.geom.Coordinate;
import com.vividsolutions.jts.geom.GeometryFactory;
import com.vividsolutions.jts.geom.Point;

/**
 *
 * @author ben
 *
 */
@Entity
public class Image extends SearchableObject implements NonOwned {
    /**
     *
     */
    private static long serialVersionUID = 3341900807619517602L;

    /**
     *
     */
    private String title;

    /**
     *
     */
    private String description;

    /**
     *
     */
    private String spatial;

    /**
     *
     */
    private ImageFormat format;

    /**
     *
     */
    private String subject;

    /**
     *
     */
    private Point location;
    
    /**
     *
     */
    private Double latitude;
    
    /**
     *
     */
    private Double longitude;

    /**
     *
     */
    private Taxon taxon;

    /**
     *
     */
    private Set<Taxon> taxa = new HashSet<Taxon>();

    /**
     *
     */
    private Set<Annotation> annotations = new HashSet<Annotation>();

    /**
     *
     */
    private Long id;
    
    /**
     *
     */
    private String creator;
    
    /**
     *
     */
    private String references;
    
    /**
     *
     */
    private String contributor;
    
    /**
     *
     */
    private String publisher;
    
    /**
     *
     */
    private String audience;

    /**
	 * @return the creator
	 */
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
	 * REMEMBER: references is a reserved word in mysql
	 * @return the references
	 */
	@Column(name = "source")
	public String getReferences() {
		return references;
	}

	/**
	 * @param references the references to set
	 */
	public void setReferences(String references) {
		this.references = references;
	}

	/**
    *
    * @return the description
    */
   @Lob
   public String getDescription() {
       return description;
   }

   /**
    *
    * @param description Set the description
    */
   public void setDescription(String description) {
       this.description = description;
   }

   /**
    * REMEMBER: spatial is a reserved word in mysql!
    * @return the location as a string
    */
   @Column(name = "locality")
   public String getSpatial() {
       return spatial;
   }

   /**
    *
    * @param locality Set the location as a string
    */
   public void setSpatial(final String locality) {
       this.spatial = locality;
   }

   /**
    *
    * @return the format of the image
    */
   @Enumerated(EnumType.STRING)
   public ImageFormat getFormat() {
       return format;
   }

   /**
    *
    * @param format Set the format of the image
    */
   public void setFormat(ImageFormat format) {
       this.format = format;
   }

   /**
    *
    * @return the keywords as a string, comma separated
    */
   public String getSubject() {
       return subject;
   }

   /**
    *
    * @param keywords Set the keywords as a string, comma separated
    */
   public void setSubject(String keywords) {
       this.subject = keywords;
   }

   /**
    *
    * @return the location the image was taken
    */
   @Type(type = "spatialType")
   public Point getLocation() {
       return location;
   }

   /**
    *
    * @param location Set the location the image was taken
    */
   public void setLocation(Point location) {
       this.location = location;
   }

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
    @GeneratedValue(generator = "system-increment")
    public Long getId() {
        return id;
    }

    /**
     *
     * @return the caption
     */
    public String getTitle() {
        return title;
    }

    /**
     *
     * @param caption
     *            Set the caption
     */
    public void setTitle(String title) {
        this.title = title;
    }

    /**
	 * @return the contributor
	 */
	public String getContributor() {
		return contributor;
	}

	/**
	 * @return the latitude
	 */
	public Double getLatitude() {
		return latitude;
	}

	/**
	 * @param latitude the latitude to set
	 */
	public void setLatitude(Double latitude) {
		this.latitude = latitude;
		updateLocation();
	}

	/**
	 * @return the longitude
	 */
	public Double getLongitude() {
		return longitude;
	}

	/**
	 * @param longitude the longitude to set
	 */
	public void setLongitude(Double longitude) {
		this.longitude = longitude;
		updateLocation();
	}

	/**
	 * @param contributor the contributor to set
	 */
	public void setContributor(String contributor) {
		this.contributor = contributor;
	}

	/**
	 * @return the publisher
	 */
	public String getPublisher() {
		return publisher;
	}

	/**
	 * @param publisher the publisher to set
	 */
	public void setPublisher(String publisher) {
		this.publisher = publisher;
	}

	/**
	 * @return the audience
	 */
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
     * The taxon (page) this image should link to - should be the lowest rank
     * taxon in taxa?
     *
     * @return the taxon this image is of
     */
    @ManyToOne(fetch = FetchType.LAZY)
    public Taxon getTaxon() {
        return taxon;
    }

    /**
     *
     * @param taxon
     *            Set the taxon this image is of
     */
    public void setTaxon(Taxon taxon) {
        this.taxon = taxon;
    }

    /**
     * The list of all taxa associated with this image - including e.g. genera
     * family, and so on.
     *
     * @return a list of taxa
     */
    @ManyToMany(fetch = FetchType.LAZY)
    @JoinTable(name = "Taxon_Image", joinColumns = {@JoinColumn(name = "images_id")}, inverseJoinColumns = {@JoinColumn(name = "Taxon_id")})
    @Cascade({ CascadeType.SAVE_UPDATE, CascadeType.MERGE })
    @JsonSerialize(contentUsing = TaxonSerializer.class)
    public Set<Taxon> getTaxa() {
        return taxa;
    }

    /**
     *
     * @param taxa
     *            Set the taxa associated with this image
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
    @Where(clause = "annotatedObjType = 'Image'")
    @Cascade({ CascadeType.SAVE_UPDATE, CascadeType.MERGE, CascadeType.DELETE })
    @JsonIgnore
    public Set<Annotation> getAnnotations() {
        return annotations;
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
     * @param annotations
     *            the annotations to set
     */
    public void setAnnotations(Set<Annotation> annotations) {
        this.annotations = annotations;
    }
    
    @Override
    public SolrInputDocument toSolrInputDocument() {
    	SolrInputDocument sid = super.toSolrInputDocument();
        sid.addField("id", "image_" + getId());
    	sid.addField("base.id_l", getId());
    	sid.addField("base.class_s", "org.emonocot.model.Image");
    	return sid;
    }
}
