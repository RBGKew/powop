package org.tdwg.ubif.marshall.xml;

import org.joda.time.DateTime;
import org.joda.time.format.DateTimeFormat;
import org.joda.time.format.DateTimeFormatter;

import com.thoughtworks.xstream.converters.SingleValueConverter;

/**
 *
 * @author ben
 *
 */
public class DateTimeConverter implements SingleValueConverter {

    /**
     *
     */
    private DateTimeFormatter dateTimeFormatter = DateTimeFormat
            .forPattern("yyyy-MM-dd'T'HH':'mm':'ss");

    /**
     * @param clazz the class to convert
     * @return true if the class can be converted
     */
    public final boolean canConvert(final Class clazz) {
        if (clazz != null && clazz.equals(DateTime.class)) {
            return true;
        }
        return false;
    }

    /**
     * @param string the string to be deserialized
     * @return the deserialized object
     */
    public final Object fromString(final String string) {
        return dateTimeFormatter.parseDateTime(string);
    }

    /**
     * @param object the object to serialize
     * @return the serialized object
     */
    public final String toString(final Object object) {
        if (object == null) {
            return null;
        } else {
            DateTime dateTime = (DateTime) object;
            return dateTimeFormatter.print(dateTime);
        }
    }

}
