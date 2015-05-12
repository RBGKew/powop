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
	
	private static String FACET_SEPARATOR = ":";

    /**
     * @param facetRequest Set the facet request
     * @param locale Set the locale
     * @return the facet request as a string
     */
    public final String print(
            final FacetRequest facetRequest, final Locale locale) {
        return facetRequest.getFacet() + FacetRequestFormatter.FACET_SEPARATOR + facetRequest.getSelected();
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
        if (-1 == facetRequest.indexOf(FacetRequestFormatter.FACET_SEPARATOR)) {
            throw new ParseException(
                    facetRequest + " is not a valid facet request", 0);
        } else {
            String facetName
                = facetRequest.substring(0, facetRequest.indexOf(FacetRequestFormatter.FACET_SEPARATOR));
            String selectedFacet
                = facetRequest.substring(facetRequest.indexOf(FacetRequestFormatter.FACET_SEPARATOR) + 1);
            FacetRequest result = new FacetRequest();
            result.setFacet(facetName);
            result.setSelected(selectedFacet);
            return result;
        }
    }

}
