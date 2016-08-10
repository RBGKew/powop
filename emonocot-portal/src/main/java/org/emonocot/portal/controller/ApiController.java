package org.emonocot.portal.controller;

import java.io.IOException;
import java.util.Map;
import java.util.Map.Entry;
import org.apache.solr.client.solrj.SolrQuery;
import org.apache.solr.client.solrj.SolrServerException;
import org.apache.solr.client.solrj.response.QueryResponse;
import org.emonocot.api.SearchableObjectService;
import org.emonocot.api.TaxonService;
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
			for(Entry<String, String> requestParam : allRequestParams.entrySet()){
				queryBuilder.addParam(requestParam.getKey(), requestParam.getValue());
			}
		}
		SolrQuery query = queryBuilder.build();
		QueryResponse queryResponse = searchableObjectService.search(query);
		if(queryResponse == null){
			return new ResponseEntity<MainSearchBuilder>(HttpStatus.INTERNAL_SERVER_ERROR);
		}
		MainSearchBuilder jsonBuilder = new ResponseBuilder().buildJsonResponse(queryResponse, taxonService);
		jsonBuilder.sort(query.get("sort"));
		jsonBuilder.per_page(query.getRows());

		if(jsonBuilder.getTotalResults() != null && jsonBuilder.getPerPage() != null ){
			jsonBuilder.page(jsonBuilder.getTotalResults() / jsonBuilder.getPerPage());
		}	
		return new ResponseEntity<MainSearchBuilder>(jsonBuilder, HttpStatus.OK);
	}
}