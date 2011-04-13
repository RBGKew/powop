package org.emonocot.job.scratchpads;

import org.emonocot.job.scratchpads.model.EoLTaxonItem;
import org.emonocot.model.taxon.Taxon;
import org.springframework.batch.item.ItemProcessor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.convert.ConversionService;

public class EoLTaxonItemProcessor implements ItemProcessor<EoLTaxonItem,Taxon>{	
	
	private ConversionService conversionService;	
	
	@Autowired
	public void setConversionService(ConversionService conversionService) {
		this.conversionService = conversionService;
	}

	public Taxon process(EoLTaxonItem input) throws Exception {
		return conversionService.convert(input, Taxon.class);
	}
}
