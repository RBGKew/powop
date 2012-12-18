package org.emonocot.model.convert;

import org.gbif.ecat.voc.TaxonomicStatus;
import org.springframework.core.convert.converter.Converter;

public class TaxonomicStatusConverter implements Converter<String, TaxonomicStatus> {

	@Override
	public TaxonomicStatus convert(String source) {
		if(source == null) {
			return null;
		} else if(source.startsWith("http://e-monocot.org/TaxonomicStatus#")) {
			// TODO retire once OAI-PMH is no longer used
			switch(source) {
			case "http://e-monocot.org/TaxonomicStatus#accepted":
				return TaxonomicStatus.Accepted;
			case "http://e-monocot.org/TaxonomicStatus#illegitimate":
				return null;
			case "http://e-monocot.org/TaxonomicStatus#missapplied":
				return TaxonomicStatus.Misapplied;
			case "http://e-monocot.org/TaxonomicStatus#orthographic":
				return null;
			case "http://e-monocot.org/TaxonomicStatus#synonym":
				return TaxonomicStatus.Synonym;
			case "http://e-monocot.org/TaxonomicStatus#unplaced":
				return TaxonomicStatus.Doubtful;
			case "http://e-monocot.org/TaxonomicStatus#invalid":
			    return null;
			default:
				return null;			
			}
		} else {
			return TaxonomicStatus.fromString(source);
		}
	}

}
