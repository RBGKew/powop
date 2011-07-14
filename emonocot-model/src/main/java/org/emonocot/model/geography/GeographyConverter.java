package org.emonocot.model.geography;

import org.springframework.core.convert.converter.Converter;

/**
 *
 * @author ben
 *
 */
public class GeographyConverter implements
        Converter<String, GeographicalRegion> {

    /**
     *
     */
    private static final String TDWG_VOCABULARY_PREFIX
        = "http://rs.tdwg.org/ontology/voc/GeographicRegion.rdf#";


    public final GeographicalRegion convert(final String identifier) {
        if (identifier.startsWith(GeographyConverter.TDWG_VOCABULARY_PREFIX)) {
            String code = identifier
                    .substring(GeographyConverter.TDWG_VOCABULARY_PREFIX
                            .length());
            if (code.length() == 1) {
                return Continent.fromString(code);
            } else if (code.length() == 2) {
                return Region.fromString(code);
            } else if (code.length() == 3) {
                return Country.fromString(code);
            }
        }
        throw new IllegalArgumentException(identifier
                + " is not a valid TDWG region");
    }

}
