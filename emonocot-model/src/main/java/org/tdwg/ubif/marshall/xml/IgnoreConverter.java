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
