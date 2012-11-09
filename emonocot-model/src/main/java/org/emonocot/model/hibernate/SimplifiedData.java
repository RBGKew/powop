package org.emonocot.model.hibernate;

//import org.apache.lucene.spatial.base.shape.Shape;
//
//import com.googlecode.lucene.spatial.base.shape.JtsGeometry;
import com.vividsolutions.jts.geom.Geometry;
import com.vividsolutions.jts.io.ParseException;
import com.vividsolutions.jts.io.WKTReader;
import com.vividsolutions.jts.simplify.TopologyPreservingSimplifier;

/**
 *
 * @author ben
 *
 */
public class SimplifiedData {
    /**
     *
     */
    private String id;
    /**
     *
     */
    private String name;
    /**
     *
     */
//    private Shape shape;

    /**
     *
     * @param line Set the input line
     * @param reader Set the WKTReader
     */
    public SimplifiedData(final String line, final WKTReader reader) {
//        String[] vals = line.split("\t");
//        id = vals[0];
//        name = vals[1];
//        Geometry geo;
//        try {
//            geo = reader.read(vals[2]);
//            Geometry simpler = TopologyPreservingSimplifier
//                    .simplify(geo, 0.01d);
//            shape = new JtsGeometry(simpler);
//        } catch (ParseException e) {
//            throw new RuntimeException("Exception parsing " + name);
//        }
    }

    /**
     * @return the id
     */
    public String getId() {
        return id;
    }

    /**
     * @return the name
     */
    public String getName() {
        return name;
    }

    /**
     * @return the shape
     */
//    public Shape getShape() {
//        return shape;
//    }

}
