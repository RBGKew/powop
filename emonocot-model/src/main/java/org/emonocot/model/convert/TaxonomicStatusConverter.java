package org.emonocot.model.convert;

import org.gbif.ecat.voc.TaxonomicStatus;
import org.springframework.core.convert.converter.Converter;

public class TaxonomicStatusConverter implements Converter<String, TaxonomicStatus> {

	@Override
	public TaxonomicStatus convert(String source) {
		if(source == null || source.isEmpty()) {
			return null;
		} else {
			return TaxonomicStatus.fromString(source);
		}
	}

}
