package org.emonocot.model.media;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

import javax.persistence.*;

import org.codehaus.jackson.annotate.JsonIgnore;
import org.codehaus.jackson.map.annotate.JsonDeserialize;
import org.codehaus.jackson.map.annotate.JsonSerialize;
import org.emonocot.model.common.Annotation;
import org.emonocot.model.common.SearchableObject;
import org.emonocot.model.hibernate.TaxonomyBridge;
import org.emonocot.model.marshall.json.TaxonDeserializer;
import org.emonocot.model.marshall.json.TaxonSerializer;
import org.emonocot.model.taxon.Taxon;
import org.hibernate.annotations.Cascade;
import org.hibernate.annotations.CascadeType;
import org.hibernate.annotations.Type;
import org.hibernate.annotations.Where;
import org.hibernate.search.annotations.Analyzer;
import org.hibernate.search.annotations.ClassBridge;
import org.hibernate.search.annotations.DocumentId;
import org.hibernate.search.annotations.Field;
import org.hibernate.search.annotations.Fields;
import org.hibernate.search.annotations.Index;
import org.hibernate.search.annotations.Indexed;
import org.hibernate.search.annotations.IndexedEmbedded;

import com.vividsolutions.jts.geom.Point;

/**
 *
 * @author ben
 *
 */
@Entity
@Indexed(index = "org.emonocot.model.common.SearchableObject")
@ClassBridge(name = "taxon", impl = TaxonomyBridge.class, index = Index.UN_TOKENIZED,
        analyzer = @Analyzer(definition = "facetAnalyzer"))
public class Image extends SearchableObject {
    /**
     *
     */
    private static long serialVersionUID = 3341900807619517602L;

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
    private String description;

    /**
     *
     */
    private String locality;

    /**
     *
     */
    private String format;

    /**
     *
     */
    private String keywords;

    /**
     *
     */
    private Point location;

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
     */
    private Long id;

    /**
    *
    * @return the description
    */
   @Lob
   @Field
   public String getDescription() {
       return description;
   }

   /**
    *
    * @param description Set the description
    */
   public void setDescription(String description) {
       this.description = description;
   }

   /**
    *
    * @return the location as a string
    */
   @Field
   public String getLocality() {
       return locality;
   }

   /**
    *
    * @param locality Set the location as a string
    */
   public void setLocality(final String locality) {
       this.locality = locality;
   }

   /**
    *
    * @return the format of the image
    */
   public String getFormat() {
       return format;
   }

   /**
    *
    * @param format Set the format of the image
    */
   public void setFormat(String format) {
       this.format = format;
   }

   /**
    *
    * @return the keywords as a string, comma separated
    */
   @Field
   public String getKeywords() {
       return keywords;
   }

   /**
    *
    * @param keywords Set the keywords as a string, comma separated
    */
   public void setKeywords(String keywords) {
       this.keywords = keywords;
   }

   /**
    *
    * @return the location the image was taken
    */
   @Type(type = "spatialType")
   public Point getLocation() {
       return location;
   }

   /**
    *
    * @param location Set the location the image was taken
    */
   public void setLocation(Point location) {
       this.location = location;
   }

    /**
     *
     * @param newId
     *            Set the identifier of this object.
     */
    public void setId(Long newId) {
        this.id = newId;
    }

    /**
     *
     * @return Get the identifier for this object.
     */
    @Id
    @GeneratedValue(generator = "system-increment")
    @DocumentId
    public Long getId() {
        return id;
    }

    /**
     *
     * @param newUrl
     *            Set the url of the image
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
    @Fields({ @Field, @Field(name = "label", index = Index.UN_TOKENIZED) })
    public String getCaption() {
        return caption;
    }

    /**
     *
     * @param caption
     *            Set the caption
     */
    public void setCaption(String caption) {
        this.caption = caption;
    }

    /**
     * The taxon (page) this image should link to - should be the lowest rank
     * taxon in taxa?
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
     * @param taxon
     *            Set the taxon this image is of
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
    @Cascade({ CascadeType.SAVE_UPDATE })
    @JsonSerialize(contentUsing = TaxonSerializer.class)
    public List<Taxon> getTaxa() {
        return taxa;
    }

    /**
     *
     * @param taxa
     *            Set the taxa associated with this image
     */
    @JsonDeserialize(contentUsing = TaxonDeserializer.class)
    public void setTaxa(List<Taxon> taxa) {
        this.taxa = taxa;
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
