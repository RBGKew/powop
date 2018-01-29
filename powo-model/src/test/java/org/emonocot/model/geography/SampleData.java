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
package org.emonocot.model.geography;

import java.util.Comparator;

public class SampleData {
	public String id;
	public String code;
	public String name;
	public String shape;

	public SampleData(String line) {
		String[] vals = line.split("\t");
		id = vals[0];
		code = vals[1];
		name = vals[2];
		shape = vals[3];
	}

	public static Comparator<SampleData> NAME_ORDER = new Comparator<SampleData>() {
		@Override
		public int compare(SampleData o1, SampleData o2) {
			return o1.name.compareTo(o2.name);
		}
	};

}
