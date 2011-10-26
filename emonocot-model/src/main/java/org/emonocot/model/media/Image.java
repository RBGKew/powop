package org.emonocot.model.media;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToMany;
import javax.persistence.ManyToOne;
import javax.persistence.OneToMany;
import javax.persistence.Transient;

import org.codehaus.jackson.annotate.JsonIgnore;
import org.codehaus.jackson.map.annotate.JsonDeserialize;
import org.codehaus.jackson.map.annotate.JsonSerialize;
import org.emonocot.model.common.Annotation;
import org.emonocot.model.common.BaseData;
import org.emonocot.model.marshall.json.TaxonDeserializer;
import org.emonocot.model.marshall.json.TaxonSerializer;
import org.emonocot.model.taxon.Taxon;
import org.hibernate.annotations.Cascade;
import org.hibernate.annotations.CascadeType;
import org.hibernate.annotations.Where;
import org.hibernate.search.annotations.Field;
import org.hibernate.search.annotations.Fields;
import org.hibernate.search.annotations.Index;
import org.hibernate.search.annotations.Indexed;
import org.hibernate.search.annotations.IndexedEmbedded;

/**
 *
 * @author ben
 *
 */
@Entity
@Indexed(index = "org.emonocot.model.common.SearchableObject")
public class Image extends BaseData {
    /**
     *
     */
    private static final long serialVersionUID = 3341900807619517602L;

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
    */
   private Set<Annotation> annotations = new HashSet<Annotation>();

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
    @Fields({@Field,@Field(name="label", index = Index.UN_TOKENIZED)})
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
    @Cascade({CascadeType.SAVE_UPDATE })
    @JsonSerialize(contentUsing = TaxonSerializer.class)
    public List<Taxon> getTaxa() {
        return taxa;
    }

    /**
     *
     * @param taxa Set the taxa associated with this image
     */
    @JsonDeserialize(contentUsing = TaxonDeserializer.class)
    public void setTaxa(List<Taxon> taxa) {
        this.taxa = taxa;
    }

    /**
     *
     * @return the name of this class
     */
    @Transient
    @JsonIgnore
    public final String getClassName() {
      return "Image";
    }

    /**
     * @return the annotations
     */
    @OneToMany(fetch = FetchType.LAZY, orphanRemoval = true)
    @JoinColumn(name = "annotatedObjId")
    @Where(clause = "annotatedObjType = 'Image'")
    @Cascade({ CascadeType.SAVE_UPDATE, CascadeType.MERGE, CascadeType.DELETE })
    @JsonIgnore
    public Set<Annotation> getAnnotations() {
        return annotations;
    }

    /**
     * @param annotations
     *            the annotations to set
     */
    public void setAnnotations(Set<Annotation> annotations) {
        this.annotations = annotations;
    }
}
