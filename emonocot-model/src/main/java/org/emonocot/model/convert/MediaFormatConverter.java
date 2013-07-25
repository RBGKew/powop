package org.emonocot.model.convert;

import org.emonocot.model.constants.MediaFormat;
import org.springframework.core.convert.converter.Converter;

/**
 *
 * @author ben
 *
 */
public class MediaFormatConverter implements
        Converter<String, MediaFormat> {

    /**
     * @param value the string to convert
     * @return an ImageFormat object or null if the image format term is null
     */
    public final MediaFormat convert(final String value) {
        if (value == null || value.trim().isEmpty()) {
            return null;
        } else {
            if (value.equals("image/jpeg")) {
                return MediaFormat.jpg;
            } else if (value.equals("image/png")) {
                return MediaFormat.png;
            } else if (value.equals("image/gif")) {
                return MediaFormat.png;
            } else if (value.equals("application/xml")) {
            	return MediaFormat.xml;
            } else if (value.equals("text/plain")) {
            	return MediaFormat.txt;
            } else {
                return MediaFormat.valueOf(value);
            }
        }
    }

}
