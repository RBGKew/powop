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
package org.emonocot.model.compare;

import java.util.Comparator;

import org.emonocot.model.constants.Location;

/**
 *
 * @author ben
 *
 */
public class LocationComparator implements
        Comparator<Location> {

    /**
     * @param o1 Set the first region
     * @param o2 Set the second region
     * @return -1 if o1 comes before o2, 1 if o1 comes after o2 and 0 if the two
     *         regions are equal
     */
    public int compare(Location o1, Location o2) {    	
    	if(o1.isChildOf(o2)) { 
    		// o1 is child of o2, returning MAX_VALUE
    		return Integer.MAX_VALUE;
    	} else if(o2.isChildOf(o1)) {
    		// o2 is child of o1, returning MIN_VALUE
    		return Integer.MIN_VALUE;
    	} else if(o1.getLevel() < o2.getLevel()) {
    		// o1 higher level than o2, comparing o1 to o2.parent
    		return compare(o1,o2.getParent());
    	} else if(o1.getLevel() > o2.getLevel()) {
    		// o1 lower level than o2, comparing o1.parent to o2
    		return compare(o1.getParent(),o2);
    	} else if(o1.isSiblingOf(o2)) {
    		// o1 and o2 are siblings, comparing directly
    		return o1.compareNames(o2);
    	} else {
    		// o1 and o2 are of the same level, but are not siblings, comparing o1.parent to o2.parent
    		return compare(o1.getParent(), o2.getParent());
    	}
    }

}
