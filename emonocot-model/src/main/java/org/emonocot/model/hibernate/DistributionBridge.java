package org.emonocot.model.hibernate;

import org.apache.lucene.document.Document;
import org.apache.lucene.document.Field;
import org.apache.lucene.document.Fieldable;
//import org.apache.lucene.spatial.base.context.SpatialContextProvider;
//import org.apache.lucene.spatial.base.prefix.QuadPrefixGrid;
//import org.apache.lucene.spatial.strategy.SimpleSpatialFieldInfo;
//import org.apache.lucene.spatial.strategy.SpatialFieldInfo;
//import org.apache.lucene.spatial.strategy.SpatialStrategy;
//import org.apache.lucene.spatial.strategy.prefix.NGramPrefixGridStrategy;
import org.emonocot.model.description.Distribution;
import org.emonocot.model.geography.Continent;
import org.emonocot.model.geography.Country;
import org.emonocot.model.geography.GeographicalRegion;
import org.emonocot.model.geography.Region;
import org.hibernate.search.bridge.FieldBridge;
import org.hibernate.search.bridge.LuceneOptions;

/**
 *
 * @author ben
 *
 */
public class DistributionBridge implements FieldBridge {
 
   /**
    *
    */
//   private SpatialStrategy spatialStrategy
//       = new NGramPrefixGridStrategy(new QuadPrefixGrid(), 0);

    @Override
    public final void set(final String name, final Object value,
            final Document document, final LuceneOptions luceneOptions) {
        Distribution distribution = (Distribution) value;
        
        GeographicalRegion geographicalRegion = distribution.getRegion();

        if (geographicalRegion != null) {  // WARN this should not be null!
//            SpatialFieldInfo fieldInfo = new SimpleSpatialFieldInfo("area");
//            
//            for (Fieldable f : spatialStrategy
//                    .createFields(fieldInfo,
//                            geographicalRegion.getShape(), true, true)) {
//                if( f != null ) { // null if incompatibleGeometry && ignore
//                  document.add(f);
//                }
//              }
            
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
    }

}
