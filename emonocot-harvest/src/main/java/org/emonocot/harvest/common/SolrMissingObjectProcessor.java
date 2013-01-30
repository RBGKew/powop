package org.emonocot.harvest.common;

import org.apache.solr.common.SolrDocument;
import org.emonocot.api.SearchableObjectService;
import org.springframework.batch.item.ItemProcessor;
import org.springframework.beans.factory.annotation.Autowired;

public class SolrMissingObjectProcessor implements ItemProcessor<SolrDocument, String> {

	private SearchableObjectService searchableObjectService;
	
	@Autowired
	public void setSearchableObjectService(SearchableObjectService searchableObjectService) {
		this.searchableObjectService = searchableObjectService;
	}
	
	@Override
	public String process(SolrDocument item) throws Exception {		
		Object o = searchableObjectService.loadObjectForDocument(item);
		if(o == null) {
		    return (String)item.get("id");
		} else {
		    return null;
		}
	}

}
