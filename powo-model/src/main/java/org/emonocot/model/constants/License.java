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
package org.emonocot.model.constants;

/**
 *
 * @author ben
 *
 */
public enum License {
	/**
	 *
	 */
	PUBLIC_DOMAIN("http://creativecommons.org/licenses/publicdomain/"),
	/**
	 *
	 */
	ATTRIBUTION("http://creativecommons.org/licenses/by/3.0/"),
	/**
	 *
	 */
	ATTRIBUTION_NONCOMMERCIAL("http://creativecommons.org/licenses/by-nc/3.0/"),
	/**
	 *
	 */
	ATTRIBUTION_SHAREALIKE("http://creativecommons.org/licenses/by-sa/3.0/"),
	/**
	 *
	 */
	ATTRIBUTION_NONCOMMERCIAL_SHAREALIKE(
			"http://creativecommons.org/licenses/by-nc-sa/3.0/");

	/**
	 *
	 */
	private String uri;

	/**
	 *
	 * @param newUri Get the uri of this license;
	 */
	private License(final String newUri) {
		this.uri = newUri;
	}

	/**
	 *
	 * @param string The string being converted into a license
	 * @return A license instance
	 */
	public static License fromString(final String string) {
		for (License l : License.values()) {
			if (l.uri.equals(string)) {
				return l;
			}
		}
		throw new IllegalArgumentException(string
				+ " is not an acceptable value for License");
	}
}
