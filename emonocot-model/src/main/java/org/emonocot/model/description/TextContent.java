package org.emonocot.model.description;

import java.util.HashSet;
import java.util.Set;

import javax.persistence.Entity;
import javax.persistence.EnumType;
import javax.persistence.Enumerated;
import javax.persistence.FetchType;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.Lob;
import javax.persistence.ManyToOne;
import javax.persistence.OneToMany;
import javax.persistence.Transient;

import org.apache.commons.lang.ObjectUtils;
import org.codehaus.jackson.annotate.JsonIgnore;
import org.emonocot.model.common.Annotation;
import org.emonocot.model.common.BaseData;
import org.emonocot.model.taxon.Taxon;
import org.hibernate.annotations.Cascade;
import org.hibernate.annotations.CascadeType;
import org.hibernate.annotations.Where;
import org.hibernate.search.annotations.DocumentId;
import org.hibernate.search.annotations.Field;
import org.hibernate.search.annotations.Indexed;

/**
 *
 * @author ben
 *
 */
@Entity
@Indexed
public class TextContent extends BaseData {

    /**
     *
     */
    private static final long serialVersionUID = -177666938449346483L;
    /**
     *
     */
    private String content;
    
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
   */
  private Long id;
  
  /**
  *
  */
 private Set<Annotation> annotations = new HashSet<Annotation>();

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

    /**
     *
     * @param newContent Set the content of this object.
     */
    public void setContent(String newContent) {
        this.content = newContent;
    }

    /**
     *
     * @return the content as a string
     */
    @Lob
    @Field
    public String getContent() {
        return content;
    }

    @Override
    public boolean equals(Object other) {
        if (!super.equals(other)) {
            return false;
        }
     
        TextContent otherContent = (TextContent) other;
        return ObjectUtils.equals(this.feature, otherContent.feature)
                && ObjectUtils.equals(this.taxon, otherContent.taxon)
                && ObjectUtils.equals(this.content, otherContent.content);
    }

    @Override
    public int hashCode() {
        return super.hashCode() + ObjectUtils.hashCode(this.taxon)
        + ObjectUtils.hashCode(this.feature) +(this.content == null ? 0 : this.content.hashCode());
    }

    @Transient
    @JsonIgnore
    public final String getClassName() {
      return "TextContent";
    }

    /**
     * @return the annotations
     */
    @OneToMany(fetch = FetchType.LAZY, orphanRemoval = true)
    @JoinColumn(name = "annotatedObjId")
    @Where(clause = "annotatedObjType = 'TextContent'")
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
