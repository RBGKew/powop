package org.emonocot.job.dwc.write;

import org.emonocot.model.Taxon;
import org.springframework.batch.item.ItemProcessor;

public class NullLicenseTaxonFilter implements ItemProcessor<Taxon, Taxon> {

	@Override
	public Taxon process(Taxon item) throws Exception {
		if(item.getLicense() != null && !item.getLicense().isEmpty()) {
			return item;
		} else {
		    return null;
		}
	}

}
