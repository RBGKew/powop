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

import java.rmi.RemoteException;
import java.text.DecimalFormat;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;

import org.apache.commons.lang3.StringUtils;
import org.apache.solr.client.solrj.SolrServerException;
import org.emonocot.api.CommentService;
import org.emonocot.api.OrganisationService;
import org.emonocot.api.ResourceService;
import org.emonocot.api.SearchableObjectService;
import org.emonocot.api.TypeAndSpecimenService;
import org.emonocot.api.UserService;
import org.emonocot.api.autocomplete.Match;
import org.emonocot.model.SearchableObject;
import org.emonocot.model.TypeAndSpecimen;
import org.emonocot.pager.CellSet;
import org.emonocot.pager.Cube;
import org.emonocot.pager.Dimension;
import org.emonocot.pager.FacetName;
import org.emonocot.pager.Page;
import org.emonocot.portal.controller.form.NcbiDto;
import org.emonocot.portal.format.annotation.FacetRequestFormat;
import org.emonocot.portal.view.geojson.Feature;
import org.emonocot.portal.view.geojson.FeatureCollection;
import org.emonocot.portal.ws.ncbi.NcbiService;
import org.restdoc.api.GlobalHeader;
import org.restdoc.api.MethodDefinition;
import org.restdoc.api.ParamValidation;
import org.restdoc.api.ResponseDefinition;
import org.restdoc.api.RestDoc;
import org.restdoc.api.RestResource;
import org.restdoc.api.Schema;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.servlet.ModelAndView;

import com.fasterxml.jackson.databind.JsonMappingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.util.JSONPObject;
import com.fasterxml.jackson.module.jsonSchema.factories.SchemaFactoryWrapper;

/**
 *
 * @author ben
 *
 */
@Controller
public class SearchController {

	private static Logger queryLog = LoggerFactory.getLogger("query");

	private static Logger logger = LoggerFactory
			.getLogger(SearchController.class);

	private SearchableObjectService searchableObjectService;

	private CommentService commentService;

	private OrganisationService organisationService;

	private ResourceService resourceService;

	private TypeAndSpecimenService typeAndSpecimenService;

	private UserService userService;

	private NcbiService ncbiService;

	private ObjectMapper objectMapper;

	@Autowired
	public void setSearchableObjectService(SearchableObjectService searchableObjectService) {
		this.searchableObjectService = searchableObjectService;
	}

	@Autowired
	public void setCommentService(CommentService commentService) {
		this.commentService = commentService;
	}

	@Autowired
	public void setOrganisationService(OrganisationService organisationService) {
		this.organisationService = organisationService;
	}

	@Autowired
	public void setResourceService(ResourceService resourceService) {
		this.resourceService = resourceService;
	}

	@Autowired
	public void setUserService(UserService userService) {
		this.userService = userService;
	}

	@Autowired
	public void setObjectMapper(ObjectMapper objectMapper) {
		this.objectMapper = objectMapper;
	}

	@Autowired
	public void setTypeAndSpecimenService(TypeAndSpecimenService typeAndSpecimenService) {
		this.typeAndSpecimenService = typeAndSpecimenService;
	}

	@Autowired
	public void setNcbiService(NcbiService ncbiService) {
		this.ncbiService = ncbiService;
	}

	/**
	 * @param query
	 * @param start
	 * @param limit
	 * @param spatial
	 * @param responseFacets
	 * @param sort
	 * @param selectedFacets
	 * @return
	 */
	private Page<? extends SearchableObject> runQuery(String query,
			Integer start, Integer limit, String spatial,
			String[] responseFacets, Map<String, String> facetPrefixes,
			String sort, Map<String, String> selectedFacets) throws SolrServerException {
		Page<? extends SearchableObject> result = searchableObjectService
				.search(query, spatial, limit, start, responseFacets,
						facetPrefixes, selectedFacets, sort, "taxon-with-image");
		queryLog.info("Query: \'{}\', start: {}, limit: {},"
				+ "facet: [{}], {} results", new Object[] { query, start,
						limit, selectedFacets, result.getSize() });
		result.putParam("query", query);

		return result;
	}




