package org.emonocot.model.hibernate;

import java.io.IOException;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

//import com.googlecode.lucene.spatial.base.context.JtsSpatialContext;

/**
 *
 * @author ben
 *
 */
public class DistributionBridge 
//implements FieldBridge 
{
	
	Logger logger = LoggerFactory.getLogger(DistributionBridge.class);

    /**
     *
     */
//    public static SpatialContext SPATIAL_CONTEXT = new JtsSpatialContext();
    
    private boolean skipInit = false;

    /**
     *
     */
    public static int LEVELS = 8;
    
    /**
     *
     * @param skipInit Skip initialisation
     */
    public void setSkipInit(boolean skipInit) {
    	this.skipInit = skipInit;
    }

    /**
     *
     */
    public final void clearRegions() {
//        for (Continent continent : Continent.values()) {	
//            continent.setShape(null);
//        }
//
//        for (Region region : Region.values()) {
//            region.setShape(null);
//        }
//
//        for (Country country : Country.values()) {
//            country.setShape(null);
//        }
    }

    /**
     *
     * @throws IOException
     */
	public final void setupRegions() throws IOException {
//		logger.debug("setupRegions");
//		if (!skipInit) {
//			logger.debug("Initializing regions");
//			InputStream level1Stream = DistributionBridge.class
//					.getClassLoader().getResourceAsStream(
//							"org/emonocot/model/level1.txt");
//			SimplifyingDataReader level1DataReader = new SimplifyingDataReader(
//					level1Stream);
//			while (level1DataReader.hasNext()) {
//				SimplifiedData data = level1DataReader.next();
//				Continent continent = Continent.fromString(data.getId());				
//				continent.setShape(data.getShape());
//			}
//			level1Stream.close();
//
//			InputStream level2Stream = DistributionBridge.class
//					.getClassLoader().getResourceAsStream(
//							"org/emonocot/model/level2.txt");
//			SimplifyingDataReader level2DataReader = new SimplifyingDataReader(
//					level2Stream);
//			while (level2DataReader.hasNext()) {
//				SimplifiedData data = level2DataReader.next();
//				Region region = Region.fromString(data.getId());				
//				region.setShape(data.getShape());
//			}
//			level2Stream.close();
//
//			InputStream level3Stream = DistributionBridge.class
//					.getClassLoader().getResourceAsStream(
//							"org/emonocot/model/level3.txt");
//			SimplifyingDataReader level3DataReader = new SimplifyingDataReader(
//					level3Stream);
//			while (level3DataReader.hasNext()) {
//				SimplifiedData data = level3DataReader.next();
//				Country country = Country.fromString(data.getId());
//				country.setShape(data.getShape());
//			}
//			level3Stream.close();
//		}
	}

    /**
    *
    */
//   private SpatialStrategy spatialStrategy
//   = new DynamicPrefixStrategy(new GeohashSpatialPrefixGrid(
//           DistributionBridge.SPATIAL_CONTEXT, DistributionBridge.LEVELS ));

    /**
     * @param name Set the name of the field
     * @param value Set the value to be indexed
     * @param document Set the lucene document
     * @param luceneOptions Set the options for indexing
     */
/*    public final void set(final String name, final Object value,
            final Document document, final LuceneOptions luceneOptions) {
        Distribution distribution = (Distribution) value;

        GeographicalRegion geographicalRegion = distribution.getLocation();

        if (geographicalRegion != null) {  // WARN this should not be null!
        	
			SpatialFieldInfo fieldInfo = new SimpleSpatialFieldInfo("area");
			if (geographicalRegion.getShape() != null) {

				for (Fieldable f : spatialStrategy.createFields(fieldInfo,
						geographicalRegion.getShape(), true, true)) {
					if (f != null) {
						// null if incompatibleGeometry && ignore
						document.add(f);
					}
				}
			}

            if (geographicalRegion.getClass() == Country.class) {
                Country country = (Country) geographicalRegion;
                Field countryField = new Field("country",
                        country.name(), luceneOptions.getStore(),
                        luceneOptions.getIndex(),
                        luceneOptions.getTermVector());
                countryField.setBoost(luceneOptions.getBoost());
                document.add(countryField);

                Field regionField = new Field("region",
                        country.getRegion().name(), luceneOptions.getStore(),
                        luceneOptions.getIndex(),
                        luceneOptions.getTermVector());
                countryField.setBoost(luceneOptions.getBoost());
                document.add(regionField);

                Field continentField = new Field("continent",
                        country.getRegion().getContinent().name(),
                        luceneOptions.getStore(),
                        luceneOptions.getIndex(),
                        luceneOptions.getTermVector());
                continentField.setBoost(luceneOptions.getBoost());
                document.add(continentField);
            } else if (geographicalRegion.getClass() == Region.class) {
                Region region = (Region) geographicalRegion;
                Field regionField = new Field("region",
                        region.name(), luceneOptions.getStore(),
                        luceneOptions.getIndex(),
                        luceneOptions.getTermVector());
                regionField.setBoost(luceneOptions.getBoost());
                document.add(regionField);

                Field continentField = new Field("continent",
                        region.getContinent().name(),
                        luceneOptions.getStore(),
                        luceneOptions.getIndex(),
                        luceneOptions.getTermVector());
                continentField.setBoost(luceneOptions.getBoost());
                document.add(continentField);
            } else {
                Continent continent = (Continent) geographicalRegion;
                Field continentField = new Field("continent",
                        continent.name(),
                        luceneOptions.getStore(),
                        luceneOptions.getIndex(),
                        luceneOptions.getTermVector());
                continentField.setBoost(luceneOptions.getBoost());
                document.add(continentField);
            }
        }
    }*/

}
