package org.emonocot.model.description;

import org.apache.commons.lang.ObjectUtils;
import org.emonocot.model.common.Base;
import org.emonocot.model.taxon.Taxon;

public class Content extends Base {
	
	private Taxon taxon;
	
	private Feature feature;
	
	public void setTaxon(Taxon taxon) {
		this.taxon = taxon;
	}
	
	public Feature getFeature() {
		return feature;
	}
	
	public void setFeature(Feature feature) {
		this.feature = feature;
	}
	
	public Taxon getTaxon() {
		return taxon;
	}
	
	@Override
	public boolean equals(Object other) {
		if(!super.equals(other)) {
			return false;
		}

		Content content = (Content)other;
	    return ObjectUtils.equals(this.feature,content.feature)
	        && ObjectUtils.equals(this.taxon, content.taxon);
	}

}
