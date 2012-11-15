package org.emonocot.portal.format;

import java.text.ParseException;
import java.util.Locale;

import org.emonocot.portal.controller.FacetRequest;
import org.springframework.format.Formatter;

/**
 *
 * @author ben
 *
 */
public class FacetRequestFormatter
    implements Formatter<FacetRequest> {

    /**
     * @param facetRequest Set the facet request
     * @param locale Set the locale
     * @return the facet request as a string
     */
    public final String print(
            final FacetRequest facetRequest, final Locale locale) {
        return facetRequest.getFacet() + "." + facetRequest.getSelected();
    }

    /**
     * @param facetRequest the facet request as a string
     * @param locale Set the locale
     * @return a FacetRequest object
     * @throws ParseException if there is a problem parsing the string
     */
    public final FacetRequest parse(
            final String facetRequest, final Locale locale)
            throws ParseException {
        if (-1 == facetRequest.indexOf(".")) {
            throw new ParseException(
                    facetRequest + " is not a valid facet request", 0);
        } else {
            String facetName
                = facetRequest.substring(0, facetRequest.indexOf("."));
            String selectedFacet
                = facetRequest.substring(facetRequest.indexOf(".") + 1);
            FacetRequest result = new FacetRequest();
            result.setFacet(facetName);
            result.setSelected(selectedFacet);
            return result;
        }
    }

}
