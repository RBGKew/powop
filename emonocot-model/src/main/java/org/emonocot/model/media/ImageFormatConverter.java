package org.emonocot.model.media;

import org.springframework.core.convert.converter.Converter;

/**
 *
 * @author ben
 *
 */
public class ImageFormatConverter implements
        Converter<String, ImageFormat> {

    /**
     * @param value the string to convert
     * @return an ImageFormat object or null if the image format term is null
     */
    public final ImageFormat convert(final String value) {
        if (value == null || value.trim().isEmpty()) {
            return null;
        } else {
            if (value.equals("image/jpeg")) {
                return ImageFormat.jpg;
            } else if (value.equals("image/png")) {
                return ImageFormat.png;
            } else if (value.equals("image/gif")) {
                return ImageFormat.png;
            } else {
                return ImageFormat.valueOf(value);
            }
        }
    }

}
