package org.emonocot.portal.controller;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;

import org.apache.solr.client.solrj.SolrQuery;
import org.apache.solr.client.solrj.SolrServerException;
import org.apache.solr.client.solrj.response.QueryResponse;
import org.apache.solr.client.solrj.response.SolrResponseBase;
import org.apache.solr.client.solrj.response.SuggesterResponse;
import org.apache.solr.common.params.ModifiableSolrParams;
import org.apache.solr.common.util.NamedList;
import org.emonocot.api.SearchableObjectService;
import org.emonocot.api.TaxonService;
import org.emonocot.model.Taxon;
import org.emonocot.persistence.solr.AutoCompleteBuilder;
import org.emonocot.persistence.solr.QueryBuilder;
import org.emonocot.portal.json.ResponseBuilder;
import org.emonocot.portal.json.TaxonResponse;
import org.emonocot.portal.json.MainSearchBuilder;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.PathVariable;
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
				queryBuilder.addParam(key, allRequestParams.get(key));
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
			@RequestParam(value = "page_size", required = false, defaultValue = "5") Integer pageSize,
			@RequestParam(value = "context", required = false) String context
			) throws SolrServerException, IOException {

		SolrQuery query = new AutoCompleteBuilder()
				.setWorkingSuggesters(checkSuggesters(suggesters))
				.setSuggesters(suggesters)
				.pageSize(pageSize)
				.setQuery(queryString)
				.setContext(context)
				.build();

		if(query != null) {
			SuggesterResponse response = searchableObjectService.autocomplete(query);
			return new ResponseEntity<SuggesterResponse>(response, HttpStatus.OK);
		}

		return null;
	}

	/* Check which suggesters are currently built and working in solr. 
	 * Returns a list of working ones to query. This is needed because
	 * Solr will fail the whole request if you ask for a list of suggesters
	 * and one throws an exception.
	 */
	@SuppressWarnings("unchecked")
	private List<String> checkSuggesters(List<String> suggesters){
		List<String> workingSuggesters = new ArrayList<String>();
		ModifiableSolrParams params = new ModifiableSolrParams()
				.add("key", "suggest")
				.add("stats", "true");
		SolrQuery query = new SolrQuery().setRequestHandler("/admin/mbeans");
		query.add(params);
		SolrResponseBase response = searchableObjectService.search(query);
		NamedList<Object> responseStats = (NamedList<Object>) response.getResponse().findRecursive("solr-mbeans","OTHER","suggest","stats");
		for(Entry<String, Object> suggester : responseStats){
			if(!suggester.getKey().equals("totalSizeInBytes") 
					&& !(((String) suggester.getValue()).contains("sizeInBytes=0"))) {
				workingSuggesters.add(suggester.getKey());
			}
		}

		return workingSuggesters;
	}

	@RequestMapping(value = "/taxon/{identifier}", method = RequestMethod.GET, produces = {"application/json"})
	public ResponseEntity<TaxonResponse> taxon(@PathVariable String identifier) {
		Taxon taxon = taxonService.find(identifier);
		return new ResponseEntity<TaxonResponse>(new TaxonResponse(taxon), HttpStatus.OK);
	}
}
