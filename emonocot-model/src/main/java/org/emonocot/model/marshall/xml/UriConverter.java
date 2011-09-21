package org.emonocot.model.marshall.xml;

import java.net.URI;
import java.net.URISyntaxException;

import com.thoughtworks.xstream.converters.SingleValueConverter;

/**
 *
 * @author ben
 *
 */
public class UriConverter implements SingleValueConverter {

    public final boolean canConvert(final Class clazz) {
        if (clazz != null && clazz.equals(URI.class)) {
            return true;
        }
        return false;
    }

    public final Object fromString(final String string) {
        try {
            return new URI(string);
        } catch (URISyntaxException e) {
            throw new IllegalArgumentException(string + " is not a valid uri");
        }
    }

    public final String toString(final Object object) {
        return ((URI) object).toString();
    }

}
