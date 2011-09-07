package org.emonocot.model.media;

import java.util.ArrayList;
import java.util.List;

import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.ManyToMany;
import javax.persistence.ManyToOne;
import javax.persistence.Transient;

import org.emonocot.model.common.SearchableObject;
import org.emonocot.model.taxon.Taxon;
import org.hibernate.annotations.Cascade;
import org.hibernate.annotations.CascadeType;
import org.hibernate.search.annotations.Field;
import org.hibernate.search.annotations.Indexed;
import org.hibernate.search.annotations.IndexedEmbedded;

/**
 *
 * @author ben
 *
 */
@Entity
@Indexed(index = "org.emonocot.model.common.SearchableObject")
public class Image extends SearchableObject {
    /**
     *
     */
    private String url;

    /**
     *
     */
    private String caption;

    /**
     *
     */
    private Taxon taxon;

    /**
     *
     */
    private List<Taxon> taxa = new ArrayList<Taxon>();

    /**
     *
     * @param newUrl Set the url of the image
     */
    public void setUrl(String newUrl) {
        this.url = newUrl;
    }

   /**
    *
    * @return the url of the image
    */
    @Field
    public String getUrl() {
        return url;
    }

    /**
     *
     * @return the caption
     */
    @Field
    public String getCaption() {
        return caption;
    }

    /**
     *
     * @param caption Set the caption
     */
    public void setCaption(String caption) {
        this.caption = caption;
    }

    /**
     * The taxon (page) this image should link to - should
     * be the lowest rank taxon in taxa?
     *
     * @return the taxon this image is of
     */
    @ManyToOne(fetch = FetchType.LAZY)
    @IndexedEmbedded
    public Taxon getTaxon() {
        return taxon;
    }

    /**
     *
     * @param taxon Set the taxon this image is of
     */
    public void setTaxon(Taxon taxon) {
        this.taxon = taxon;
    }

    /**
     * The list of all taxa associated with this image - including e.g. genera
     * family, and so on.
     *
     * @return a list of taxa
     */
    @ManyToMany(fetch = FetchType.LAZY, mappedBy = "images")
    @Cascade({CascadeType.SAVE_UPDATE})
    public List<Taxon> getTaxa() {
        return taxa;
    }

    /**
     *
     * @param taxa Set the taxa associated with this image
     */
    public void setTaxa(List<Taxon> taxa) {
        this.taxa = taxa;
    }

    @Transient
    public final String getClassName() {
      return "Image";
    }
}
