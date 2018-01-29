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
package org.emonocot.common;

import org.jsoup.Jsoup;
import org.jsoup.safety.Whitelist;
import com.google.common.base.Strings;

public class HtmlSanitizer {

	private static final Whitelist html = Whitelist.basic()
			.addAttributes("a", "title")
			.removeTags("span");

	public static String sanitize(String unclean) {
		if (Strings.isNullOrEmpty(unclean)) {
			return unclean;
		} else {
			unclean = unclean.replace("\0", "");
			return Jsoup.clean(unclean, html);
		}
	}

	public static String strip(String unclean) {
		if (Strings.isNullOrEmpty(unclean)) {
			return unclean;
		} else {
			return Jsoup.clean(unclean, Whitelist.none());
		}
	}
}
