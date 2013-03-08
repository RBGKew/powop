package org.emonocot.model;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
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
import javax.persistence.OrderBy;

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
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

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
public class Image extends SearchableObject implements NonOwned {
	
	private static Logger logger = LoggerFactory.getLogger(Image.class);
	
    private static long serialVersionUID = 3341900807619517602L;

    private String title;

    private String description;

    private String spatial;

    private ImageFormat format;

    private String subject;

    private Point location;
    
    private Double latitude;

    private Double longitude;

    private Taxon taxon;

    private Set<Taxon> taxa = new HashSet<Taxon>();

    private Set<Annotation> annotations = new HashSet<Annotation>();

    private Long id;
    
    private String creator;

    private String references;
    
    private String contributor;

    private String publisher;
    
    private String audience;
    
    private List<Comment> comments = new ArrayList<Comment>();

	public String getCreator() {
		return creator;
	}

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

	public void setReferences(String references) {
		this.references = references;
	}

   @Lob
   public String getDescription() {
       return description;
   }

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

   public void setSpatial(final String locality) {
       this.spatial = locality;
   }

   @Enumerated(EnumType.STRING)
   public ImageFormat getFormat() {
       return format;
   }

   public void setFormat(ImageFormat format) {
       this.format = format;
   }

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
    @GeneratedValue(generator = "system-increment")
    public Long getId() {
        return id;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

	public String getContributor() {
		return contributor;
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

	public void setContributor(String contributor) {
		this.contributor = contributor;
	}

	public String getPublisher() {
		return publisher;
	}

	public void setPublisher(String publisher) {
		this.publisher = publisher;
	}

	public String getAudience() {
		return audience;
	}

	public void setAudience(String audience) {
		this.audience = audience;
	}

    @ManyToOne(fetch = FetchType.LAZY)
    public Taxon getTaxon() {
        return taxon;
    }


    public void setTaxon(Taxon taxon) {
        this.taxon = taxon;
    }

    @ManyToMany(fetch = FetchType.LAZY)
    @JoinTable(name = "Taxon_Image", joinColumns = {@JoinColumn(name = "images_id")}, inverseJoinColumns = {@JoinColumn(name = "Taxon_id")})
    @Cascade({ CascadeType.SAVE_UPDATE, CascadeType.MERGE })
    @JsonSerialize(contentUsing = TaxonSerializer.class)
    public Set<Taxon> getTaxa() {
        return taxa;
    }

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
    
    /**
	 * @return the comments
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
	 * @param comments the comments to set
	 */
    @JsonIgnore
	public void setComments(List<Comment> comments) {
		this.comments = comments;
	}
    
    @Override
    public SolrInputDocument toSolrInputDocument() {
    	SolrInputDocument sid = super.toSolrInputDocument();
    	sid.addField("searchable.label_sort", getTitle());
    	addField(sid,"image.audience_t", getAudience());
    	
    	addField(sid,"image.creator_t", getCreator());
    	addField(sid,"image.description_t", getDescription());
    	addField(sid,"image.publisher_t", getPublisher());
    	addField(sid,"image.references_t", getReferences());
    	addField(sid,"image.spatial_t", getSpatial());
    	addField(sid,"image.subject_t", getSubject());
    	sid.addField("image.title_t", getTitle());    	
    	
		StringBuilder summary = new StringBuilder().append(getAudience())
				.append(" ").append(getCreator()).append(" ")
				.append(getDescription()).append(" ").append(getPublisher())
				.append(" ").append(getReferences()).append(" ")
				.append(getSpatial()).append(" ").append(getSubject())
				.append(" ").append(getTitle()).append(" ");
    	if(getTaxon() != null) {
    		addField(sid,"taxon.class_s", getTaxon().getClazz());
    	    addField(sid,"taxon.family_s", getTaxon().getFamily());
    	    addField(sid,"taxon.genus_s", getTaxon().getGenus());
    	    addField(sid,"taxon.kingdom_s", getTaxon().getKingdom());
    	    addField(sid,"taxon.order_s", getTaxon().getOrder());
    	    addField(sid,"taxon.phylum_s", getTaxon().getPhylum());
    	    addField(sid,"taxon.subfamily_s", getTaxon().getSubfamily());
    	    addField(sid,"taxon.subgenus_s", getTaxon().getSubgenus());
    	    addField(sid,"taxon.subtribe_s", getTaxon().getSubtribe());
    	    addField(sid,"taxon.tribe_s", getTaxon().getTribe());
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
    	if(title != null) {
    		stringBuffer.append(": \"" + title + "\"");
    	}
    	return stringBuffer.toString();
    }
}