	/**
	 *
	 * @param view
	 *            Set the view name
	 * @param className
	 *            Set the class name
	 * @return the default limit
	 */

	private String setView(String view, String className) {
		if (view == null || view == "") {
			return null;
		}	else if (view.equals("grid")){
			if (className == null || className == "") {
				return null;
			} else if (className.equals("org.emonocot.model.Image")){
				return "grid";
			}
			else {
				return null;
			}

		} return view;
	}




	/**
	 *
	 * @param query
	 *            Set the query
	 * @param limit
	 *            Limit the number of returned results
	 * @param start
	 *            Set the offset
	 * @param facets
	 *            The facets to set
	 * @param view
	 *            Set the view
	 * @param model
	 *            Set the model
	 *
	 * @return a model and view
	 */
	@RequestMapping(value = "/search", method = RequestMethod.GET, produces = {"text/html", "*/*"})
	public String search(
			@RequestParam(value = "query", required = false) String query,
			@RequestParam(value = "limit", required = false, defaultValue = "24") Integer limit,
			@RequestParam(value = "start", required = false, defaultValue = "0") Integer start,
			@RequestParam(value = "facet", required = false) @FacetRequestFormat List<FacetRequest> facets,
			@RequestParam(value = "sort", required = false) String sort,
			@RequestParam(value = "view", required = false) String view,
			Model model) throws SolrServerException {

		Map<String, String> selectedFacets = null;
		if (facets != null && !facets.isEmpty()) {
			selectedFacets = new HashMap<String, String>();
			for (FacetRequest facetRequest : facets) {
				selectedFacets.put(facetRequest.getFacet(),
						facetRequest.getSelected());
			}
			logger.debug(selectedFacets.size()
					+ " facets have been selected from " + facets.size()
					+ " available");
		} else {
			logger.debug("There were no facets available to select from");
		}
		if(selectedFacets != null && !selectedFacets.isEmpty()){
			for(String key : selectedFacets.keySet()){
				String escapeSpaceFacet =  selectedFacets.get(key).replace(" ","\\ ").replace("\\\\", "\\");
				selectedFacets.put(key, escapeSpaceFacet);

			}
		}
		// Decide which facets to return
		List<String> responseFacetList = new ArrayList<String>();
		Map<String, String> facetPrefixes = new HashMap<String, String>();
		responseFacetList.add("base.class_s");
		if(selectedFacets == null) {
			responseFacetList.add(FacetName.FAMILY.getSolrField());
		} else {
			int taxFacetIdx = 1; //Start from FacetName.FAMILY
			for (; taxFacetIdx < FacetName.taxonomyFacets.length; taxFacetIdx++) {
				FacetName fn = FacetName.taxonomyFacets[taxFacetIdx];
				if(!responseFacetList.contains(fn.getSolrField())){
					responseFacetList.add(fn.getSolrField());
				}
				if(!selectedFacets.containsKey(fn.getSolrField())) {
					break;
				}
			}
			for(; taxFacetIdx < FacetName.taxonomyFacets.length; ++taxFacetIdx) {
				selectedFacets.remove(FacetName.taxonomyFacets[taxFacetIdx].getSolrField());
			}
		}
		responseFacetList.add("taxon.distribution_TDWG_0_ss");
		responseFacetList.add("taxon.measurement_or_fact_threatStatus_txt");
		responseFacetList.add("taxon.measurement_or_fact_Lifeform_txt");
		responseFacetList.add("taxon.measurement_or_fact_Habitat_txt");
		responseFacetList.add("taxon.taxon_rank_s");
		responseFacetList.add("taxon.taxonomic_status_s");
		responseFacetList.add("searchable.sources_ss");
		String className = null;
		if (selectedFacets == null) {
			logger.debug("No selected facets, setting default response facets");
		} else {
			if (selectedFacets.containsKey("base.class_s")) {
				className = selectedFacets.get("base.class_s");
			}
			if (selectedFacets.containsKey("taxon.distribution_TDWG_0_ss")) {
				logger.debug("Adding region facet");
				responseFacetList.add("taxon.distribution_TDWG_1_ss");
				facetPrefixes.put("taxon.distribution_TDWG_1_ss",
						selectedFacets.get("taxon.distribution_TDWG_0_ss")
						+ "_");
			} else {
				selectedFacets.remove("taxon.distribution_TDWG_1_ss");
			}
		}
		String[] responseFacets = new String[] {};
		responseFacets = responseFacetList.toArray(responseFacets);
		view = setView(view,className);
		//limit = setLimit(view, className);


		// Run the search
		Page<? extends SearchableObject> result = runQuery(query, start, limit,
				null, responseFacets, facetPrefixes, sort, selectedFacets);

		result.putParam("view", view);
		result.setSort(sort);
		model.addAttribute("result", result);
		return "search";
	}

