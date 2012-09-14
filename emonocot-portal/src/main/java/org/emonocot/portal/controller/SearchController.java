package org.emonocot.portal.controller;

import java.text.DecimalFormat;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.apache.commons.lang3.StringUtils;
import org.emonocot.api.FacetName;
import org.emonocot.api.ImageService;
import org.emonocot.api.PlaceService;
import org.emonocot.api.SearchableObjectService;
import org.emonocot.api.Sorting;
import org.emonocot.api.TaxonService;
import org.emonocot.model.common.SearchableObject;
import org.emonocot.model.key.IdentificationKey;
import org.emonocot.model.media.Image;
import org.emonocot.model.pager.Page;
import org.emonocot.model.taxon.Taxon;
import org.emonocot.portal.format.annotation.FacetRequestFormat;
import org.emonocot.portal.format.annotation.SortingFormat;
import org.emonocot.api.IdentificationKeyService;
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

    /**
    *
    */
    private static Logger queryLog = LoggerFactory.getLogger("query");

    /**
     *
     */
    private static Logger logger = LoggerFactory
            .getLogger(SearchController.class);
    /**
     *
     */
    private TaxonService taxonService;

    /**
     *
     */
    private SearchableObjectService searchableObjectService;

    /**
     *
     */
    private ImageService imageService;

    /**
     *
     */
    private IdentificationKeyService keyService;
    
    /**
     * 
     */
    private PlaceService placeService;

    /**
     *
     * @param newTaxonService
     *            Set the taxon service
     */
    @Autowired
    public final void setTaxonService(final TaxonService newTaxonService) {
        this.taxonService = newTaxonService;
    }

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
     *
     * @param newImageService
     *            set the image service
     */
    @Autowired
    public final void setImageService(final ImageService newImageService) {
        this.imageService = newImageService;
    }

    /**
     * @param newKeyService the keyService to set
     */
    @Autowired
    public final void setKeyService(
            final IdentificationKeyService newKeyService) {
        this.keyService = newKeyService;
    }
    
    /**
	 * @param placeService the placeService to set
	 */
    @Autowired
	public final void setPlaceService(PlaceService placeService) {
		this.placeService = placeService;
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
    private Page<? extends SearchableObject> runQuery(String query, Integer start, Integer limit, String spatial, FacetName[] responseFacets, Sorting sort, Map<FacetName, String> selectedFacets){
    	Page<? extends SearchableObject> result = null;
        if (selectedFacets == null
                || !selectedFacets.containsKey(FacetName.CLASS)) {

            result = searchableObjectService.search(
                    query, spatial, limit, start, responseFacets,
                    selectedFacets, sort, "taxon-with-image");
        } else {
            if (selectedFacets.get(FacetName.CLASS)
                    .equals("org.emonocot.model.media.Image")) {
                logger.debug("Using the image service for " + query);
                result = imageService.search(query, spatial , limit, start,
                        responseFacets,
                        selectedFacets, sort, "image-taxon");
            } else if (selectedFacets.get(FacetName.CLASS).equals(
                    "org.emonocot.model.taxon.Taxon")) {
                logger.debug("Using the taxon service for " + query);
                result = taxonService.search(query, spatial, limit, start,
                        responseFacets,
                        selectedFacets, sort, "taxon-with-image");
            } else if (selectedFacets.get(FacetName.CLASS).equals(
                    "org.emonocot.model.key.IdentificationKey")) {
                logger.debug("Using the IdentificationKey service for " + query);
                result = keyService.search(query, spatial, limit, start,
                        responseFacets,
                        selectedFacets, sort, "front-cover");
            } else if (selectedFacets.get(FacetName.CLASS).equals("org.emonocot.model.geography.Place")) {
        		result = placeService.search(
                            query, spatial, limit, start, responseFacets,
                            selectedFacets, sort, "taxon-with-image");
        	} else {
                logger.error("We can't search by an object of FacetName.CLASS idx="
                        + selectedFacets.get(FacetName.CLASS));
            }
        }
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
            } else if (className.equals("org.emonocot.model.media.Image")) {
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
       @RequestParam(value = "sort", required = false) @SortingFormat final Sorting sort,
       @RequestParam(value = "view", required = false) String view,
       final Model model) {

       Map<FacetName, String> selectedFacets = null;
       if (facets != null && !facets.isEmpty()) {
           selectedFacets = new HashMap<FacetName, String>();
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
       List<FacetName> responseFacetList = new ArrayList<FacetName>();
       responseFacetList.add(FacetName.CLASS);
       responseFacetList.add(FacetName.FAMILY);
       responseFacetList.add(FacetName.CONTINENT);
       responseFacetList.add(FacetName.AUTHORITY);
       String className = null;
       if (selectedFacets == null) {
           logger.debug("No selected facets, setting default response facets");
       } else {
           if (selectedFacets.containsKey(FacetName.CLASS)) {
        	   className = selectedFacets.get(FacetName.CLASS);
               if (className.equals("org.emonocot.model.taxon.Taxon")) {
                   logger.debug("Adding taxon specific facets");
                   responseFacetList.add(FacetName.RANK);
                   responseFacetList.add(FacetName.TAXONOMIC_STATUS);
               }
           }
           if (selectedFacets.containsKey(FacetName.CONTINENT)) {
               logger.debug("Adding region facet");
               responseFacetList.add(FacetName.REGION);
           } else {
               selectedFacets.remove(FacetName.REGION);
           }
       }
       FacetName[] responseFacets = new FacetName[]{};
       responseFacets = responseFacetList.toArray(responseFacets);
       limit = setLimit(view, className);

       //Run the search
       Page<? extends SearchableObject> result = runQuery(query, start, limit, null, responseFacets, sort, selectedFacets);

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
      @RequestParam(value = "sort", required = false) @SortingFormat final Sorting sort,
      @RequestParam(value = "view", required = false) String view,
      final Model model) {
      String spatial = null;
      DecimalFormat decimalFormat = new DecimalFormat("###0.0");
      if (x1 != null && y1 != null && x2 != null && y2 != null && (x1 != 0.0 && y1 != 0.0 && x2 != 0.0 && x2 != 0.0 && y2 != 0.0)) {
        spatial = "Intersects(" + decimalFormat.format(x1) + " " + decimalFormat.format(y1) + " " + decimalFormat.format(x2) + " " + decimalFormat.format(y2) + ")";
      }

      Map<FacetName, String> selectedFacets = null;
      if (facets != null && !facets.isEmpty()) {
          selectedFacets = new HashMap<FacetName, String>();
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
      List<FacetName> responseFacetList = new ArrayList<FacetName>();
      responseFacetList.add(FacetName.CLASS);
      responseFacetList.add(FacetName.FAMILY);
      responseFacetList.add(FacetName.AUTHORITY);
      String className = null;
      if (selectedFacets == null) {
          logger.debug("No selected facets, setting default response facets");
      } else {
          if (selectedFacets.containsKey(FacetName.CLASS)) {
        	  className = selectedFacets.get(FacetName.CLASS);
              if (selectedFacets.get(FacetName.CLASS).equals(
                      "org.emonocot.model.taxon.Taxon")) {
                  logger.debug("Adding taxon specific facets");
                  responseFacetList.add(FacetName.RANK);
                  responseFacetList.add(FacetName.TAXONOMIC_STATUS);
              }
          }
          if (selectedFacets.containsKey(FacetName.CONTINENT)) {
              logger.debug("Removing continent facet");
              responseFacetList.remove(FacetName.CONTINENT);
          }
      }
      FacetName[] responseFacets = new FacetName[]{};
      responseFacets = responseFacetList.toArray(responseFacets);
      limit = setLimit(view, className);

      //Run the search
      Page<? extends SearchableObject> result = runQuery(query, start, limit, spatial, responseFacets, sort, selectedFacets);
      
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
    @RequestMapping(value = "/autocomplete",
                    method = RequestMethod.GET,
                    headers = "Accept=application/json")
    public final @ResponseBody List<Match> get(
            @RequestParam(required = true) final String term) {
        Page<SearchableObject> result = searchableObjectService.search(term
                + "*", null, 10, 0, null, null, null, null);
        List<Match> matches = new ArrayList<Match>();
        for (SearchableObject object : result.getRecords()) {
            if (object.getClass().equals(Taxon.class)) {
                matches.add(new Match(((Taxon) object).getName()));
            } else if (object.getClass().equals(Image.class)) {
                matches.add(new Match(((Image) object).getCaption()));
            } else if (object.getClass().equals(IdentificationKey.class)) {
                matches.add(new Match(((IdentificationKey) object).getTitle()));
            } else {
                logger.error("Unable to determine autocomplete label for " + object);
            }
        }
        return matches;
    }

    /**
     * Used to return the autocomplete matches.
     *
     * @author ben
     *
     */
    class Match {

        /**
         *
         */
        private String label;

        /**
         * @return the label
         */
        public final String getLabel() {
            return label;
        }

        /**
         * @param newLabel the label to set
         */
        public final void setLabel(final String newLabel) {
            this.label = newLabel;
        }

        /**
         *
         * @param newLabel Set the label
         */
        public Match(final String newLabel) {
            this.label = newLabel;
        }
    }
}
