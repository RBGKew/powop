package org.emonocot.job.scratchpads.convert;

import org.emonocot.model.description.Feature;
import org.springframework.core.convert.converter.Converter;

/**
 *
 * @author ben
 *
 */
public class FeatureConverter implements Converter<String, Feature> {

    public final Feature convert(final String value) {
        if (value == null) {
            return null;
        }
        return Feature.fromString(value);
    }

}
