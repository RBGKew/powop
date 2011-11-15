package org.emonocot.checklist.view.assembler;

import org.dozer.CustomConverter;
import org.dozer.MappingException;
import org.emonocot.checklist.model.TaxonStatus;
import org.tdwg.voc.TaxonStatusTerm;

/**
 *
 * @author ben
 *
 */
public class TaxonStatusConverter implements CustomConverter {

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
        if (source instanceof TaxonStatus) {
            TaxonStatus status = (TaxonStatus) source;
            switch (status) {
            case A:
                return TaxonStatusTerm.ACCEPTED;
            case C:
                return TaxonStatusTerm.ILLEGITIMATE;
            case D:
                // housekeeping terms are not returned
                return null;
            case I:
                // housekeeping terms are not returned
                return null;
            case M:
                return TaxonStatusTerm.MISSAPPLIED;
            case O:
                return TaxonStatusTerm.ORTHOGRAPHIC_VARIANT;
            case S:
                return TaxonStatusTerm.SYNONYM;
            case U:
                return TaxonStatusTerm.UNPLACED;
            case V:
                return TaxonStatusTerm.ORTHOGRAPHIC_VARIANT;
            default:
                return null;
            }
        } else {
            throw new MappingException(
                    "Converter TaxonStatusConverter used incorrectly."
                    + " Arguments passed in were:"
                            + destination + " and " + source);
        }
    }
}
