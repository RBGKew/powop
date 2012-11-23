package org.emonocot.model;

import org.apache.solr.common.SolrInputDocument;

public interface Searchable extends SecuredObject {
	
	public String getDocumentId();
	
	public SolrInputDocument toSolrInputDocument();
}
