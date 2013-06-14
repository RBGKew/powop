package org.emonocot.model.convert;

import org.gbif.ecat.voc.TaxonomicStatus;
import org.springframework.core.convert.converter.Converter;

public class TaxonomicStatusToStringConverter implements Converter<TaxonomicStatus, String> {

	@Override
	public String convert(TaxonomicStatus value) {
		if(value == null) {
			return null;
		} else {
		    switch(value) {
		    case Accepted:
		    	return "accepted";
		    case DeterminationSynonym:
		    	return "determinationSynonym";
		    case Doubtful:
		    	return "doubtful";
		    case Heterotypic_Synonym:
		    	return "heterotypicSynonym";
		    case Homotypic_Synonym:
		    	return "homotypicSynonym";
		    case IntermediateRankSynonym:
		    	return "intermediateRankSynonym";
		    case Misapplied:
		    	return "misapplied";
		    case Proparte_Synonym:
		    	return "proParteSynonym";
		    case Synonym:
		    default:
		    	return "synonym";
		    }
		}
	}

}
