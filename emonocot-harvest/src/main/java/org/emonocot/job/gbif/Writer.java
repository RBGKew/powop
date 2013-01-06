package org.emonocot.job.gbif;

import java.util.ArrayList;
import java.util.List;

import org.emonocot.model.TypeAndSpecimen;

import org.springframework.batch.item.ItemProcessor;
import org.springframework.batch.item.ItemWriter;

public class Writer implements ItemWriter<DataResource> {
	
	private ItemProcessor<TaxonOccurrence,TypeAndSpecimen> taxonOccurrenceProcessor;
	
	private ItemProcessor<TypeAndSpecimen,TypeAndSpecimen> typeAndSpecimenProcessor;
	
	private ItemWriter itemWriter;
	
	public void	write(List<? extends DataResource> items) throws Exception {
		List<TypeAndSpecimen> typesAndSpecimens = new ArrayList<TypeAndSpecimen>();
		for(DataResource dataResource : items) {
			for(TaxonOccurrence taxonOccurrence : dataResource.getOccurrenceRecords()) {
				taxonOccurrence.setRights(dataResource.getRights());
				if(dataResource.getCitation()!= null && dataResource.getCitation().length() > 255) {
				    taxonOccurrence.setBibliographicCitation(dataResource.getCitation().substring(0, 254));
				} else {
					taxonOccurrence.setBibliographicCitation(dataResource.getCitation());
				}
				TypeAndSpecimen typeAndSpecimen = taxonOccurrenceProcessor.process(taxonOccurrence);
				if(typeAndSpecimen != null) {
					typeAndSpecimen = typeAndSpecimenProcessor.process(typeAndSpecimen);
					if(typeAndSpecimen != null) {
						typesAndSpecimens.add(typeAndSpecimen);
					}
				}
			}
		}
		
		itemWriter.write(typesAndSpecimens);		
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
