package org.emonocot.model.convert;

import org.gbif.ecat.voc.NomenclaturalStatus;
import org.springframework.core.convert.converter.Converter;

public class NomenclaturalStatusConverter implements Converter<String, NomenclaturalStatus> {

	@Override
	public NomenclaturalStatus convert(String source) {
		if(source == null || source.trim().isEmpty()) {
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
