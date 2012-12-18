package org.emonocot.model.convert;

import org.gbif.ecat.voc.OccurrenceStatus;
import org.springframework.core.convert.converter.Converter;

public class OccurrenceStatusConverter implements
		Converter<String, OccurrenceStatus> {

	@Override
	public OccurrenceStatus convert(String source) {
		if(source == null || source.isEmpty()) {
			return null;
		} 
		switch(source) {
		case "present":
			return OccurrenceStatus.Present;
		case "common":
			return OccurrenceStatus.Common;
		case "rare":
			return OccurrenceStatus.Rare;
		case "irregular":
			return OccurrenceStatus.Irregular;
		case "doubtful":
			return OccurrenceStatus.Doubtful;
		case "absent":
			return OccurrenceStatus.Absent;
		case "excluded":
			return OccurrenceStatus.Excluded;
		default:
			return OccurrenceStatus.valueOf(source);
		}
	}

}
