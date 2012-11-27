package org.emonocot.model.geography;

import com.vividsolutions.jts.geom.Polygon;

//import org.apache.lucene.document.Document;
//import org.apache.lucene.spatial.base.shape.Shape;
//import org.apache.lucene.spatial.strategy.SpatialStrategy;

/**
 *
 * @author ben
 *
 * @param <T>
 */
public interface GeographicalRegion<T extends GeographicalRegion> {
    /**
     *
     * @return the geographical region code
     */
    Object getCode();

    /**
     *
     * @param other the other region
     * @return 1 if other is after this, -1 if other is before this and 0 if
     *         other is equal to this
     */
    int compareNames(T other);

    /**
     *
     * @return the feature id
     */
    Integer getFeatureId();

    /**
     *
     * @return the envelope (MBR)
     */
    Polygon getEnvelope();

	Integer getLevel();

	String getPrefix();

	GeographicalRegion getParent();
	
	String name();
}
