package org.emonocot.portal.controller;

import java.io.IOException;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;
import org.apache.solr.client.solrj.SolrQuery;
import org.apache.solr.client.solrj.SolrServerException;
import org.apache.solr.client.solrj.response.QueryResponse;
import org.apache.solr.client.solrj.response.SuggesterResponse;
import org.emonocot.api.SearchableObjectService;
import org.emonocot.api.TaxonService;
import org.emonocot.model.solr.SolrFieldNameMappings;
import org.emonocot.persistence.solr.AutoCompleteBuilder;
import org.emonocot.persistence.solr.QueryBuilder;
import org.emonocot.portal.json.ResponseBuilder;
import org.emonocot.portal.json.MainSearchBuilder;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;

@Controller
@RequestMapping("/api/1/")
public class ApiController {

	
	private static Logger logger = LoggerFactory.getLogger(ApiController.class);
	@Autowired
	private SearchableObjectService searchableObjectService;
	
	@Autowired
	private TaxonService taxonService;

	@RequestMapping(value = "/search", method = RequestMethod.GET, produces={"application/json"})
	public ResponseEntity<MainSearchBuilder> search(@RequestParam Map<String,String> allRequestParams) throws SolrServerException, IOException {
		QueryBuilder queryBuilder = new QueryBuilder();
		if(allRequestParams != null && !allRequestParams.isEmpty()){
			for(String key : allRequestParams.keySet()){
				if(key.equals("highlight")){
					String[] values =  allRequestParams.get(key).split(":");
					queryBuilder.setHighlightQuery(values[0], values[1]);
				}else{
					queryBuilder.addParam(key, allRequestParams.get(key));
				}
			}
		}
		SolrQuery query = queryBuilder.build();
		
		QueryResponse queryResponse = searchableObjectService.search(query);
		MainSearchBuilder jsonBuilder = new ResponseBuilder().buildJsonResponse(queryResponse, taxonService);	
		return new ResponseEntity<MainSearchBuilder>(jsonBuilder, HttpStatus.OK);
	}
	
	
	
	@RequestMapping(value = "/suggest", method = RequestMethod.GET, produces={"application/json"})
	public ResponseEntity<SuggesterResponse> suggest(
			@RequestParam(value = "query", required = true) String queryString,
			@RequestParam(value = "suggester", required = true) List<String> suggesters,
			@RequestParam(value = "page_size", required = false, defaultValue = "5") Integer pageSize
			) throws SolrServerException, IOException {
		AutoCompleteBuilder autoCompleteBuilder = new AutoCompleteBuilder();
		autoCompleteBuilder.pageSize(pageSize);
		autoCompleteBuilder.setQuery(queryString);
		for(String suggester : suggesters){
			autoCompleteBuilder.addSuggester(suggester);
		}
		
		SuggesterResponse response = searchableObjectService.autocomplete(autoCompleteBuilder.build());
		return new ResponseEntity<SuggesterResponse>(response, HttpStatus.OK);	
	}
}
