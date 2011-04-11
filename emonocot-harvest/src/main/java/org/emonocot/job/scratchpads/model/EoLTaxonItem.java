package org.emonocot.job.scratchpads.model;

import java.util.ArrayList;
import java.util.List;

public class EoLTaxonItem {

	// maps to dc:identifier
	private String identifier;
	
	// maps to dataObject instances
	private List<DataObject> dataObjects = new ArrayList<DataObject>();

	public String getIdentifer() {
		return identifier;
	}

	public List<DataObject> getDataObjects() {
		return dataObjects;
	}

	public boolean containsImage(String url) {
		// TODO Auto-generated method stub
		return false;
	}

}
