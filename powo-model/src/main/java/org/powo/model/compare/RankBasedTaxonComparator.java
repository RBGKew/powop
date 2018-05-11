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
package org.powo.model.compare;

import java.util.Comparator;

import org.powo.model.Taxon;

/**
 *
 * @author ben
 *
 */
public class RankBasedTaxonComparator implements Comparator<Taxon> {

	/**
	 * @param o1
	 *            the first taxon
	 * @param o2
	 *            the second taxon
	 * @return -1 if o1 comes before o2, 1 if o1 comes after o2 and 0 if the two
	 *         regions are equal
	 */
	public final int compare(final Taxon o1, final Taxon o2) {
		int o = 0;
		/**
		 * Nulls last
		 */
		if (o1.getTaxonRank() == null) {
			if (o1.getTaxonRank() != o2.getTaxonRank()) {
				return 1;
			} else {
				return 0;
			}
		} else if (o2.getTaxonRank() == null) {
			return -1;
		} else {
			o = o1.getTaxonRank().compareTo(o2.getTaxonRank());
		}

		/**
		 * Homonyms
		 */
		if (o == 0) {
			if (o1.getScientificName() == null) {
				if (o1.getScientificName() != o2.getScientificName()) {
					return 1;
				} else {
					return 0;
				}
			} else if (o2.getScientificName() == null) {
				return -1;
			} else {
				return o1.getScientificName().compareTo(o2.getScientificName());
			}
		} else {
			return o;
		}
	}

}
