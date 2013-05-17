package org.emonocot.portal.controller;

/**
 *
 * @author ben
 *
 */
public class FacetRequest {

    private String facet;

    private String selected;

    public final String getFacet() {
        return facet;
    }

    public final void setFacet(final String newFacet) {
        this.facet = newFacet;
    }

    public final String getSelected() {
        return selected;
    }

    public final void setSelected(final String newSelected) {
        this.selected = newSelected;
    }

}
