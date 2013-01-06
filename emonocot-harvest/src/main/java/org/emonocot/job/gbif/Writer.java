package org.emonocot.job.gbif;

import java.util.List;

import org.emonocot.model.TypeAndSpecimen;

import org.springframework.batch.item.ItemProcessor;
import org.springframework.batch.item.ItemWriter;

public class Writer implements ItemWriter<DataResource> {
	
	private ItemProcessor<TaxonOccurrence,TypeAndSpecimen> taxonOccurrenceProcessor;
	
	private ItemProcessor<TypeAndSpecimen,TypeAndSpecimen> typeAndSpecimenProcessor;
	
	private ItemWriter itemWriter;
	
	public void	write(List<? extends DataResource> items) {
		
	}
	
	public void setTaxonOccurrenceProcessor(ItemProcessor<TaxonOccurrence,TypeAndSpecimen> taxonOccurrenceProcessor) {
		this.taxonOccurrenceProcessor = taxonOccurrenceProcessor;
	}
	
	public void setTypeAndSpecimenProcessor(ItemProcessor<TypeAndSpecimen,TypeAndSpecimen> typeAndSpecimenProcessor) {
		this.typeAndSpecimenProcessor = typeAndSpecimenProcessor;
	}
	
	public void setItemWriter(ItemWriter itemWriter) {
		this.itemWriter = itemWriter;
	}
}
