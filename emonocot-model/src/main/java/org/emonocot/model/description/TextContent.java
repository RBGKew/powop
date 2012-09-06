package org.emonocot.model.description;

import java.util.HashSet;
import java.util.Set;
import java.util.UUID;

import javax.persistence.Entity;
import javax.persistence.EnumType;
import javax.persistence.Enumerated;
import javax.persistence.FetchType;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.JoinTable;
import javax.persistence.Lob;
import javax.persistence.ManyToMany;
import javax.persistence.ManyToOne;
import javax.persistence.OneToMany;
import javax.persistence.Transient;

import org.apache.commons.lang.ObjectUtils;
import org.codehaus.jackson.annotate.JsonBackReference;
import org.codehaus.jackson.annotate.JsonIgnore;
import org.codehaus.jackson.map.annotate.JsonDeserialize;
import org.codehaus.jackson.map.annotate.JsonSerialize;
import org.emonocot.model.common.Annotation;
import org.emonocot.model.common.BaseData;
import org.emonocot.model.marshall.json.ReferenceDeserializer;
import org.emonocot.model.marshall.json.ReferenceSerializer;
import org.emonocot.model.reference.Reference;
import org.emonocot.model.taxon.Taxon;
import org.hibernate.annotations.Cascade;
import org.hibernate.annotations.CascadeType;
import org.hibernate.annotations.Where;
import org.hibernate.search.annotations.ContainedIn;
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
     */
    private Set<Reference> references = new HashSet<Reference>();

    /**
     *
     */
    public TextContent() {
        this.identifier = UUID.randomUUID().toString();
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
     * @param newTaxon
     *            Set the taxon that this content is about.
     */
    @JsonBackReference("content-taxon")
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
    @ManyToOne(fetch = FetchType.LAZY)
    @ContainedIn
    @JsonBackReference("content-taxon")
    public Taxon getTaxon() {
        return taxon;
    }

    /**
     *
     * @param newContent
     *            Set the content of this object.
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

    /**
     * @return the references
     */
    @ManyToMany(fetch = FetchType.LAZY)
    @Cascade({ CascadeType.SAVE_UPDATE })
    @JoinTable(name = "TextContent_Reference", joinColumns = { @JoinColumn(name = "TextContent_id") }, inverseJoinColumns = { @JoinColumn(name = "references_id") })
    @JsonSerialize(contentUsing = ReferenceSerializer.class)
    public Set<Reference> getReferences() {
        return references;
    }

    /**
     * @param newReferences the references to set
     */
    @JsonDeserialize(contentUsing = ReferenceDeserializer.class)
    public void setReferences(Set<Reference> newReferences) {
        this.references = newReferences;
    }
}
