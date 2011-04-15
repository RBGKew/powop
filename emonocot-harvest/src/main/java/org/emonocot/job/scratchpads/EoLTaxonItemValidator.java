package org.emonocot.job.scratchpads;

import org.emonocot.job.scratchpads.model.EoLTaxonItem;
import org.emonocot.service.TaxonService;
import org.springframework.batch.item.ItemProcessor;
import org.springframework.beans.factory.annotation.Autowired;

public class EoLTaxonItemValidator implements ItemProcessor<EoLTaxonItem,EoLTaxonItem> {
	
	private String authority;
	
	private TaxonService taxonService;
	
	@Autowired
	public void setTaxonService(TaxonService taxonService) {
		this.taxonService = taxonService;
	}

	public EoLTaxonItem process(EoLTaxonItem item) throws Exception {
		if(taxonService.verify(item.getIdentifer(),item.getScientificName())) {
			return item;
		} else {
			return null;
		}
	}
	
	public void setAuthority(String authority) {
		this.authority = authority;
	}

}
