package org.emonocot.model.description;

import org.apache.commons.lang.ObjectUtils;
import org.emonocot.model.common.Base;
import org.emonocot.model.taxon.Taxon;

/**
 * 
 * @author ben
 * 
 */
public class Content extends Base {

    /**
     *
     */
    private Taxon taxon;

    /**
     *
     */
    private Feature feature;

    /**
     * 
     * @param newTaxon
     *            Set the taxon that this content is about.
     */
    public final void setTaxon(final Taxon newTaxon) {
        this.taxon = newTaxon;
    }

    /**
     * 
     * @return Return the subject that this content is about.
     */
    public final Feature getFeature() {
        return feature;
    }

    /**
     * 
     * @param newFeature
     *            Set the subject that this content is about.
     */
    public final void setFeature(final Feature newFeature) {
        this.feature = newFeature;
    }

    /**
     * 
     * @return Get the taxon that this content is about.
     */
    public final Taxon getTaxon() {
        return taxon;
    }

    @Override
    public boolean equals(final Object other) {
        if (!super.equals(other)) {
            return false;
        }

        Content content = (Content) other;
        return ObjectUtils.equals(this.feature, content.feature)
                && ObjectUtils.equals(this.taxon, content.taxon);
    }

    @Override
    public int hashCode() {
        return super.hashCode() + this.taxon.hashCode()
                + this.feature.hashCode();
    }
}
