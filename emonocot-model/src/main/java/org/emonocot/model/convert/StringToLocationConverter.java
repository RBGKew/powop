/*
 * This is eMonocot, a global online biodiversity information resource.
 *
 * Copyright © 2011–2015 The Board of Trustees of the Royal Botanic Gardens, Kew and The University of Oxford
 *
 * eMonocot is free software: you can redistribute it and/or modify it under the terms of the
 * GNU Affero General Public License as published by the Free Software Foundation, either version 3
 * of the License, or (at your option) any later version.
 *
 * eMonocot is distributed in the hope that it will be useful, but WITHOUT ANY WARRANTY; without even
 * the implied warranty of MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 * GNU Affero General Public License for more details.
 *
 * The complete text of the GNU Affero General Public License is in the source repository as the file
 * ‘COPYING’.  It is also available from <http://www.gnu.org/licenses/>.
 */
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