	@RequestMapping(value = "/search", method = RequestMethod.GET, produces = "application/json")
	public ResponseEntity<Page> searchAPI(
			@RequestParam(value = "query", required = false) String query,
			@RequestParam(value = "limit", required = false, defaultValue = "24") Integer limit,
			@RequestParam(value = "start", required = false, defaultValue = "0") Integer start,
			@RequestParam(value = "facet", required = false) @FacetRequestFormat List<FacetRequest> facets,
			@RequestParam(value = "x1", required = false) Double x1,
			@RequestParam(value = "y1", required = false) Double y1,
			@RequestParam(value = "x2", required = false) Double x2,
			@RequestParam(value = "y2", required = false) Double y2,
			@RequestParam(value = "sort", required = false) String sort,
			Model model) throws SolrServerException {

		spatial(query,x1, y1, x2, y2, null, limit,start,facets,sort,null,model);
		return new ResponseEntity<Page>((Page) model.asMap().get("result"),HttpStatus.OK);
	}

	@RequestMapping(value = "/search", method = RequestMethod.GET, produces = "application/javascript")
	public ResponseEntity<JSONPObject> searchAPIJSONP(
			@RequestParam(value = "query", required = false) String query,
			@RequestParam(value = "limit", required = false, defaultValue = "24") Integer limit,
			@RequestParam(value = "start", required = false, defaultValue = "0") Integer start,
			@RequestParam(value = "facet", required = false) @FacetRequestFormat List<FacetRequest> facets,
			@RequestParam(value = "x1", required = false) Double x1,
			@RequestParam(value = "y1", required = false) Double y1,
			@RequestParam(value = "x2", required = false) Double x2,
			@RequestParam(value = "y2", required = false) Double y2,
			@RequestParam(value = "sort", required = false) String sort,
			@RequestParam(value = "callback", required = true) String callback,
			Model model) throws SolrServerException {

		spatial(query,x1, y1, x2, y2, null, limit,start,facets,sort,null,model);
		return new ResponseEntity<JSONPObject>(new JSONPObject(callback,(Page) model.asMap().get("result")),HttpStatus.OK);
	}

