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

import org.gbif.ecat.voc.NomenclaturalStatus;
import org.springframework.core.convert.converter.Converter;

public class NomenclaturalStatusToStringConverter implements
Converter<NomenclaturalStatus, String> {

	@Override
	public String convert(NomenclaturalStatus value) {
		if (value == null) {
			return null;
		} else {
			switch (value) {
			case Nomen_Abortivum:
				return "abortivum";
			case Alternative:
				return "alternativum";
			case Ambiguous:
				return "ambigua";
			case Available:
				return "available";
			case New_Combination:
				return "combinatio";
			case Confused:
				return "confusum";
			case Conserved:
				return "conservandum";
			case Conserved_Proposed:
				return "conservandumProp";
			case Doubtful:
				return "dubimum";
			case Illegitimate:
				return "illegitimum";
			case Invalid:
				return "invalidum";
			case Legitimate:
				return "legitimate";
			case Denied:
				return "negatum";
			case Novum:
				return "novum";
			case Nudum:
				return "nudum";
			case Forgotten:
				return "oblitum";
			case Opus_Utique_Oppressa:
				return "oppressa";
			case Orthographic_Variant:
				return "orthographia";
			case Protected:
				return "protectum";
			case Provisional:
				return "provisorium";
			case Rejected:
				return "rejiciendum";
			case Rejected_Proposed:
				return "rejiciendumProp";
			case Utique_Rejected:
				return "rejiciendumUtique";
			case Utique_Rejected_Proposed:
				return "rejiciendumUtiqueProp";
			case Subnudum:
				return "subnudum";
			case Superfluous:
				return "superfluum";
			case Valid:
				return "valid";
			default:
				return value.toString();
			}
		}
	}
}