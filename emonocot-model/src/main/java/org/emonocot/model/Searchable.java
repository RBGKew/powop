package org.emonocot.model;

import org.apache.solr.common.SolrInputDocument;
import org.emonocot.model.geography.GeographicalRegionFactory;

public interface Searchable extends SecuredObject {
	
	public String getDocumentId();
	
	public SolrInputDocument toSolrInputDocument(GeographicalRegionFactory geographicalRegionFactory);
}
