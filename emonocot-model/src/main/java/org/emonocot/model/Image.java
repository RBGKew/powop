package org.emonocot.model;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.validation.constraints.Size;

import org.apache.solr.common.SolrInputDocument;
import org.geotools.geometry.jts.JTSFactoryFinder;
import org.hibernate.annotations.Type;
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
public class Image extends Multimedia {
	
	private static Logger logger = LoggerFactory.getLogger(Image.class);
	
    private static final long serialVersionUID = 3341900807619517602L;

    private String spatial;

    private String subject;

    private Point location;
    
    private Double latitude;

    private Double longitude;

    @Id
    @GeneratedValue(generator = "table-hilo", strategy = GenerationType.TABLE)
    private Long id;

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
