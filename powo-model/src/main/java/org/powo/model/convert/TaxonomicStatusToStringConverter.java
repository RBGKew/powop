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

import org.gbif.ecat.voc.TaxonomicStatus;
import org.springframework.core.convert.converter.Converter;

public class TaxonomicStatusToStringConverter implements Converter<TaxonomicStatus, String> {

	@Override
	public String convert(TaxonomicStatus value) {
		if(value == null) {
			return null;
		} else {
			switch(value) {
			case Accepted:
				return "accepted";
			case DeterminationSynonym:
				return "determinationSynonym";
			case Doubtful:
				return "doubtful";
			case Heterotypic_Synonym:
				return "heterotypicSynonym";
			case Homotypic_Synonym:
				return "homotypicSynonym";
			case IntermediateRankSynonym:
				return "intermediateRankSynonym";
			case Misapplied:
				return "misapplied";
			case Proparte_Synonym:
				return "proParteSynonym";
			case Synonym:
			default:
				return "synonym";
			}
		}
	}

}
