package org.emonocot.model.reference;

import org.springframework.core.convert.converter.Converter;

/**
 * 
 * @author ben
 * 
 */
public class ReferenceTypeConverter implements Converter<String, ReferenceType> {

    /**
    *
    */
    private static final String TDWG_VOCABULARY_PREFIX = "http://rs.tdwg.org/ontology/voc/PublicationCitation#";

    /**
     * @param identifier
     *            set the identifier
     * @return a geographical region
     */
    public final ReferenceType convert(final String identifier) {
        if (identifier == null) {
            return null;
        }
        if (identifier
                .startsWith(ReferenceTypeConverter.TDWG_VOCABULARY_PREFIX)) {
            String code = identifier
                    .substring(ReferenceTypeConverter.TDWG_VOCABULARY_PREFIX
                            .length());
            return ReferenceType.valueOf(code);
        } else {
            throw new IllegalArgumentException(identifier
                    + " is not a valid TDWG publication type");
        }
    }
}
