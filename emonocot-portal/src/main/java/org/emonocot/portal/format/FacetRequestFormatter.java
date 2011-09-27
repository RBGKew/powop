package org.emonocot.portal.format;

import java.text.ParseException;
import java.util.Locale;

import org.emonocot.portal.controller.FacetRequest;
import org.emonocot.service.FacetName;
import org.springframework.format.Formatter;

/**
 *
 * @author ben
 *
 */
public class FacetRequestFormatter
    implements Formatter<FacetRequest> {

    public final String print(
            final FacetRequest facetRequest, final Locale locale) {
        return facetRequest.getFacet().name()
            + "." + facetRequest.getSelected();
    }

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
            result.setFacet(FacetName.valueOf(facetName));
            result.setSelected(Integer.parseInt(selectedFacet));
            return result;
        }
    }

}
