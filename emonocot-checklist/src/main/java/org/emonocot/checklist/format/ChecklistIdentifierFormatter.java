package org.emonocot.checklist.format;

import java.text.ParseException;
import java.util.Locale;

import org.emonocot.checklist.model.Taxon;
import org.springframework.format.Formatter;

/**
 *
 * @author ben
 *
 */
public class ChecklistIdentifierFormatter implements Formatter<Long> {

    public final String print(final Long value, final Locale locale) {
        if (value == null) {
            return null;
        } else {
            return Taxon.IDENTIFIER_PREFIX + value;
        }
    }

    public final Long parse(
            final String string, final Locale locale) throws ParseException {
        if (string.startsWith(Taxon.IDENTIFIER_PREFIX)) {
            try {
                return Long.parseLong(string
                        .substring(Taxon.IDENTIFIER_PREFIX.length()));
            } catch (Exception e) {
                throw new ParseException(string
                        + " is not a valid identifier format",
                        Taxon.IDENTIFIER_PREFIX.length());
            }
        } else {
            throw new ParseException(string
                    + " is not a valid identifier format", 0);
        }
    }

}
