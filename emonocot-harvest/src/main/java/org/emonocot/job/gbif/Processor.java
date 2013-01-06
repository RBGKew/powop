package org.emonocot.job.gbif;

import org.emonocot.model.TypeAndSpecimen;

import org.springframework.batch.item.ItemProcessor;

public class Processor implements ItemProcessor<TaxonOccurrence,TypeAndSpecimen> {
	
	public TypeAndSpecimen process(TaxonOccurrence o) {
		return null;
	}
}