	/**
	 *
	 * @param query
	 *            Set the query
	 * @param limit
	 *            Limit the number of returned results
	 * @param start
	 *            Set the offset
	 * @param facets
	 *            The facets to set
	 * @param x1
	 *            the first latitude
	 * @param x2
	 *            the second latitude
	 * @param y1
	 *            the first longitude
	 * @param y2
	 *            the second longitude
	 * @param view
	 *            Set the view
	 * @param model
	 *            Set the model
	 *
	 * @return a model and view
	 */
	@RequestMapping(value = "/spatial", method = RequestMethod.GET, produces = {"text/html", "*/*"})
	public String spatial(
			@RequestParam(value = "query", required = false) String query,
			@RequestParam(value = "x1", required = false) Double x1,
			@RequestParam(value = "y1", required = false) Double y1,
			@RequestParam(value = "x2", required = false) Double x2,
			@RequestParam(value = "y2", required = false) Double y2,
			@RequestParam(value = "featureId", required = false) String featureId,
			@RequestParam(value = "limit", required = false, defaultValue = "24") Integer limit,
			@RequestParam(value = "start", required = false, defaultValue = "0") Integer start,
			@RequestParam(value = "facet", required = false) @FacetRequestFormat List<FacetRequest> facets,
			@RequestParam(value = "sort", required = false) String sort,
			@RequestParam(value = "view", required = false) String view,
			Model model) throws SolrServerException {
		String spatial = null;
		DecimalFormat decimalFormat = new DecimalFormat("###0.0");
		if (x1 != null
				&& y1 != null
				&& x2 != null
				&& y2 != null
				&& (x1 != 0.0 && y1 != 0.0 && x2 != 0.0 && x2 != 0.0 && y2 != 0.0)) {
			spatial = "{!join to=taxon.distribution_ss from=location.tdwg_code_s}geo:\"Intersects(" + decimalFormat.format(x1) + " "
					+ decimalFormat.format(y1) + " " + decimalFormat.format(x2)
					+ " " + decimalFormat.format(y2) + ")\"";
		}

		Map<String, String> selectedFacets = null;
		if (facets != null && !facets.isEmpty()) {
			selectedFacets = new HashMap<String, String>();
			for (FacetRequest facetRequest : facets) {
				selectedFacets.put(facetRequest.getFacet(),
						facetRequest.getSelected());
			}
			logger.debug(selectedFacets.size()
					+ " facets have been selected from " + facets.size()
					+ " available");
		} else {
			logger.debug("There were no facets available to select from");
		}

		// Decide which facets to return
		List<String> responseFacetList = new ArrayList<String>();
		responseFacetList.add("base.class_s");
		if(selectedFacets == null) {
			responseFacetList.add(FacetName.FAMILY.getSolrField());
		} else {
			int taxFacetIdx = 1; //Start from FacetName.FAMILY
			for (; taxFacetIdx < FacetName.taxonomyFacets.length; taxFacetIdx++) {
				FacetName fn = FacetName.taxonomyFacets[taxFacetIdx];
				if(!responseFacetList.contains(fn.getSolrField())){
					responseFacetList.add(fn.getSolrField());
				}
				if(!selectedFacets.containsKey(fn.getSolrField())) {
					break;
				}
			}
			for(; taxFacetIdx < FacetName.taxonomyFacets.length; ++taxFacetIdx) {
				selectedFacets.remove(FacetName.taxonomyFacets[taxFacetIdx].getSolrField());
			}
		}
		responseFacetList.add("taxon.measurement_or_fact_threatStatus_txt");
		responseFacetList.add("taxon.measurement_or_fact_Lifeform_txt");
		responseFacetList.add("taxon.measurement_or_fact_Habitat_txt");
		responseFacetList.add("taxon.taxon_rank_s");
		responseFacetList.add("taxon.taxonomic_status_s");
		responseFacetList.add("searchable.sources_ss");
		String className = null;
		if (selectedFacets == null) {
			logger.debug("No selected facets, setting default response facets");
		} else {
			if (selectedFacets.containsKey("base.class_s")) {
				className = selectedFacets.get("base.class_s");
			}
			if (selectedFacets.containsKey("taxon.distribution_TDWG_0_ss")) {
				logger.debug("Removing continent facet");
				responseFacetList.remove("taxon.distribution_TDWG_0_ss");
			}
		}
		String[] responseFacets = new String[] {};
		responseFacets = responseFacetList.toArray(responseFacets);
		view = setView(view,className);
		//limit = setLimit(view, className);


		// Run the search
		Page<? extends SearchableObject> result = runQuery(query, start, limit,
				spatial, responseFacets, null, sort, selectedFacets);

		if (spatial != null) {
			result.putParam("x1", x1);
			result.putParam("y1", y1);
			result.putParam("x2", x2);
			result.putParam("y2", y2);
		}
		if (!StringUtils.isEmpty(featureId)) {
			result.putParam("featureId", featureId);
		}
		result.putParam("view", view);
		result.setSort(sort);
		model.addAttribute("result", result);
		return "spatial";
	}

