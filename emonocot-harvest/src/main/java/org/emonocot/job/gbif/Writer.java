/*
 * This is eMonocot, a global online biodiversity information resource.
 *
 * Copyright © 2011–2015 The Board of Trustees of the Royal Botanic Gardens, Kew and The University of Oxford
 *
 * eMonocot is free software: you can redistribute it and/or modify it under the terms of the
 * GNU Affero General Public License as published by the Free Software Foundation, either version 3
 * of the License, or (at your option) any later version.
 *
 * eMonocot is distributed in the hope that it will be useful, but WITHOUT ANY WARRANTY; without even
 * the implied warranty of MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 * GNU Affero General Public License for more details.
 *
 * The complete text of the GNU Affero General Public License is in the source repository as the file
 * ‘COPYING’.  It is also available from <http://www.gnu.org/licenses/>.
 */
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
