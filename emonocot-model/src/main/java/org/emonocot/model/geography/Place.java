package org.emonocot.model.geography;

import java.util.HashSet;
import java.util.Set;

import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.OneToMany;
import javax.persistence.Transient;

import org.apache.solr.common.SolrInputDocument;
import org.codehaus.jackson.annotate.JsonIgnore;
import org.codehaus.jackson.map.annotate.JsonDeserialize;
import org.codehaus.jackson.map.annotate.JsonSerialize;
import org.emonocot.model.Annotation;
import org.emonocot.model.SearchableObject;
import org.emonocot.model.marshall.json.ShapeDeserializer;
import org.emonocot.model.marshall.json.ShapeSerializer;
import org.hibernate.annotations.Cascade;
import org.hibernate.annotations.CascadeType;
import org.hibernate.annotations.Type;
import org.hibernate.annotations.Where;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.vividsolutions.jts.geom.Envelope;
import com.vividsolutions.jts.geom.Geometry;
import com.vividsolutions.jts.geom.GeometryFactory;
import com.vividsolutions.jts.geom.MultiPolygon;
import com.vividsolutions.jts.geom.Point;
import com.vividsolutions.jts.geom.Polygon;
import com.vividsolutions.jts.io.WKTWriter;
import com.vividsolutions.jts.simplify.TopologyPreservingSimplifier;

/**
 * @author jk00kg
 *
 */
@Entity
public class Place extends SearchableObject {

    /**
     *
     */
    private static Logger logger = LoggerFactory.getLogger(Place.class);

	/**
	 * 
	 */
	private Long id;

	/**
	 * 
	 */
	private String title;
	
	/**
	 * 
	 */
	private String fipsCode;
	
	/**
	 * A featureId (OGR_FID) referenced by the map server
	 */
	private Long mapFeatureId;

	/**
	 * A JTS Polygon for this place
	 * @see com.vividsolutions.jts.geom.Polygon
	 */
	private Geometry shape;

	/**
	 * Used for a single point reference of a place.
	 * Usually in place of a shape, but possibly 
	 */
	private Point point;
	
	private Set<Annotation> annotations = new HashSet<Annotation>();


    @Id
    @GeneratedValue(generator = "system-increment")
	public Long getId() {
		return id;
	}

    /**
     * @param id the id to set
     */
    public void setId(final Long id) {
        this.id = id;
    }

	/**
	 * @return the name
	 */
	public String getTitle() {
		return title;
	}

	/**
	 * @param name the name to set
	 */
	public void setTitle(final String name) {
		this.title = name;
	}

	/**
	 * @return the fipsCode
	 */
	public String getFipsCode() {
		return fipsCode;
	}

	/**
	 * @param fipsCode the fipsCode to set
	 */
	public void setFipsCode(final String fipsCode) {
		this.fipsCode = fipsCode;
	}

	/**
	 * @return the mapFeatureId
	 */
	public Long getMapFeatureId() {
		return mapFeatureId;
	}

	/**
	 * @param mapFeatureId the mapFeatureId to set
	 */
	public void setMapFeatureId(final Long mapFeatureId) {
		this.mapFeatureId = mapFeatureId;
	}

	/**
	 * @return the shape
	 */
	@Type(type="spatialType")
	@JsonSerialize(using=ShapeSerializer.class)
	public Geometry getShape() {
		return shape;
	}

	/**
	 * @param shape the shape to set
	 */
	@JsonDeserialize(using=ShapeDeserializer.class)
	public void setShape(Geometry shape) {
		this.shape = shape;
		
	}

	/**
	 * @return the point
	 */
	@Type(type="spatialType")
	public Point getPoint() {
		return point;
	}

	/**
	 * @param point the point to set
	 */
	public void setPoint(Geometry point) {
		try{
			if (point instanceof Point){
				this.point = (Point) point;
			}
		} catch (ClassCastException e) {
			logger.error("Expected a point but got " + point.toText() + " " + point, e);
		}
	}
    
    /**
     * @return an Envelope  
     */
	@Transient
	@JsonIgnore
    public Envelope getEnvelope(){
    	if(shape != null){
			return shape.getEnvelopeInternal();
    	} else if(point != null){
			return point.getEnvelopeInternal();
		} else {
			//an empty Envelope
			return new Envelope();
		} 
    }
	
	@OneToMany(fetch = FetchType.LAZY, orphanRemoval = true)
    @JoinColumn(name = "annotatedObjId")
    @Where(clause = "annotatedObjType = 'Place'")
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

    @Override
    public SolrInputDocument toSolrInputDocument() {
    	SolrInputDocument sid = super.toSolrInputDocument();
    	sid.addField("searchable.label_sort", getTitle());
    	addField(sid,"place.fips_code_t", getFipsCode());
    	StringBuilder summary = new StringBuilder().append(getTitle()).append(" ").append(getFipsCode());
    	sid.addField("searchable.solrsummary_t", summary);
    	try {
    		WKTWriter wktWriter = new WKTWriter();
    		sid.addField("geo", wktWriter.write(TopologyPreservingSimplifier.simplify(getShape(),0.01)));
		} catch (Exception e) {
			logger.error(e.getLocalizedMessage());
		}
    	return sid;
    }
}
