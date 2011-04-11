package org.emonocot.job.scratchpads;

import org.emonocot.job.scratchpads.model.EoLTaxonItem;
import org.springframework.batch.item.ItemProcessor;

public class EoLTaxonItemValidator implements ItemProcessor<EoLTaxonItem,EoLTaxonItem> {
	
	private String authority;

	public EoLTaxonItem process(EoLTaxonItem item) throws Exception {
		// TODO Auto-generated method stub
		return item;
	}
	
	public void setAuthority(String authority) {
		this.authority = authority;
	}

}
