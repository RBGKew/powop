package org.emonocot.checklist.view.oai.assembler;

import org.dozer.CustomConverter;
import org.dozer.MappingException;
import org.emonocot.checklist.model.ChangeType;

import org.openarchives.pmh.Status;

/**
 *
 * @author ben
 *
 */
public class StatusTypeConverter implements CustomConverter {

    /**
     *
     * @param destination
     *            the existing destination field value
     * @param source
     *            the source field value
     * @param destClass
     *            the destination class
     * @param sourceClass
     *            the source class
     * @return a DateTime or null if the date has not been set
     */
    public final Object convert(final Object destination, final Object source,
            final Class destClass, final Class sourceClass) {
        if (source == null) {
            return null;
        }
        if (source instanceof ChangeType) {
            ChangeType changeType = (ChangeType) source;
            switch (changeType) {
            case CREATE:
                return null;
            case MODIFIED:
                return null;
            case DELETE:
            default:
                return Status.deleted;
            }
        } else {
            throw new MappingException(
                    "Converter StatusTypeConverter used incorrectly."
                    + " Arguments passed in were:"
                            + destination + " and " + source);
        }
    }
}