	@RequestMapping(value = "/visualise", method = RequestMethod.GET, produces = "text/html")
	public String visualise(
			Model uiModel,
			@RequestParam(value = "rows", required = false) String rows,
			@RequestParam(value = "firstRow", required = false, defaultValue = "0") Integer firstRow,
			@RequestParam(value = "maxRows", required = false, defaultValue = "10") Integer maxRows,
			@RequestParam(value = "cols", required = false) String cols,
			@RequestParam(value = "firstCol", required = false, defaultValue = "0") Integer firstCol,
			@RequestParam(value = "maxCols", required = false, defaultValue = "5") Integer maxCols,
			@RequestParam(value = "facet", required = false) @FacetRequestFormat List<FacetRequest> facets,
			@RequestParam(value = "view", required = false, defaultValue = "bar") String view)
					throws Exception {

		Map<String, String> selectedFacets = null;
		if (facets != null && !facets.isEmpty()) {
			selectedFacets = new HashMap<String, String>();
			for (FacetRequest facetRequest : facets) {
				selectedFacets.put(facetRequest.getFacet(),
						facetRequest.getSelected());
			}
		}

		List<String> facetList = new ArrayList<String>();
		if (selectedFacets == null) {
			facetList.add(FacetName.FAMILY.getSolrField());
		} else {
			int taxFacetIdx = 1; // Start from FacetName.FAMILY
			for (; taxFacetIdx < FacetName.taxonomyFacets.length; taxFacetIdx++) {
				FacetName fn = FacetName.taxonomyFacets[taxFacetIdx];
				if (!facetList.contains(fn.getSolrField())) {
					facetList.add(fn.getSolrField());
				}
				if (!selectedFacets.containsKey(fn.getSolrField())) {
					break;
				}
			}
			for (; taxFacetIdx < FacetName.taxonomyFacets.length; ++taxFacetIdx) {
				selectedFacets.remove(FacetName.taxonomyFacets[taxFacetIdx]
						.getSolrField());
			}
		}
		facetList.add("taxon.distribution_TDWG_0_ss");
		facetList.add("taxon.taxon_rank_s");
		facetList.add("taxon.taxonomic_status_s");
		facetList.add("searchable.sources_ss");
		facetList.add("taxon.measurement_or_fact_threatStatus_txt");
		facetList.add("taxon.measurement_or_fact_Lifeform_txt");
		facetList.add("taxon.measurement_or_fact_Habitat_txt");

		Cube cube = new Cube(selectedFacets);
		cube.setDefaultLevel("taxon.order_s");
		Dimension taxonomy = new Dimension("taxonomy");
		cube.addDimension(taxonomy);

		taxonomy.addLevel("taxon.order_s", false);
		taxonomy.addLevel("taxon.family_ss", false);
		taxonomy.addLevel("taxon.subfamily_ss", false);
		taxonomy.addLevel("taxon.tribe_ss", false);
		taxonomy.addLevel("taxon.subtribe_ss", false);
		taxonomy.addLevel("taxon.genus_ss", false);

		Dimension distribution = new Dimension("distribution");
		cube.addDimension(distribution);

		distribution.addLevel("taxon.distribution_TDWG_0_ss", true);
		distribution.addLevel("taxon.distribution_TDWG_1_ss", true);
		distribution.addLevel("taxon.distribution_TDWG_2_ss", true);

		Dimension taxonRank = new Dimension("taxonRank");
		cube.addDimension(taxonRank);
		taxonRank.addLevel("taxon.taxon_rank_s", false);

		Dimension taxonomicStatus = new Dimension("taxonomicStatus");
		cube.addDimension(taxonomicStatus);
		taxonomicStatus.addLevel("taxon.taxonomic_status_s", false);

		Dimension lifeForm = new Dimension("lifeForm");
		cube.addDimension(lifeForm);
		lifeForm.addLevel("taxon.measurement_or_fact_Lifeform_txt", false);

		Dimension habitat = new Dimension("habitat");
		cube.addDimension(habitat);
		habitat.addLevel("taxon.measurement_or_fact_Habitat_txt", false);

		Dimension conservationStatus = new Dimension("conservationStatus");
		cube.addDimension(conservationStatus);
		conservationStatus.addLevel(
				"taxon.measurement_or_fact_threatStatus_txt", false);

		Dimension withDescriptions = new Dimension("hasDescriptions");
		cube.addDimension(withDescriptions);
		withDescriptions.addLevel("taxon.descriptions_not_empty_b", false);

		Dimension withImages = new Dimension("hasImages");
		cube.addDimension(withImages);
		withImages.addLevel("taxon.images_not_empty_b", false);

		CellSet cellSet = searchableObjectService.analyse(rows, cols, firstCol,
				maxCols, firstRow, maxRows, selectedFacets,
				facetList.toArray(new String[facetList.size()]), cube);

		uiModel.addAttribute("cellSet", cellSet);
		uiModel.addAttribute("view", view);
		return "visualise";
	}

