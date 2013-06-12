package org.emonocot.model.convert;

import org.gbif.ecat.voc.NomenclaturalCode;
import org.springframework.core.convert.converter.Converter;

public class NomenclaturalCodeToStringConverter implements
		Converter<NomenclaturalCode, String> {

	@Override
	public String convert(NomenclaturalCode value) {
		if(value == null) {
			return null;
		} else {
			return value.acronym;
		}
	}

}
