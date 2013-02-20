package org.emonocot.portal.controller;

import java.text.DecimalFormat;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.apache.commons.lang3.StringUtils;
import org.emonocot.api.SearchableObjectService;
import org.emonocot.api.autocomplete.Match;
import org.emonocot.model.SearchableObject;
import org.emonocot.pager.Page;
import org.emonocot.portal.format.annotation.FacetRequestFormat;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;

/**
 *
 * @author ben
 *
 */
@Controller
public class SearchController {

    private static Logger queryLog = LoggerFactory.getLogger("query");

    private static Logger logger = LoggerFactory.getLogger(SearchController.class);

    private SearchableObjectService searchableObjectService;

    /**
     *
     * @param newSearchableObjectService
     *            set the service to search across all 'searchable' objects
     */
    @Autowired
    public final void setSearchableObjectService(
            final SearchableObjectService newSearchableObjectService) {
        this.searchableObjectService = newSearchableObjectService;
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
    private Page<? extends SearchableObject> runQuery(String query, Integer start, Integer limit, String spatial, String[] responseFacets, Map<String,String> facetPrefixes, String sort, Map<String, String> selectedFacets){
    	Page<? extends SearchableObject> result = searchableObjectService.search(query, spatial, limit, start, responseFacets,facetPrefixes, selectedFacets, sort, null);
        queryLog.info("Query: \'{}\', start: {}, limit: {},"
                + "facet: [{}], {} results", new Object[] {query,
                start, limit, selectedFacets, result.getSize() });
        result.putParam("query", query);
        
        return result;
    }

    /**
     *
     * @param view Set the view name
     * @param className Set the class name
     * @return the default limit
     */
    private Integer setLimit(final String view, final String className) {
        if (view == null || view == "") {
            if (className == null) {
                return 10;
            } else if (className.equals("org.emonocot.model.Image")) {
                return 24;
            } else {
                return 10;
            }
        } else if (view.equals("list")) {
            return 10;
        } else if (view.equals("grid")) {
            return 24;
        } else {
            return 24;
        }
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
    * @param view Set the view
    * @param model Set the model
    *
    * @return a model and view
    */
   @RequestMapping(value = "/search", method = RequestMethod.GET)
   public final String search(
       @RequestParam(value = "query", required = false) final String query,
       @RequestParam(value = "limit", required = false, defaultValue = "10") Integer limit,
       @RequestParam(value = "start", required = false, defaultValue = "0") final Integer start,
       @RequestParam(value = "facet", required = false) @FacetRequestFormat final List<FacetRequest> facets,
       @RequestParam(value = "sort", required = false) String sort,
       @RequestParam(value = "view", required = false) String view,
       final Model model) {

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

       //Decide which facets to return
       List<String> responseFacetList = new ArrayList<String>();
       Map<String,String> facetPrefixes = new HashMap<String,String>();
       responseFacetList.add("base.class_s");
       responseFacetList.add("taxon.family_s");
       responseFacetList.add("taxon.distribution_TDWG_0_ss");
       responseFacetList.add("searchable.sources_ss");
       String className = null;
       if (selectedFacets == null) {
           logger.debug("No selected facets, setting default response facets");
       } else {
           if (selectedFacets.containsKey("base.class_s")) {
        	   className = selectedFacets.get("base.class_s");
               if (className.equals("org.emonocot.model.Taxon")) {
                   logger.debug("Adding taxon specific facets");
                   responseFacetList.add("taxon.measurement_or_fact_IUCNConservationStatus_txt");
                   responseFacetList.add("taxon.measurement_or_fact_Lifeform_txt");
                   responseFacetList.add("taxon.measurement_or_fact_Habitat_txt");
                   responseFacetList.add("taxon.taxon_rank_s");
                   responseFacetList.add("taxon.taxonomic_status_s");
               }
           }
           if (selectedFacets.containsKey("taxon.distribution_TDWG_0_ss")) {
               logger.debug("Adding region facet");
               responseFacetList.add("taxon.distribution_TDWG_1_ss");               
               facetPrefixes.put("taxon.distribution_TDWG_1_ss", selectedFacets.get("taxon.distribution_TDWG_0_ss") + "_");
           } else {
               selectedFacets.remove("taxon.distribution_TDWG_1_ss");
           }
       }
       String[] responseFacets = new String[]{};
       responseFacets = responseFacetList.toArray(responseFacets);
       limit = setLimit(view, className);

       //Run the search
       Page<? extends SearchableObject> result = runQuery(query, start, limit, null, responseFacets, facetPrefixes, sort, selectedFacets);

       result.putParam("view", view);
       result.setSort(sort);
       model.addAttribute("result", result);
       return "search";
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
   * @param x1 the first latitude
   * @param x2 the second latitude
   * @param y1 the first longitude
   * @param y2 the second longitude
   * @param view Set the view
   * @param model Set the model
   *
   * @return a model and view
   */
  @RequestMapping(value = "/spatial", method = RequestMethod.GET)
  public final String spatial(
      @RequestParam(value = "query", required = false) final String query,
      @RequestParam(value = "x1", required = false) final Double x1,
      @RequestParam(value = "y1", required = false) final Double y1,
      @RequestParam(value = "x2", required = false) final Double x2,
      @RequestParam(value = "y2", required = false) final Double y2,
      @RequestParam(value = "featureId", required = false) final String featureId,
      @RequestParam(value = "limit", required = false, defaultValue = "10") Integer limit,
      @RequestParam(value = "start", required = false, defaultValue = "0") final Integer start,
      @RequestParam(value = "facet", required = false) @FacetRequestFormat final List<FacetRequest> facets,
      @RequestParam(value = "sort", required = false) final String sort,
      @RequestParam(value = "view", required = false) String view,
      final Model model) {
      String spatial = null;
      DecimalFormat decimalFormat = new DecimalFormat("###0.0");
      if (x1 != null && y1 != null && x2 != null && y2 != null && (x1 != 0.0 && y1 != 0.0 && x2 != 0.0 && x2 != 0.0 && y2 != 0.0)) {
        spatial = "Intersects(" + decimalFormat.format(x1) + " " + decimalFormat.format(y1) + " " + decimalFormat.format(x2) + " " + decimalFormat.format(y2) + ")";
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

      //Decide which facets to return
      List<String> responseFacetList = new ArrayList<String>();
      responseFacetList.add("base.class_s");
      responseFacetList.add("taxon.family_s");
      responseFacetList.add("searchable.sources_ss");
      String className = null;
      if (selectedFacets == null) {
          logger.debug("No selected facets, setting default response facets");
      } else {
          if (selectedFacets.containsKey("base.class_s")) {
        	  className = selectedFacets.get("base.class_s");
              if (selectedFacets.get("base.class_s").equals(
                      "org.emonocot.model.Taxon")) {
                  logger.debug("Adding taxon specific facets");
                  responseFacetList.add("taxon.measurement_or_fact_IUCNConservationStatus_txt");
                  responseFacetList.add("taxon.measurement_or_fact_Lifeform_txt");
                  responseFacetList.add("taxon.measurement_or_fact_Habitat_txt");
                  responseFacetList.add("taxon.taxon_rank_s");
                  responseFacetList.add("taxon.taxonomic_status_s");
              }
          }
          if (selectedFacets.containsKey("taxon.distribution_TDWG_0_ss")) {
              logger.debug("Removing continent facet");
              responseFacetList.remove("taxon.distribution_TDWG_0_ss");
          }
      }
      String[] responseFacets = new String[]{};
      responseFacets = responseFacetList.toArray(responseFacets);
      limit = setLimit(view, className);

      //Run the search
      Page<? extends SearchableObject> result = runQuery(query, start, limit, spatial, responseFacets, null, sort, selectedFacets);
      
      if (spatial != null) {
        result.putParam("x1", x1);
        result.putParam("y1", y1);
        result.putParam("x2", x2);
        result.putParam("y2", y2);
      }
      if(!StringUtils.isEmpty(featureId)){
          result.putParam("featureId", featureId);
      }
      result.putParam("view", view);
      result.setSort(sort);
      model.addAttribute("result", result);
      return "spatial";
  }

    /**
     * @param term
     *            The term to search for
     * @return A list of terms to serialize
     */
    @RequestMapping(value = "/autocomplete", method = RequestMethod.GET, produces = "application/json")
    public final @ResponseBody List<Match> autocomplete(@RequestParam(required = true) final String term) {    	
        return searchableObjectService.autocomplete(term, 10, null);
    }
}
