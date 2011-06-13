package org.emonocot.model.geography;

import org.apache.lucene.spatial.base.shape.Shape;

/**
 *
 * @author ben
 *
 */
public interface GeographicalRegion {

    /**
     *
     * @return the representation of this region as a shape
     */
    Shape getShape();

    /**
     *
     * @param shape Set the shape of this region
     */
    void setShape(Shape shape);
}
