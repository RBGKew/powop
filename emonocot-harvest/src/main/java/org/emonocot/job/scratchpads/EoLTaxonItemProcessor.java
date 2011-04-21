package org.emonocot.job.scratchpads;


import org.emonocot.job.scratchpads.model.EoLTaxonItem;
import org.emonocot.model.taxon.Taxon;
import org.springframework.batch.item.ItemProcessor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.convert.converter.Converter;

public class EoLTaxonItemProcessor implements ItemProcessor<EoLTaxonItem,Taxon>{	
	
	private Converter<EoLTaxonItem, Taxon> taxonItemConverter;	
	
	@Autowired
	public void setTaxonItemConverter(Converter<EoLTaxonItem, Taxon> taxonItemConverter) {
		this.taxonItemConverter = taxonItemConverter;
	}

	public Taxon process(EoLTaxonItem input) throws Exception {
		return taxonItemConverter.convert(input);
	}
}
