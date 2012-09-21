package org.emonocot.job.dwc.taxon;

import org.emonocot.api.TaxonService;
import org.emonocot.model.taxon.Rank;
import org.emonocot.model.taxon.Taxon;
import org.springframework.batch.item.ItemProcessor;

public class CalculateDerivedPropertiesProcessor implements
		ItemProcessor<String, Taxon> {
	
	private TaxonService taxonService;
	
	public void setTaxonService(TaxonService taxonService) {
		this.taxonService = taxonService;
	}

	@Override
	public Taxon process(String item) throws Exception {
		Taxon taxon = taxonService.load(item, "taxon-page");
		
		if(taxon.getRank() == null) {
			return null;
		}
		
		if(taxon.getRank().ordinal() >= Rank.ORDER.ordinal()) {
			
		} else if(taxon.getParent() != null) {
			if(taxon.getParent().getRank().equals(Rank.ORDER)) {
				taxon.setOrder(taxon.getParent().getName());
			} else {
				taxon.setOrder(taxon.getParent().getOrder());
			}	
		}
		
        if(taxon.getRank().ordinal() >= Rank.FAMILY.ordinal()) {
			
		} else if(taxon.getParent() != null) {
			if(taxon.getParent().getRank().equals(Rank.FAMILY)) {
				taxon.setFamily(taxon.getParent().getName());
			} else {
				taxon.setFamily(taxon.getParent().getFamily());
			}	
		}
        
        if(taxon.getRank().ordinal() >= Rank.SUBFAMILY.ordinal()) {
			
		} else if(taxon.getParent() != null) {
			if(taxon.getParent().getRank().equals(Rank.SUBFAMILY)) {
				taxon.setSubfamily(taxon.getParent().getName());
			} else {
				taxon.setSubfamily(taxon.getParent().getSubfamily());
			}	
		}
        
        if(taxon.getRank().ordinal() >= Rank.TRIBE.ordinal()) {
			
		} else if(taxon.getParent() != null) {
			if(taxon.getParent().getRank().equals(Rank.TRIBE)) {
				taxon.setTribe(taxon.getParent().getName());
			} else {
				taxon.setTribe(taxon.getParent().getTribe());
			}	
		}
        
        if(taxon.getRank().ordinal() >= Rank.SUBTRIBE.ordinal()) {
			
		} else if(taxon.getParent() != null) {
			if(taxon.getParent().getRank().equals(Rank.SUBTRIBE)) {
				taxon.setSubtribe(taxon.getParent().getName());
			} else {
				taxon.setSubtribe(taxon.getParent().getSubtribe());
			}	
		}
		return taxon;
	}

}
