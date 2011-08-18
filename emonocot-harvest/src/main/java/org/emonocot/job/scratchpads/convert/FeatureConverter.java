package org.emonocot.job.scratchpads.convert;

import org.emonocot.model.description.Feature;
import org.springframework.core.convert.converter.Converter;

/**
 *
 * @author ben
 *
 */
public class FeatureConverter implements Converter<String, Feature> {

    private static String TDWG_SPM_PREFIX = "http://rs.tdwg.org/ontology/voc/SPMInfoItems.rdf";
    
    public final Feature convert(final String value) {
        if (value == null) {
            return null;
        }
        if(value.startsWith(TDWG_SPM_PREFIX)) {
            String term = value.substring(TDWG_SPM_PREFIX.length()).toLowerCase();
            if(term.equals("general_description")) {
                return Feature.general;
            }
        }
        throw new IllegalArgumentException(value + " is not a legal value for Feature");
    }

}
