package org.emonocot.job.dwc.taxon;

import org.emonocot.api.TaxonService;
import org.emonocot.model.Taxon;
import org.gbif.ecat.voc.Rank;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.batch.item.ItemProcessor;


public class CalculateDerivedPropertiesProcessor implements
		ItemProcessor<Long, Taxon> {
	
	private static Logger logger = LoggerFactory.getLogger(CalculateDerivedPropertiesProcessor.class);
	
	private TaxonService taxonService;
	
	public void setTaxonService(TaxonService taxonService) {
		this.taxonService = taxonService;
	}

	@Override
	public Taxon process(Long id) throws Exception {
		Taxon taxon = taxonService.load(id, "taxon-page");
		
		if(taxon.getTaxonRank() == null) {
			return null;
		}
		
		if(taxon.getTaxonRank().ordinal() <= Rank.ORDER.ordinal()) {
			
		} else if(taxon.getParentNameUsage() != null && taxon.getParentNameUsage().getTaxonRank() != null) {
			if(taxon.getParentNameUsage().getTaxonRank().equals(Rank.ORDER)) {
				taxon.setOrder(taxon.getParentNameUsage().getScientificName());
			} else {
				taxon.setOrder(taxon.getParentNameUsage().getOrder());
			}	
		}
		
        if(taxon.getTaxonRank().ordinal() <= Rank.FAMILY.ordinal()) {
			
		} else if(taxon.getParentNameUsage() != null && taxon.getParentNameUsage().getTaxonRank() != null) {
			if(taxon.getParentNameUsage().getTaxonRank().equals(Rank.FAMILY)) {
				taxon.setFamily(taxon.getParentNameUsage().getScientificName());
			} else {
				taxon.setFamily(taxon.getParentNameUsage().getFamily());
			}	
		}
        
        if(taxon.getTaxonRank().ordinal() <= Rank.Subfamily.ordinal()) {
			
		} else if(taxon.getParentNameUsage() != null && taxon.getParentNameUsage().getTaxonRank() != null) {
			if(taxon.getParentNameUsage().getTaxonRank().equals(Rank.Subfamily)) {
				taxon.setSubfamily(taxon.getParentNameUsage().getScientificName());
			} else {
				taxon.setSubfamily(taxon.getParentNameUsage().getSubfamily());
			}	
		}
        
        if(taxon.getTaxonRank().ordinal() <= Rank.Tribe.ordinal()) {
			
		} else if(taxon.getParentNameUsage() != null && taxon.getParentNameUsage().getTaxonRank() != null) {
			if(taxon.getParentNameUsage().getTaxonRank().equals(Rank.Tribe)) {
				taxon.setTribe(taxon.getParentNameUsage().getScientificName());
			} else {
				taxon.setTribe(taxon.getParentNameUsage().getTribe());
			}	
		}
        
        if(taxon.getTaxonRank().ordinal() <= Rank.Subtribe.ordinal()) {
			
		} else if(taxon.getParentNameUsage() != null && taxon.getParentNameUsage().getTaxonRank() != null) {
			if(taxon.getParentNameUsage().getTaxonRank().equals(Rank.Subtribe)) {
				taxon.setSubtribe(taxon.getParentNameUsage().getScientificName());
			} else {
				taxon.setSubtribe(taxon.getParentNameUsage().getSubtribe());
			}	
		}
		return taxon;
	}

}
