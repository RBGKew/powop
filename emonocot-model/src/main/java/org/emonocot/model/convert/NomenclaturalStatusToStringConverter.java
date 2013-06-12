package org.emonocot.model.convert;

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