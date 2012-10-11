package org.emonocot.model.convert;

import org.gbif.ecat.voc.Rank;
import org.springframework.core.convert.converter.Converter;

public class RankConverter implements Converter<String, Rank> {

	@Override
	public Rank convert(String source) {
		if (source == null) {
			return null;
		}
		source = source.toLowerCase().trim();
		if (source.isEmpty()) {
			return null;
		}
		for (Rank term : Rank.values()) {
			if (term.toString().toLowerCase().equals(source)) {
				return term;
			}
		}

		return null;
	}
}
