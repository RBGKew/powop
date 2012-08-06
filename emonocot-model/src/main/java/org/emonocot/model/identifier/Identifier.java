package org.emonocot.model.identifier;

import java.util.HashSet;
import java.util.Set;

import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.Lob;
import javax.persistence.ManyToOne;
import javax.persistence.OneToMany;
import javax.persistence.Transient;

import org.codehaus.jackson.annotate.JsonBackReference;
import org.codehaus.jackson.annotate.JsonIgnore;
import org.emonocot.model.common.Annotation;
import org.emonocot.model.common.BaseData;
import org.emonocot.model.taxon.Taxon;
import org.hibernate.annotations.Cascade;
import org.hibernate.annotations.CascadeType;
import org.hibernate.annotations.Where;
import org.hibernate.search.annotations.DocumentId;
import org.hibernate.search.annotations.Field;
import org.hibernate.search.annotations.Fields;
import org.hibernate.search.annotations.Index;
import org.hibernate.search.annotations.Indexed;

/**
 *
 * @author ben
 *
 */
@Entity
@Indexed
public class Identifier extends BaseData {
    /**
     *
     */
    private static final long serialVersionUID = 8866662399880411558L;

    /**
     *
     */
    private String title;

    /**
     *
     */
    private String subject;

    /**
     *
     */
    private String format;

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
    private Set<Annotation> annotations = new HashSet<Annotation>();

    /**
     *
     */
    private Long id;

    /**
    *
    * @return the content
    */
   @Lob
   @Field
   public String getContent() {
       return content;
   }

   /**
    *
    * @param content Set the content
    */
   public void setContent(String content) {
       this.content = content;
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
    * @return the subject of this identifier
    */
   @Field
   public String getSubject() {
       return subject;
   }

   /**
    *
    * @param subject Set the subject of this identifier
    */
   public void setSubject(String subject) {
       this.subject = subject;
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
     * @return the title
     */
    @Fields({ @Field, @Field(name = "label", index = Index.UN_TOKENIZED) })
    public String getTitle() {
        return title;
    }

    /**
     *
     * @param newTitle
     *            Set the title
     */
    public void setTitle(String newTitle) {
        this.title = newTitle;
    }

    /**
     * The taxon (page) this identifier is related to.
     *
     * @return the taxon this image is of
     */
    @ManyToOne(fetch = FetchType.LAZY)
    @JsonBackReference("identifier-taxon")
    public Taxon getTaxon() {
        return taxon;
    }

    /**
     *
     * @param taxon
     *            Set the taxon this image is of
     */
    @JsonBackReference("identifier-taxon")
    public void setTaxon(Taxon taxon) {
        this.taxon = taxon;
    }

    /**
     *
     * @return the name of this class
     */
    @Transient
    @JsonIgnore
    public String getClassName() {
        return "Identifier";
    }

    /**
     * @return the annotations
     */
    @OneToMany(fetch = FetchType.LAZY, orphanRemoval = true)
    @JoinColumn(name = "annotatedObjId")
    @Where(clause = "annotatedObjType = 'Identifier'")
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
