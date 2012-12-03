package org.emonocot.model.convert;

import org.springframework.core.convert.converter.Converter;

/**
 *
 * @author ben
 *
 */
public class ClassToStringConverter implements Converter<Class, String> {

    /**
     * @param clazz the class to convert
     * @return a string
     */
    public final String convert(final Class clazz) {
        if (clazz == null) {
            return null;
        } else {
            return clazz.getCanonicalName();
        }
    }

}
