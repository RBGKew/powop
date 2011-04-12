package org.emonocot.job.scratchpads.model;

import java.util.ArrayList;
import java.util.List;

import com.thoughtworks.xstream.annotations.XStreamAlias;
import com.thoughtworks.xstream.annotations.XStreamImplicit;

@XStreamAlias("taxon")
public class EoLTaxonItem {

	// maps to dc:identifier
	private String identifier;
	
	// maps to dc:source
	private String source;
	
	@XStreamAlias("ScientificName")
	// maps to dwc:ScientificName
	private String scientificName;
	
	@XStreamImplicit(itemFieldName = "dataObject")
	// maps to dataObject instances
	private List<EoLDataObject> dataObjects = new ArrayList<EoLDataObject>();
	
	@XStreamImplicit(itemFieldName = "reference")
	// maps to reference instances
	private List<EoLReference> references = new ArrayList<EoLReference>();

	public String getIdentifer() {
		return identifier;
	}

	public List<EoLDataObject> getDataObjects() {
		return dataObjects;
	}

	public boolean containsImage(String url) {
		// TODO Auto-generated method stub
		return false;
	}

	public List<EoLReference> getReferences() {
		return references;
	}

	public String getScientificName() {
		return scientificName;
	}

	public String getSource() {
		return source;
	}

	public String getIdentifier() {
		return identifier;
	}

}
