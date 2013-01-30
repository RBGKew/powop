package org.emonocot.harvest.common;

import java.util.HashMap;
import java.util.Map;

import org.apache.solr.common.SolrDocument;
import org.emonocot.api.SearchableObjectService;
import org.springframework.batch.item.database.AbstractPagingItemReader;
import org.springframework.beans.factory.annotation.Autowired;

public class SolrDocumentItemReader extends AbstractPagingItemReader<SolrDocument> {
	
	private SearchableObjectService searchableObjectService;
	
    private String queryString = null;
	
	private Map<String,String> selectedFacets = new HashMap<String,String>();
	
	private String sort = null;
	
	public void setSort(String sort) {
		this.sort = sort;
	}
	
	public void setQueryString(String queryString) {
		this.queryString = queryString;
	}
	
	public void setSelectedFacets(String[] selectedFacets) {
		if(selectedFacets != null) {
		    for(String selectedFacet : selectedFacets) {
		        String[] f = selectedFacet.split("\\=");
		        this.selectedFacets.put(f[0],f[1]);
		    }
		}
	}
	
    @Autowired
	public void setSearchableObjectService(SearchableObjectService searchableObjectService) {
		this.searchableObjectService = searchableObjectService;
	}

	@Override
	protected void doReadPage() {
		results = searchableObjectService.searchForDocuments(queryString, getPageSize(), getPage(), selectedFacets, sort).getRecords();
 
		logger.debug("Search for " + queryString + " (page number " + getPage() + " got a page of " + results.size()
					+ " (results");
	}

	@Override
	protected void doJumpToPage(int itemIndex) {
	}

}
