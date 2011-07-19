package org.emonocot.checklist.view.oai.assembler;

import org.dozer.CustomConverter;
import org.dozer.MappingException;
import org.emonocot.checklist.model.Rank;
import org.tdwg.voc.TaxonRankTerm;

/**
 *
 * @author ben
 *
 */
public class RankConverter implements CustomConverter {

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
        if (source instanceof Rank) {
            Rank rank = (Rank) source;
            switch (rank) {
            case FAMILY:
                return TaxonRankTerm.FAMILY;
            case GENUS:
                return TaxonRankTerm.GENUS;
            case INFRASPECIFIC:
                return TaxonRankTerm.INFRASPECIES;
            case SPECIES:
                return TaxonRankTerm.SPECIES;
            case SUBSPECIES:
                return TaxonRankTerm.SUBSPECIES;
            case VARIETY:
                return TaxonRankTerm.VARIETY;           
            default:
                return null;
            }
        } else {
            throw new MappingException(
                    "Converter RankConverter used incorrectly."
                    + " Arguments passed in were:"
                            + destination + " and " + source);
        }
    }
}
