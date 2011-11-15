package org.emonocot.model.taxon;

import org.springframework.core.convert.converter.Converter;

/**
 *
 * @author ben
 *
 */
public class TaxonomicStatusConverter implements
        Converter<String, TaxonomicStatus> {

    /**
     * @param identifier
     *            the taxon rank identifier to convert
     * @return a Rank object or null if the taxon rank term is null
     */
    public final TaxonomicStatus convert(final String identifier) {
        if (identifier == null) {
            return null;
        } else {
            if (identifier
                    .equals("http://e-monocot.org/TaxonomicStatus#accepted")) {
                return TaxonomicStatus.accepted;
            } else if (identifier
                    .equals("http://e-monocot.org/TaxonomicStatus#illegitimate")) {
                return TaxonomicStatus.illegitimate;
            } else if (identifier
                    .equals("http://e-monocot.org/TaxonomicStatus#orthographic")) {
                return TaxonomicStatus.illegitimate;
            } else if (identifier
                    .equals("http://e-monocot.org/TaxonomicStatus#synonym")) {
                return TaxonomicStatus.synonym;
            } else if (identifier
                    .equals("http://e-monocot.org/TaxonomicStatus#unplaced")) {
                return TaxonomicStatus.unplaced;
            } else if (identifier
                    .equals("http://e-monocot.org/TaxonomicStatus#invalid")) {
                return TaxonomicStatus.invalid;
            }
        }
        throw new IllegalArgumentException(identifier
                + " cannot be converted into a Taxonomic Status");
    }

}
