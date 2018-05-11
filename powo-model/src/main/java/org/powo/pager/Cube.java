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
package org.powo.pager;

import java.util.HashMap;
import java.util.Map;
import java.util.Set;
import java.util.SortedSet;
import java.util.TreeSet;

public class Cube {

	SortedSet<Dimension> dimensions = new TreeSet<Dimension>();

	Map<String,String> selectedFacets = new HashMap<String,String>();

	private String defaultLevel;

	public Cube(Map<String,String> selectedFacets) {
		this.selectedFacets = selectedFacets;
	}

	public Level getLevel(String field) {
		for(Dimension d : dimensions) {
			if(d.containsLevel(field)) {
				return d.getLevel(field);
			}
		}
		return null;
	}

	public void addDimension(Dimension dimension) {
		dimensions.add(dimension);
	}

	public Set<Dimension> getDimensions() {
		return dimensions;
	}

	public String getDefaultLevel() {
		return defaultLevel;
	}

	public void setDefaultLevel(String defaultLevel) {
		this.defaultLevel = defaultLevel;
	}
}
