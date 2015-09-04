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

import static org.junit.Assert.fail;

import org.emonocot.model.Taxon;
import org.junit.Before;
import org.junit.Test;

/**
 *
 * @author ben
 *
 */
public class AlphabeticalTaxonComparatorTest {
	/**
	 *
	 */
	private AlphabeticalTaxonComparator comparator = new AlphabeticalTaxonComparator();

	private Taxon t1 = null;

	private Taxon t2 = null;

	/**
	 *
	 */
	@Before
	public final void setUp() {
		t1 = new Taxon();
		t2 = new Taxon();
		t2.setScientificName("Poa annua");
	}

	/**
	 * Tests the case where Taxon.name == null (which did result in a
	 * NullPointerException).
	 * http://build.e-monocot.org/bugzilla/show_bug.cgi?id=125 Null Pointer
	 * Exception in AlphabeticTaxonComparator java.lang.NullPointerException:
	 * java.lang.String.compareTo(String.java:1168)
	 * org.emonocot.model.taxon.AlphabeticalTaxonComparator
	 * .compare(AlphabeticalTaxonComparator.java:19)
	 * org.emonocot.model.taxon.AlphabeticalTaxonComparator
	 * .compare(AlphabeticalTaxonComparator.java:10)
	 *
	 * Also bug http://build.e-monocot.org/bugzilla/show_bug.cgi?id=149
	 */
	@Test
	public final void testNullTaxa() {
		try {
			comparator.compare(t1, t2);
		} catch (Exception e) {
			fail("No exception expected here");
		}
	}
}
