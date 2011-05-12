package org.openarchives.pmh.format;

import java.text.ParseException;
import java.util.Locale;

import org.openarchives.pmh.MetadataPrefix;
import org.springframework.format.Formatter;

/**
 *
 * @author ben
 *
 */
public class MetadataPrefixFormatter implements Formatter<MetadataPrefix> {

    @Override
    public final String print(final MetadataPrefix metadataPrefix,
            final Locale locale) {
        if (metadataPrefix == null) {
            return null;
        } else {
            return metadataPrefix.name();
        }
    }

    @Override
    public final MetadataPrefix parse(final String text, final Locale locale)
            throws ParseException {
       return MetadataPrefix.fromValue(text);
    }
}
