package org.emonocot.portal.controller;

import org.emonocot.api.FacetName;

/**
 *
 * @author ben
 *
 */
public class FacetRequest {
    /**
     * The name of the facet.
     */
    private FacetName facet;
    /**
     * The selected facet.
     */
    private String selected;

    /**
     * @return the facet
     */
    public final FacetName getFacet() {
        return facet;
    }

    /**
     * @param newFacet the facet to set
     */
    public final void setFacet(final FacetName newFacet) {
        this.facet = newFacet;
    }

    /**
     * @return the selected
     */
    public final String getSelected() {
        return selected;
    }

    /**
     * @param newSelected the selected to set
     */
    public final void setSelected(final String newSelected) {
        this.selected = newSelected;
    }

}
