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
package org.emonocot.model.convert;

import org.gbif.ecat.voc.NomenclaturalStatus;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.core.convert.converter.Converter;

public class NomenclaturalStatusConverter implements Converter<String, NomenclaturalStatus> {

	Logger logger = LoggerFactory.getLogger(NomenclaturalStatusConverter.class);

	@Override
	public NomenclaturalStatus convert(String source) {
		logger.info("Converting " + source);
		if(source == null || source.trim().isEmpty()) {
			logger.info("Returning null");
			return null;
		} else {
			switch(source.toLowerCase()) {
			case "abortivum":
				return NomenclaturalStatus.Nomen_Abortivum;
			case "alternativum":
				return NomenclaturalStatus.Alternative;
			case "ambigua":
				return NomenclaturalStatus.Ambiguous;
			case "available":
				return NomenclaturalStatus.Available;
			case "combinatio":
				return NomenclaturalStatus.New_Combination;
			case "confusum":
				return NomenclaturalStatus.Confused;
			case "conservandum":
				return NomenclaturalStatus.Conserved;
			case "conservandumProp":
				return NomenclaturalStatus.Conserved_Proposed;
			case "correctum":
				return null;
			case "dubimum":
				return NomenclaturalStatus.Doubtful;
			case "illegitimum":
				return NomenclaturalStatus.Illegitimate;
			case "invalidum":
				return NomenclaturalStatus.Invalid;
			case "legitimate":
				return NomenclaturalStatus.Legitimate;
			case "negatum":
				return NomenclaturalStatus.Denied;
			case "novum":
				return NomenclaturalStatus.Novum;
			case "nudum":
				return NomenclaturalStatus.Nudum;
			case "nullum":
				return null;
			case "oblitum":
				return NomenclaturalStatus.Forgotten;
			case "oppressa":
				return NomenclaturalStatus.Opus_Utique_Oppressa;
			case "orthographia":
				return NomenclaturalStatus.Orthographic_Variant;
			case "protectum":
				return NomenclaturalStatus.Protected;
			case "provisorium":
				return NomenclaturalStatus.Provisional;
			case "rejiciendum":
				return NomenclaturalStatus.Rejected;
			case "rejiciendumProp":
				return NomenclaturalStatus.Rejected_Proposed;
			case "rejiciendumUtique":
				return NomenclaturalStatus.Utique_Rejected;
			case "rejiciendumUtiqueProp":
				return NomenclaturalStatus.Utique_Rejected_Proposed;
			case "subnudum":
				return NomenclaturalStatus.Subnudum;
			case "superfluum":
				return NomenclaturalStatus.Superfluous;
			case "valid":
				return NomenclaturalStatus.Valid;
			default:
				return NomenclaturalStatus.valueOf(source);
			}
		}
	}

}
