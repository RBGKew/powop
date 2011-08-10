package org.emonocot.model.taxon;

import org.springframework.core.convert.converter.Converter;

/**
 *
 * @author ben
 *
 */
public class RankConverter implements Converter<String, Rank> {

    /**
     * @param identifier the taxon rank identifier to convert
     * @return a Rank object or null if the taxon rank term is null
     */
    public final Rank convert(final String identifier) {
        if (identifier == null) {
            return null;
        } else {
            return Rank.valueOf(identifier.toUpperCase());
        }
    }

}
