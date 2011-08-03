package org.emonocot.checklist.view.assembler;

import java.net.URI;
import java.net.URISyntaxException;

import org.dozer.CustomConverter;
import org.dozer.MappingException;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 *
 * @author ben
 *
 */
public class UriConverter implements CustomConverter {
    /**
     *
     */
    private static Logger logger = LoggerFactory.getLogger(UriConverter.class);
    /**
     *
     * @param destination the existing destination field value
     * @param source the source field value
     * @param destClass the destination class
     * @param sourceClass the source class
     * @return a URI or null if the date has not been set
     */
    public final Object convert(final Object destination, final Object source,
            final Class destClass, final Class sourceClass) {
        if (source == null) {
            return null;
        }
        if (source instanceof String) {
            try {
                return new URI(((String) source));
            } catch (URISyntaxException urise) {
                logger.error(urise.getMessage());
                return null;
            }
        } else {
            throw new MappingException("Converter UriConverter used"
                    + " incorrectly. Arguments passed in were:"
                    + destination + " and " + source);
        }
    }
}