	/**
	 * @param term
	 *            The term to search for
	 * @return A list of terms to serialize
	 */
	@RequestMapping(value = "/autocomplete",   method = RequestMethod.GET, produces = "application/json")
	public @ResponseBody
	List<Match> autocomplete(@RequestParam(required = true) String term) throws SolrServerException {
		return searchableObjectService.autocomplete(term, 10, null);
	}

	@RequestMapping(value = "/autocomplete/comment", method = RequestMethod.GET, produces = "application/json")
	public @ResponseBody List<Match> autocompleteComments(@RequestParam(required = true) String term) throws SolrServerException {
		return commentService.autocomplete(term, 10, null);
	}

	@RequestMapping(value = "/autocomplete/user", method = RequestMethod.GET, produces = "application/json")
	public @ResponseBody List<Match> autocompleteUsers(@RequestParam(required = true) String term) throws SolrServerException {
		return userService.autocomplete(term, 10, null);
	}

	@RequestMapping(value = "/autocomplete/organisation", method = RequestMethod.GET, produces = "application/json")
	public @ResponseBody List<Match> autocompleteOrganisations(@RequestParam(required = true) String term) throws SolrServerException {
		return organisationService.autocomplete(term, 10, null);
	}

	@RequestMapping(value = "/autocomplete/resource", method = RequestMethod.GET, produces = "application/json")
	public @ResponseBody List<Match> autocompleteResources(@RequestParam(required = true) String term) throws SolrServerException {
		return resourceService.autocomplete(term, 10, null);
	}

	@ExceptionHandler(SolrServerException.class)
	@ResponseStatus(value = HttpStatus.SERVICE_UNAVAILABLE)
	public ModelAndView handleObjectNotFoundException(SolrServerException sse) {
		ModelAndView modelAndView = new ModelAndView("serviceUnavailable");
		modelAndView.addObject("exception", sse);
		return modelAndView;
	}

