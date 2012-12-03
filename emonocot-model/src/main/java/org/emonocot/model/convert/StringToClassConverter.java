package org.emonocot.model.convert;

import org.springframework.core.convert.converter.Converter;

/**
 *
 * @author ben
 *
 */
public class StringToClassConverter implements Converter<String, Class> {

    /**
     * @param clazz Set the string value
     * @return a class
     */
    public final Class convert(final String clazz) {
        if (clazz == null) {
            return null;
        } else {
            try {

                return Class.forName(clazz);
            } catch (ClassNotFoundException e) {
                throw new IllegalArgumentException(e);
            }
        }
    }
}
