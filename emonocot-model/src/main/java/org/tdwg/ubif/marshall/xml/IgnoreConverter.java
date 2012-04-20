package org.tdwg.ubif.marshall.xml;

import com.thoughtworks.xstream.converters.SingleValueConverter;

/**
 *
 * @author ben
 *
 */
public class IgnoreConverter implements SingleValueConverter {

    /**
     * @param clazz the class to convert
     * @return true if the class can be converted
     */
    public final boolean canConvert(final Class clazz) {
        if (clazz != null && clazz.equals(Ignore.class)) {
            return true;
        }
        return false;
    }

    /**
     * @param string the string to be deserialized
     * @return the deserialized object
     */
    public final Object fromString(final String string) {
        return null;
    }

    /**
     * @param object the object to serialize
     * @return the serialized object
     */
    public final String toString(final Object object) {
        return null;
    }

}
