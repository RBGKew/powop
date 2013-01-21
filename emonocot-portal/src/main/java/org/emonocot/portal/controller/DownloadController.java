package org.emonocot.portal.controller;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.emonocot.api.SearchableObjectService;
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

/**
 *
 * @author ben
 *
 */
@Controller
public class DownloadController {
	public static String TAXON_FIELDS = "taxonId,scientificName,scientificNameAuthorship,scientificNameID,namePublishedInYear,higherClassification,kingdom,phylum,clazz,ordr,family,genus,subgenus,specificEpithet,infraspecificEpithet,taxonRank,verbatimTaxonRank,nomenclaturalCode,taxonomicStatus,taxonRemarks,acceptedNameUsage.taxonId,parentNameUsage.taxonId,namePublishedIn.identifier";
	public static String DESCRIPTION_FIELDS = "taxon.taxonId,identifier,dscription,type,source,creator,contributor,audience,license,rightsHolder";
	public static String DISTRIBUTION_FIELDS = "taxon.taxonId,identifier,loclity,countryCode,lifeStage,occurrenceStatus";
	public static String MEASUREMENT_OR_FACT_FIELDS = "taxon.taxonId,identifier,charactr,valu,accuracy,unit,determinedBy,determinedDate,mthod,remarks";
	public static String VERNACULAR_NAME_FIELDS = "taxon.taxonId,identifier,vrnacularName,source,lang,temporal,location,locality,countryCode,sex,plural,preferredName,organismPart,taxonRemarks,lifeStage";
	public static String IDENTIFIER_FIELDS = "taxon.taxonId,identifier";
	public static String IMAGE_FIELDS = "taxa[0].taxonId,identifier,refrences,description,locality,lattitude,longitude,format,created,contributor,publisher,audience,license,rightsHolder";
	public static String REFERENCE_FIELDS = "taxa[0].taxonId,identifier,bibliographicCitation,datePublished,source,description,subject,publicationLanguage,rights,taxonRemarks,type";
	public static String TYPE_AND_SPECIMEN_FIELDS = "taxa[0].taxonId,occurrenceId,typeStatus,typeDesignationType,typeDesignatedBy,scientificName,taxonRank,bibliographicCitation,institutionCode,collectionCode,catalogNumber,locality,sex,recordedBy,source,verbatimEventDate,verbatimLongitude,verbatimLatitude,verbatimLabel";

    private static Logger queryLog = LoggerFactory.getLogger("query");

    private static Logger logger = LoggerFactory.getLogger(DownloadController.class);

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
   @RequestMapping(value = "/download", method = RequestMethod.GET)
   public final String search(
       @RequestParam(value = "query", required = false) final String query,       
       @RequestParam(value = "facet", required = false) @FacetRequestFormat final List<FacetRequest> facets,
       @RequestParam(value = "sort", required = false) String sort,
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
                   responseFacetList.add("taxon.taxon_rank_s");
                   responseFacetList.add("taxon.taxonomic_status_s");
               }
           }
           if (selectedFacets.containsKey("taxon.distribution_TDWG_0_ss")) {
               logger.debug("Adding region facet");
               responseFacetList.add("taxon.distribution_TDWG_1_ss");
           } else {
               selectedFacets.remove("taxon.distribution_TDWG_1_ss");
           }
       }
       String[] responseFacets = new String[]{};
       responseFacets = responseFacetList.toArray(responseFacets);
       

       //Run the search
       Page<? extends SearchableObject> result = searchableObjectService.search(query, null, 0, 10, responseFacets, selectedFacets, sort, null);

       result.setSort(sort);
       model.addAttribute("result", result);       

       return "download/download";
   }

}
