package org.emonocot.model;

import java.util.Set;

public interface NonOwned {
	
	public Set<Taxon> getTaxa();
	
	public void setTaxa(Set<Taxon> taxa);

}
