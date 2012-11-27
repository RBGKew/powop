package org.emonocot.model.geography;

import java.util.ArrayList;
import java.util.List;

import org.junit.Test;

import com.vividsolutions.jts.geom.Coordinate;
import com.vividsolutions.jts.geom.Geometry;
import com.vividsolutions.jts.geom.GeometryCollection;
import com.vividsolutions.jts.geom.GeometryFactory;

/**
 *
 * @author ben
 *
 */
public class GeographicalRegionTest {


    /**
     *
     */
    @Test
    public final void testCompareGeography() throws Exception {
        List<Geometry> list = new ArrayList<Geometry>();
        list.add(Continent.EUROPE.getEnvelope());
        list.add(Continent.AFRICA.getEnvelope());
        list.add(Region.CHINA.getEnvelope());
        list.add(Region.MACARONESIA.getEnvelope());
        list.add(Region.EASTERN_CANADA.getEnvelope());
        list.add(Country.FRA.getEnvelope());
        list.add(Country.ABT.getEnvelope());
        list.add(Country.GRB.getEnvelope());
        list.add(Country.IRE.getEnvelope());
        list.add(Country.ALG.getEnvelope());

        GeometryCollection geometryCollection = new GeometryCollection(
                list.toArray(new Geometry[list.size()]), new GeometryFactory());

        Coordinate[] envelope = geometryCollection.getEnvelope()
                .getCoordinates();
        for (Coordinate c : envelope) {
            System.out.println(Math.round(c.x) + " " + Math.round(c.y));
        }
    }

}
