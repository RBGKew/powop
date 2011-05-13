package org.emonocot.checklist.view.assembler;

import org.dozer.CustomConverter;
import org.dozer.MappingException;
import org.joda.time.DateTime;

/**
 *
 * @author ben
 *
 */
public class DateTimeConverter implements CustomConverter {
    /**
     *
     * @param destination the existing destination field value
     * @param source the source field value
     * @param destClass the destination class
     * @param sourceClass the source class
     * @return a DateTime or null if the date has not been set
     */
    public final Object convert(final Object destination, final Object source,
            final Class destClass, final Class sourceClass) {
        if (source == null) {
            return null;
        }
        if (source instanceof DateTime) {
            return new DateTime(((DateTime) source));
        } else {
            throw new MappingException("Converter DateTimeConverter used"
                    + " incorrectly. Arguments passed in were:"
                    + destination + " and " + source);
        }
    }
}
