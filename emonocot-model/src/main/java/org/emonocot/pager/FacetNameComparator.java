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
package org.emonocot.pager;

import java.util.Comparator;


/**
 *
 * @author ben
 *
 */
public class FacetNameComparator implements Comparator<String> {

	/**
	 * @param o1
	 *            the first string
	 * @param o2
	 *            the second string
	 * @return -1 if o1 comes before o2, 1 if o1 comes after o2 and 0 if o1 and
	 *         o2 are equal
	 */
	public final int compare(final String o1, final String o2) {
		FacetName fn1 = FacetName.fromString(o1);
		FacetName fn2 = FacetName.fromString(o2);
		return fn1.compareTo(fn2);
	}

}
