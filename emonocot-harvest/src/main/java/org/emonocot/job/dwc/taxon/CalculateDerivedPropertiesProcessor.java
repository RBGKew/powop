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
