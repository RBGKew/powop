package org.emonocot.portal.controller;

import java.text.DecimalFormat;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.apache.solr.client.solrj.SolrServerException;
import org.emonocot.api.TypeAndSpecimenService;
import org.emonocot.model.TypeAndSpecimen;
import org.emonocot.pager.Page;
import org.emonocot.portal.format.annotation.FacetRequestFormat;
import org.emonocot.portal.view.geojson.Feature;
import org.emonocot.portal.view.geojson.FeatureCollection;
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
@RequestMapping("/typeAndSpecimen")
public class TypeAndSpecimenController extends
		GenericController<TypeAndSpecimen, TypeAndSpecimenService> {
	
	private static final Logger logger = LoggerFactory.getLogger(TypeAndSpecimenController.class);

	public TypeAndSpecimenController() {
		super("typeAndSpecimen",TypeAndSpecimen.class);
	}
	
	@Autowired
    public void setTypeAndSpecimenService(TypeAndSpecimenService typeAndSpecimenService) {
        super.setService(typeAndSpecimenService);
    }
	
	private Page<TypeAndSpecimen> runQuery(String query,
			Integer start, Integer limit, String spatial,
			String[] responseFacets, Map<String, String> facetPrefixes,
			String sort, Map<String, String> selectedFacets) throws SolrServerException {
		Page<TypeAndSpecimen> result = getService()
				.search(query, spatial, limit, start, responseFacets,
						facetPrefixes, selectedFacets, sort, null);
		result.putParam("query", query);

		return result;
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
		Page<TypeAndSpecimen> result = runQuery(query, start, limit,
				spatial,  new String[] {}, null, null, selectedFacets);

		FeatureCollection featureCollection = new FeatureCollection();
		for(TypeAndSpecimen typeAndSpecimen : result.getRecords()) {
			featureCollection.getFeatures().add(Feature.fromTypeAndSpecimen(typeAndSpecimen));
		}
		return new ResponseEntity<FeatureCollection>(featureCollection,HttpStatus.OK);
	}

}
