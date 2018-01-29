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
package org.powo.model.convert;

import org.gbif.ecat.voc.Rank;
import org.springframework.core.convert.converter.Converter;

public class RankConverter implements Converter<String, Rank> {

	@Override
	public Rank convert(String source) {
		if (source == null || source.isEmpty()) {
			return null;
		}
		source = source.toLowerCase().trim();
		for (Rank term : Rank.values()) {
			if (term.toString().toLowerCase().equals(source)) {
				return term;
			}
		}

		return null;
	}
}
