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

import org.springframework.util.comparator.NullSafeComparator;

public class Member implements Comparable<Member> {

	private static NullSafeComparator<String> nullSafeStringComparator;
	static {
		Comparator<String> stringComparator = new Comparator<String>() {

			@Override
			public int compare(String o1, String o2) {
				return o1.compareTo(o2);
			}

		};
		nullSafeStringComparator = new NullSafeComparator<>(stringComparator, false);
	}

	private int ordinal;

	private Level level;

	private String value;

	private String field;

	public Member(Level level, int ordinal, String field, String value) {
		this.level = level;
		this.ordinal = ordinal;
		this.value = value;
		this.field = field;
	}

	public int compareTo(Member o) {
		return nullSafeStringComparator.compare(this.value, o.value);
	}

	/**
	 * Get the ordinal of this member
	 */
	public int getOrdinal() {
		return ordinal;
	}

	/**
	 * Get the facet value (calculated) which is the name of this facet
	 */
	public String getValue() {
		return value;
	}

	public String getField() {
		return field;
	}
}
