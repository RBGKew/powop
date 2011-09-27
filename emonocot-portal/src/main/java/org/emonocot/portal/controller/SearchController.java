package org.emonocot.portal.controller;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.emonocot.model.common.SearchableObject;
import org.emonocot.model.pager.Page;
import org.emonocot.portal.format.annotation.FacetRequestFormat;
import org.emonocot.service.FacetName;
import org.emonocot.service.ImageService;
import org.emonocot.service.SearchableObjectService;
import org.emonocot.service.TaxonService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.servlet.ModelAndView;

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
     * @param taxonService
     *            Set the taxon service
     */
    @Autowired
    public final void setTaxonService(final TaxonService taxonService) {
        this.taxonService = taxonService;
    }

    /**
     *
     * @param searchableObjectService
     *            set the service to search across all 'searchable' objects
     */
    @Autowired
    public final void setSearchableObjectService(
            final SearchableObjectService searchableObjectService) {
        this.searchableObjectService = searchableObjectService;
    }

   /**
    *
    * @param imageService
    *            set the image service
    */
   @Autowired
   public final void setImageService(
           final ImageService imageService) {
       this.imageService = imageService;
   }

    /**
     * @return the name of the index view
     */
    @RequestMapping(value = "/index", method = RequestMethod.GET)
    public final String index() {
        return "index";
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
     * @return a model and view
     */
    @RequestMapping(value = "/search", method = RequestMethod.GET)
    public final ModelAndView search(
            @RequestParam(value = "query", required = false) final String query,
            @RequestParam(value = "limit", required = false, defaultValue = "10") final Integer limit,
            @RequestParam(value = "start", required = false, defaultValue = "0") final Integer start,
            @RequestParam(value = "facet", required = false) @FacetRequestFormat final List<FacetRequest> facets) {

        ModelAndView modelAndView = new ModelAndView("searchResponse");

        Map<FacetName, Integer> selectedFacets = null;
        if (facets != null && !facets.isEmpty()) {
            selectedFacets = new HashMap<FacetName, Integer>();
            for (FacetRequest facetRequest : facets) {
                selectedFacets.put(facetRequest.getFacet(),
                        facetRequest.getSelected());
            }
        }

        if (selectedFacets == null
                || !selectedFacets.containsKey(FacetName.CLASS)) {
            Page<SearchableObject> result = searchableObjectService.search(
                    query, null, limit, start, new FacetName[] {FacetName.CLASS,
                            FacetName.FAMILY, FacetName.CONTINENT,
                            FacetName.AUTHORITY},
                    selectedFacets);
            queryLog.info("Query: \'{}\', start: {}, limit: {},"
                    + "facet: [{}], {} results", new Object[] { query, start,
                    limit, selectedFacets, result.getSize() });

            result.putParam("query", query);
            modelAndView.addObject("result", result);
        } else {
            Page<? extends SearchableObject> result = null;
            logger.debug(selectedFacets.size()
                    + " facets have been selected from " + facets.size()
                    + " available");
            switch (selectedFacets.get(FacetName.CLASS)) {
            case 0:
                logger.debug("Using the image service for "+ query);
                result = imageService.search(query, null, limit,
                        start, new FacetName[] {FacetName.CLASS,
                        FacetName.FAMILY, FacetName.CONTINENT,
                        FacetName.AUTHORITY}, selectedFacets);
                queryLog.info("Query: \'{}\', start: {}, limit: {},"
                        + "facet: [{}], {} results", new Object[] {query,
                        start, limit, selectedFacets, result.getSize() });
                break;
            case 1:
                logger.debug("Using the taxon service for "+ query);
                result = taxonService.search(query, null, limit,
                        start, new FacetName[] {FacetName.CLASS,
                        FacetName.FAMILY, FacetName.CONTINENT,
                        FacetName.AUTHORITY }, selectedFacets);
                queryLog.info("Query: \'{}\', start: {}, limit: {},"
                        + "facet: [{}], {} results", new Object[] {query,
                        start, limit, selectedFacets, result.getSize() });
                break;
            default:
                logger.error("We can't search by an object of FacetName.CLASS idx="
                        + selectedFacets.get(FacetName.CLASS));
                break;
            }
            result.putParam("query", query);
            modelAndView.addObject("result", result);
        }
        return modelAndView;
    }
}
