package org.emonocot.model.convert;

import org.emonocot.model.constants.Location;
import org.springframework.core.convert.converter.Converter;

/**
 *
 * @author ben
 *
 */
public class StringToLocationConverter implements
        Converter<String, Location> {
    
    private static final String DWC_VOCABULARY_PREFIX = "TDWG:";


    /**
     * @param identifier set the identifier
     * @return a geographical region
     */
    public final Location convert(final String identifier) {
        if (identifier == null || identifier.isEmpty()) {
            return null;
        }
        if(identifier.startsWith(StringToLocationConverter.DWC_VOCABULARY_PREFIX)) {
        	String code = identifier.substring(StringToLocationConverter.DWC_VOCABULARY_PREFIX.length());
        	return Location.fromString(code);
        } else {
        	return Location.fromString(identifier);
        }        
    }

}
