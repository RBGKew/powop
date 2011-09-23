package org.emonocot.model.description;

import javax.persistence.Entity;
import javax.persistence.EnumType;
import javax.persistence.Enumerated;
import javax.persistence.FetchType;
import javax.persistence.Inheritance;
import javax.persistence.InheritanceType;
import javax.persistence.ManyToOne;

import org.apache.commons.lang.ObjectUtils;
import org.codehaus.jackson.annotate.JsonIgnore;
import org.emonocot.model.common.Base;
import org.emonocot.model.taxon.Taxon;
import org.hibernate.search.annotations.Field;

/**
 *
 * @author ben
 *
 */
@Entity
@Inheritance(strategy = InheritanceType.SINGLE_TABLE)
public abstract class Content extends Base {

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
    @JsonIgnore
    public void setTaxon(Taxon newTaxon) {
        this.taxon = newTaxon;
    }

    /**
     *
     * @return Return the subject that this content is about.
     */
    @Enumerated(value = EnumType.STRING)
    @Field
    public Feature getFeature() {
        return feature;
    }

    /**
     *
     * @param newFeature
     *            Set the subject that this content is about.
     */
    public void setFeature(Feature newFeature) {
        this.feature = newFeature;
    }

    /**
     *
     * @return Get the taxon that this content is about.
     */
    @JsonIgnore
    @ManyToOne(fetch = FetchType.LAZY)
    public Taxon getTaxon() {
        return taxon;
    }

    @Override
    public boolean equals(Object other) {
        if (!super.equals(other)) {
            return false;
        }

        Content content = (Content) other;
        return ObjectUtils.equals(this.feature, content.feature)
                && ObjectUtils.equals(this.taxon, content.taxon);
    }

    @Override
    public int hashCode() {
        return super.hashCode() + ObjectUtils.hashCode(this.taxon)
                + ObjectUtils.hashCode(this.feature);
    }
}
