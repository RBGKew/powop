package org.emonocot.job.scratchpads.convert;

import org.joda.time.DateTime;
import org.springframework.core.convert.converter.Converter;

/**
 * Converter which converts from 'Unix Time' - a moment in the datetime
 * continuum specified as a number of milliseconds from 1970-01-01T00:00Z. to
 * the JodaTime DateTime object.
 *
 * @author ben
 *
 */
public class UnixTimeConverter implements Converter<String, DateTime> {

    /**
     *
     */
    private static final Long MILLIS_IN_SECOND = 1000L;

    @Override
    public final DateTime convert(final String value) {
        if (value == null) {
            return null;
        }
        return new DateTime(Long.valueOf(value)
                * UnixTimeConverter.MILLIS_IN_SECOND);
    }

}
