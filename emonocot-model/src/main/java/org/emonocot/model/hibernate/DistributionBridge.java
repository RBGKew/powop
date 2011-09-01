package org.emonocot.model.hibernate;

import org.apache.lucene.document.Document;
import org.apache.lucene.document.Field;
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
     * @param name Set the name of the field
     * @param value Set the value to be indexed
     * @param document Set the lucene document
     * @param luceneOptions Set the options for indexing
     */
    public final void set(final String name, final Object value,
            final Document document, final LuceneOptions luceneOptions) {
        Distribution distribution = (Distribution) value;

        GeographicalRegion geographicalRegion = distribution.getRegion();

        if (geographicalRegion != null) {  // WARN this should not be null!

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
