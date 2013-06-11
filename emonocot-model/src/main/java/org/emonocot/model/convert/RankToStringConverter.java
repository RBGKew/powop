package org.emonocot.model.convert;

import org.gbif.ecat.voc.Rank;
import org.springframework.core.convert.converter.Converter;

public class RankToStringConverter implements Converter<Rank, String> {

	@Override
	public String convert(Rank value) {
		if (value == null) {
            return null;
        } else {
		    return value.name().toLowerCase();
        }
	}
}
