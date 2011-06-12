package org.emonocot.test.xml;

import static org.junit.Assert.fail;

import java.io.IOException;

//import org.apache.lucene.spatial.base.context.SpatialContext;
//import org.apache.lucene.spatial.base.context.SpatialContextProvider;
//import org.apache.lucene.spatial.base.io.sample.SampleData;
//import org.apache.lucene.spatial.base.io.sample.SampleDataReader;
//import org.apache.lucene.spatial.base.shape.Shape;
import org.emonocot.model.geography.Continent;
import org.emonocot.model.geography.Country;
import org.emonocot.model.geography.Region;
import org.junit.Test;

//import com.googlecode.lucene.spatial.base.context.JtsSpatialContext;

/**
 *
 * @author ben
 *
 */
public class SpatialDataReaderTest {

    /**
     *
     */
//    private SpatialContext spatialContext = new JtsSpatialContext();

    /**
     *
     */
    @Test
    public final void test() {
//        try {
//
//            SampleDataReader level1DataReader = new SampleDataReader(
//                    getClass().getClassLoader().getResourceAsStream("org/emonocot/model/level1.txt"));
//            while (level1DataReader.hasNext()) {
//                SampleData data = level1DataReader.next();
//                Shape shape = spatialContext.readShape(data.shape);
//                Continent continent = Continent.fromString(data.id);
//                continent.setShape(shape);
//            }
//
//            SampleDataReader level2DataReader = new SampleDataReader(
//                    getClass().getClassLoader().getResourceAsStream("org/emonocot/model/level2.txt"));
//            while (level2DataReader.hasNext()) {
//                SampleData data = level2DataReader.next();
//                Shape shape = spatialContext.readShape(data.shape);
//                Region region = Region.fromString(data.id);
//                region.setShape(shape);
//            }
//            
//            SampleDataReader level3DataReader = new SampleDataReader(
//                    getClass().getClassLoader().getResourceAsStream("org/emonocot/model/level3.txt"));
//            while (level3DataReader.hasNext()) {
//                SampleData data = level3DataReader.next();
//                Shape shape = spatialContext.readShape(data.shape);
//                Country country = Country.fromString(data.id);
//                country.setShape(shape);
//            }
//        } catch (IOException e) {
//            fail();
//        }
    }
}
