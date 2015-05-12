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

import org.emonocot.model.Description;
import org.emonocot.model.Reference;

public class ReferenceBasedDescriptionComparator implements	Comparator<Description> {
	
	Comparator<Reference> referenceComparator = new ReferenceComparator();

	@Override
	public int compare(Description o1, Description o2) {
		if(o1.getReferences().isEmpty() || o2.getReferences().isEmpty()) {
			return 0;
		} else {
			return referenceComparator.compare(o1.getReferences().iterator().next(), o2.getReferences().iterator().next());			
		}
	}

}
