package org.emonocot.job.scratchpads.convert;

import org.emonocot.model.common.License;
import org.springframework.core.convert.converter.Converter;

/**
 *
 * @author ben
 *
 */
public class LicenseConverter implements Converter<String, License> {

    @Override
    public final License convert(final String value) {
        if (value == null) {
            return null;
        }
        return License.fromString(value);
    }

}
