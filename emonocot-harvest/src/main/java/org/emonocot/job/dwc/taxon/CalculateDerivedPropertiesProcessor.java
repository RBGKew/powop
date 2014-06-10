package org.emonocot.job.dwc.taxon;

import org.emonocot.api.TaxonService;
import org.emonocot.model.Taxon;
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
		
		if(taxon.getTaxonRank() != null) {
		    switch (taxon.getTaxonRank()) {
            case FAMILY:
                if(taxon.getFamily() == null) {
                    taxon.setFamily(taxon.getScientificName());
                }
                break;
            case ORDER:
                if(taxon.getOrder() == null) {
                    taxon.setOrder(taxon.getScientificName());
                }
                break;
            case Subfamily:
                if(taxon.getSubfamily() == null) {
                    taxon.setSubfamily(taxon.getScientificName());
                }
                break;
            case SUBGENUS:
                if(taxon.getSubgenus() == null) {
                    taxon.setSubgenus(taxon.getScientificName());
                }
                break;
            case Subtribe:
                if(taxon.getSubtribe() == null) {
                    taxon.setSubtribe(taxon.getScientificName());
                }
                break;
            case Tribe:
                if(taxon.getTribe() == null) {
                    taxon.setTribe(taxon.getScientificName());
                }
                break;
            default:
                break;
            }
		}
		for(Taxon higher : taxon.getHigherClassification()) {
		    if(higher.getTaxonRank() != null) {
	            switch (higher.getTaxonRank()) {
	            case FAMILY:
    	            if(taxon.getFamily() == null) {
    	                taxon.setFamily(higher.getScientificName());
    	            }
    	            break;
	            case ORDER:
                    if(taxon.getOrder() == null) {
                        taxon.setOrder(higher.getScientificName());
                    }
                    break;
	            case Subfamily:
                    if(taxon.getSubfamily() == null) {
                        taxon.setSubfamily(higher.getScientificName());
                    }
                    break;
	            case Subtribe:
                    if(taxon.getSubtribe() == null) {
                        taxon.setSubtribe(higher.getScientificName());
                    }
                    break;
	            case Tribe:
                    if(taxon.getTribe() == null) {
                        taxon.setTribe(higher.getScientificName());
                    }
                    break;
	            default:
	                break;
	            }
		    }
		}
		return taxon;
	}

}
