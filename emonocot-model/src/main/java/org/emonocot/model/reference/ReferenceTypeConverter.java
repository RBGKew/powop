package org.emonocot.model.reference;

import java.text.ParseException;
import java.util.Locale;

import org.springframework.core.convert.converter.Converter;
import org.springframework.format.Parser;

/**
 *
 * @author ben
 *
 */
public class ReferenceTypeConverter implements
        Converter<String, ReferenceType>, Parser<ReferenceType> {

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
            if (identifier.equals("Article")) {
                return ReferenceType.JournalArticle;
            } else if (identifier.equals("Chapter")) {
                return ReferenceType.BookSection;
            } else if (identifier.equals("PersonalCommunication")) {
                return ReferenceType.Communication;
            } else if (identifier.equals("Webpage")) {
                return ReferenceType.WebPage;
            }
            return ReferenceType.valueOf(identifier);
        }
    }

    /**
     * @param value Set the string to parse
     * @param locale Set the locale
     * @return the ReferenceType
     * @throws ParseException if there is a problem parsing the string
     */
    public final ReferenceType parse(final String value, final Locale locale)
            throws ParseException {
        try {
            return convert(value);
        } catch (IllegalArgumentException iae) {
            throw new ParseException(value, 0);
        }
    }
}