	@RequestMapping(method = RequestMethod.OPTIONS,
			produces = "application/json")
	public ResponseEntity<RestDoc> optionsResource() throws JsonMappingException {
		RestDoc restDoc = new RestDoc();
		HashMap<String,Schema> schemas = new HashMap<String,Schema>();
		Schema pagerSchema = new Schema();
		SchemaFactoryWrapper pageVisitor = new SchemaFactoryWrapper();
		objectMapper.acceptJsonFormatVisitor(objectMapper.constructType(Page.class), pageVisitor);
		pagerSchema.setSchema(pageVisitor.finalSchema());
		schemas.put("http://e-monocot.org#page", pagerSchema);
		restDoc.setSchemas(schemas);

		GlobalHeader headers = new GlobalHeader();
		headers.request("Content-Type","Must be set to application/json",true);
		headers.request("Authorization","Supports HTTP Basic. Users may also use their api key",false);

		restDoc.setHeaders(headers);

		ParamValidation integerParam = new ParamValidation();
		integerParam.setType("match");
		integerParam.setPattern("\\d+");
		ParamValidation apikeyParam = new ParamValidation();
		apikeyParam.setType("match");
		apikeyParam.setPattern("[0-9a-f]{8}-[0-9a-f]{4}-[0-9a-f]{4}-[0-9a-f]{4}-[0-9a-f]{12}");
		ParamValidation stringParam = new ParamValidation();
		stringParam.setType("match");
		stringParam.setPattern("[0-9a-f]+");
		ParamValidation doubleParam = new ParamValidation();
		doubleParam.setType("match");
		doubleParam.setPattern("[0-9]+\\.[0.9]+");

		Set<RestResource> resources = new HashSet<RestResource>();

		RestResource searchForObjects = new RestResource();
		searchForObjects.setId("Search");
		searchForObjects.setPath("/search{?query,x1,y1,x2,y2,facet,limit,start,sort,callback,apikey,fetch}");
		searchForObjects.param("limit", "The maximum number of resources to return", integerParam);
		searchForObjects.param("start", "The number of pages (of size _limit_) offset from the beginning of the recordset", integerParam);
		searchForObjects.param("apikey", "The apikey of the user account making the request", apikeyParam);
		searchForObjects.param("callback", "The name of the callback function used to wrap the JSON response", stringParam);
		searchForObjects.param("x1", "The southerly extent of the bounding box (uses WGS84 Coordinate reference system). Only documents with distributions within the bounding box will be returned", doubleParam);
		searchForObjects.param("y1", "The westerly extent of the bounding box (uses WGS84 Coordinate reference system). Only documents with distributions within the bounding box will be returned", doubleParam);
		searchForObjects.param("x2", "The northerly extent of the bounding box (uses WGS84 Coordinate reference system). Only documents with distributions within the bounding box will be returned", doubleParam);
		searchForObjects.param("y2", "The easterly extent of the bounding box (uses WGS84 Coordinate reference system). Only documents with distributions within the bounding box will be returned", doubleParam);
		searchForObjects.param("query", "A free-text query string. Only documents matching the query string will be returned", stringParam);
		searchForObjects.param("facet", "Only return documents which match a particular filter, in the form {fieldName}:{fieldValue} where fieldName is from the controlled vocabulary defined by org.emonocot.pager.FacetName.", stringParam);
		searchForObjects.param("sort", "Sort the result set according to the supplied criteria, in the form {fieldName}_(asc|desc) where fieldName is from the controlled vocabulary defined by org.emonocot.pager.FacetName.", stringParam);
		searchForObjects.param("fetch", "The name of a valid 'fetch-profile' which will load some or all related objects prior to serialization. Try 'object-page' to return most related objects", stringParam);

		MethodDefinition searchObjects = new MethodDefinition();
		searchObjects.description("Search for resources");
		ResponseDefinition searchObjectsResponseDefinition = new ResponseDefinition();

		searchObjectsResponseDefinition.type("application/json", "http://e-monocot.org#page");
		searchObjectsResponseDefinition.type("application/javascript", "http://e-monocot.org#page");
		searchObjects.response(searchObjectsResponseDefinition);
		searchObjects.statusCode("200", "Successfully searched for resources");

		searchForObjects.method("GET", searchObjects);
		resources.add(searchForObjects);

		restDoc.setResources(resources);

		return new ResponseEntity<RestDoc>(restDoc,HttpStatus.OK);
	}

