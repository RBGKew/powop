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

    /**
     * @param clazz the class to convert
     * @return true if the class can be converted
     */
    public final boolean canConvert(final Class clazz) {
        if (clazz != null && clazz.equals(URI.class)) {
            return true;
        }
        return false;
    }

    /**
     * @param string the string to be deserialized
     * @return the deserialized object
     */
    public final Object fromString(final String string) {
        try {
            return new URI(string);
        } catch (URISyntaxException e) {
            throw new IllegalArgumentException(string + " is not a valid uri");
        }
    }

    /**
     * @param object the object to serialize
     * @return the serialized object
     */
    public final String toString(final Object object) {
        return ((URI) object).toString();
    }

}
