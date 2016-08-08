/*
 * This is eMonocot, a global online biodiversity information resource.
 *
 * Copyright © 2011–2015 The Board of Trustees of the Royal Botanic Gardens, Kew and The University of Oxford
 *
 * eMonocot is free software: you can redistribute it and/or modify it under the terms of the
 * GNU Affero General Public License as published by the Free Software Foundation, either version 3
 * of the License, or (at your option) any later version.
 *
 * eMonocot is distributed in the hope that it will be useful, but WITHOUT ANY WARRANTY; without even
 * the implied warranty of MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 * GNU Affero General Public License for more details.
 *
 * The complete text of the GNU Affero General Public License is in the source repository as the file
 * ‘COPYING’.  It is also available from <http://www.gnu.org/licenses/>.
 */
package org.emonocot.portal.controller;

import java.io.IOException;
import java.text.ParseException;
import java.util.HashMap;
import java.util.Map;
import javax.servlet.http.HttpServletRequest;

import org.apache.solr.client.solrj.SolrQuery;
import org.apache.solr.client.solrj.SolrServerException;
import org.emonocot.api.SearchableObjectService;
import org.emonocot.api.TaxonService;
import org.emonocot.model.SearchableObject;
import org.emonocot.model.Taxon;
import org.emonocot.pager.Page;
import org.emonocot.portal.legacy.OldSearchBuilder;
import org.emonocot.portal.logging.LoggingConstants;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.slf4j.MDC;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.dao.DataRetrievalFailureException;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.servlet.ModelAndView;

@Controller
@RequestMapping("/endpoint")
public class ChecklistWebserviceController {

	private static String CHECKLIST_WEBSERVICE_SEARCH_TYPE = "te";

	private static Logger logger = LoggerFactory.getLogger(ChecklistWebserviceController.class);

	/**
	 * Querylog for logging requests for reporting etc.
	 */
	private static Logger queryLog = LoggerFactory.getLogger("query");

	private TaxonService taxonService;

	private SearchableObjectService searchableObjectService;

	@Autowired
	public void setTaxonDao(TaxonService taxonService) {
		this.taxonService = taxonService;
	}

	@Autowired
	public void setSearchableObjectService(SearchableObjectService searchableObjectService) {
		this.searchableObjectService = searchableObjectService;
	}

	/**
	 * Simple method that allows the client to determine whether the service is
	 * up and running.
	 *
	 * @return An empty ModelAndView with the view name "rdfResponse".
	 */
	@RequestMapping(method = RequestMethod.GET, params = { "!function" })
	public ModelAndView ping() {
		logger.debug("ping");
		return new ModelAndView("rdfResponse");
	}

	/**
	 * Method which searches for taxon objects who's names match the search term
	 * exactly.
	 *
	 * @param search
	 *            A taxon name to search the database for.
	 * @return a list of taxon objects (stored under the key 'result')
	 * @throws SolrServerException
	 */
	@RequestMapping(method = RequestMethod.GET, params = { "function=search",
	"search" })
	public ModelAndView search(
			@RequestParam(value = "search", required = true) String search) throws SolrServerException, IOException {
		logger.debug("search for " + search);
		String query = new String("searchable.label_sort:" + search);
		Map<String,String> selectedFacets = new HashMap<String,String>();
		selectedFacets.put("base.class_s","org.emonocot.model.Taxon");
		ModelAndView modelAndView = new ModelAndView("rdfResponse");
		SolrQuery solrQuery = new OldSearchBuilder().oldSearchBuilder
		(query, null, null, null, null, null, selectedFacets, null, null);
		Page<SearchableObject> taxa = searchableObjectService.search(solrQuery, null);
		modelAndView.addObject("result", taxa.getRecords());
		try {
			MDC.put(LoggingConstants.SEARCH_TYPE_KEY, CHECKLIST_WEBSERVICE_SEARCH_TYPE);
			MDC.put(LoggingConstants.QUERY_KEY, query);
			MDC.put(LoggingConstants.RESULT_COUNT_KEY, Integer.toString(taxa.getSize()));
			
			queryLog.info("ChecklistWebserviceController.get");
		} finally {
			MDC.remove(LoggingConstants.SEARCH_TYPE_KEY);
			MDC.remove(LoggingConstants.QUERY_KEY);
			MDC.remove(LoggingConstants.RESULT_COUNT_KEY);
		}
		return modelAndView;
	}

	/**
	 * Method which returns a single taxon object with the specified identifier.
	 *
	 * @param id
	 *            The identifier of the taxon to be retrieved.
	 * @return a taxon objects (stored under the key 'result')
	 */
	@RequestMapping(method = RequestMethod.GET, params = {
			"function=details_tcs", "id" })
	public ModelAndView get(@RequestParam(value = "id") String id, HttpServletRequest request) {
		StringBuffer requestUrl = request.getRequestURL();
		logger.debug(" RequestUrl for checklistwebservice is " + requestUrl);
		ModelAndView modelAndView = new ModelAndView();
		modelAndView.setViewName("tcsXmlResponse");
		Taxon taxon = taxonService.load(id,"taxon-ws");
		modelAndView.addObject("requestInfo", requestUrl);
		modelAndView.addObject("result", taxon);
		
		try {
			MDC.put(LoggingConstants.SEARCH_TYPE_KEY, CHECKLIST_WEBSERVICE_SEARCH_TYPE);
			MDC.put(LoggingConstants.QUERY_KEY, id.toString());
			MDC.put(LoggingConstants.RESULT_COUNT_KEY, "1");
			queryLog.info("ChecklistWebserviceController.get");
		} finally {
			MDC.remove(LoggingConstants.SEARCH_TYPE_KEY);
			MDC.remove(LoggingConstants.QUERY_KEY);
			MDC.remove(LoggingConstants.RESULT_COUNT_KEY);
		}

		return modelAndView;
	}

	/**
	 * TaxonDao will throw a (wrapped) UnresolvableObjectException if the client
	 * provides an invalid identifier. This method returns a HTTP 400 BAD
	 * REQUEST status code and a short message
	 *
	 * @param exception
	 *            The exception just thrown (because of an invalid id)
	 * @return A model and view with the name exception and the exception stored
	 *         in the model under the key 'exception'
	 */
	@ExceptionHandler({ DataRetrievalFailureException.class,
		ParseException.class })
	@ResponseStatus(HttpStatus.BAD_REQUEST)
	public ModelAndView handleInvalidTaxonIdentifier(
			DataRetrievalFailureException exception) {
		logger.debug("exception");
		ModelAndView modelAndView = new ModelAndView("exception");
		modelAndView.addObject("exception", exception);
		return modelAndView;
	}
}