	@RequestMapping(value = "/ncbi", method = RequestMethod.GET, consumes = "application/json", produces = "application/json")
	public ResponseEntity<NcbiDto> ncbi(@RequestParam(value = "query", required = true) String query) {
		NcbiDto ncbiDto = new NcbiDto();
		try {
			ncbiDto = ncbiService.issueRequest(query);
		} catch (RemoteException re) {
			logger.error("Exception using NCBI Service :" + re.getMessage(), re);
			return new ResponseEntity<NcbiDto>(HttpStatus.INTERNAL_SERVER_ERROR);
		}

		return new ResponseEntity<NcbiDto>(ncbiDto,HttpStatus.OK);
	}


	@RequestMapping(value = "/geo", method = RequestMethod.GET, consumes = "application/json", produces = "application/json")
	public ResponseEntity<FeatureCollection> spatial(
			@RequestParam(value = "query", required = false) String query,
			@RequestParam(value = "x1", required = false) Double x1,
			@RequestParam(value = "y1", required = false) Double y1,
			@RequestParam(value = "x2", required = false) Double x2,
			@RequestParam(value = "y2", required = false) Double y2,
			@RequestParam(value = "limit", required = false, defaultValue = "5000") Integer limit,
			@RequestParam(value = "start", required = false, defaultValue = "0") Integer start,
			@RequestParam(value = "facet", required = false) @FacetRequestFormat List<FacetRequest> facets) throws SolrServerException {
		String spatial = null;
		DecimalFormat decimalFormat = new DecimalFormat("###0.0");
		if (x1 != null
				&& y1 != null
				&& x2 != null
				&& y2 != null
				&& (x1 != 0.0 && y1 != 0.0 && x2 != 0.0 && x2 != 0.0 && y2 != 0.0)) {
			spatial = "geo:\"Intersects(" + decimalFormat.format(x1) + " "
					+ decimalFormat.format(y1) + " " + decimalFormat.format(x2)
					+ " " + decimalFormat.format(y2) + ")\"";
		}

		Map<String, String> selectedFacets = new HashMap<String, String>();
		if (facets != null && !facets.isEmpty()) {
			for (FacetRequest facetRequest : facets) {
				selectedFacets.put(facetRequest.getFacet(),
						facetRequest.getSelected());
			}
			logger.debug(selectedFacets.size()
					+ " facets have been selected from " + facets.size()
					+ " available");
		} else {
			logger.debug("There were no facets available to select from");
		}
		selectedFacets.put("base.class_s", "org.emonocot.model.TypeAndSpecimen");

		// Run the search
		Page<TypeAndSpecimen> result = typeAndSpecimenQuery(query, start, limit,
				spatial,  new String[] {}, null, null, selectedFacets);

		FeatureCollection featureCollection = new FeatureCollection();
		for(TypeAndSpecimen typeAndSpecimen : result.getRecords()) {
			featureCollection.getFeatures().add(Feature.fromTypeAndSpecimen(typeAndSpecimen));
		}
		return new ResponseEntity<FeatureCollection>(featureCollection,HttpStatus.OK);
	}

	private Page<TypeAndSpecimen> typeAndSpecimenQuery(String query,
			Integer start, Integer limit, String spatial,
			String[] responseFacets, Map<String, String> facetPrefixes,
			String sort, Map<String, String> selectedFacets) throws SolrServerException {
		Page<TypeAndSpecimen> result = typeAndSpecimenService.search(query, spatial, limit, start, responseFacets,facetPrefixes, selectedFacets, sort, null);
		result.putParam("query", query);

		return result;
	}
}
