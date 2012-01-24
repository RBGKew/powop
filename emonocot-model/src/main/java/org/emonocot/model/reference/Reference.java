package org.emonocot.model.reference;

import java.util.HashSet;
import java.util.Set;

import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.Lob;
import javax.persistence.ManyToMany;
import javax.persistence.OneToMany;
import javax.persistence.Transient;

import org.codehaus.jackson.annotate.JsonIgnore;
import org.emonocot.model.common.Annotation;
import org.emonocot.model.common.BaseData;
import org.emonocot.model.hibernate.DatePublishedBridge;
import org.emonocot.model.taxon.Taxon;
import org.hibernate.annotations.Cascade;
import org.hibernate.annotations.CascadeType;
import org.hibernate.annotations.Where;
import org.hibernate.search.annotations.DocumentId;
import org.hibernate.search.annotations.Field;
import org.hibernate.search.annotations.FieldBridge;
import org.hibernate.search.annotations.Indexed;

/**
 *
 * @author ben
 *
 */
@Entity
@Indexed
public class Reference extends BaseData {

    /**
     *
     */
    private static long serialVersionUID = -5928234699377084008L;

    /**
     *
     */
    private String title;

    /**
     *
     */
    private String volume;

    /**
     *
     */
    private String pages;

    /**
     *
     */
    private String datePublished;

    /**
     *
     */
    private ReferenceType type;

    /**
     *
     */
    private String author;

    /**
     *
     */
    private String publishedIn;

    /**
     *
     */
    private String publishedInAuthor;

    /**
     *
     */
    private String citation;

    /**
     *
     */
    private Set<Taxon> taxa = new HashSet<Taxon>();

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
     */
    private String referenceAbstract;

    /**
     *
     */
    private String number;

    /**
     *
     */
    private String keywords;

    /**
     *
     */
    private String publisher;

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
     * @return the author
     */
    @Field
    public String getAuthor() {
        return author;
    }

    /**
     *
     * @param newAuthor
     *            Set the author
     */
    public void setAuthor(String newAuthor) {
        this.author = newAuthor;
    }

    /**
     *
     * @return the publication this reference was published in
     */
    @Field
    public String getPublishedIn() {
        return publishedIn;
    }

    /**
     *
     * @param newPublishedIn
     *            Set the publication this reference was published in
     */
    public void setPublishedIn(String newPublishedIn) {
        this.publishedIn = newPublishedIn;
    }

    /**
     *
     * @return the full citation
     */
    @Field
    public String getCitation() {
        return citation;
    }

    /**
     *
     * @param newCitation
     *            Set the full citation
     */
    public void setCitation(String newCitation) {
        this.citation = newCitation;
    }

    /**
     *
     * @param newTitle
     *            set the title
     */
    public void setTitle(String newTitle) {
        this.title = newTitle;
    }

    /**
     *
     * @param newVolume
     *            set the volume
     */
    public void setVolume(String newVolume) {
        this.volume = newVolume;
    }

    /**
     *
     * @param newPages
     *            set the pages
     */
    public void setPages(String newPages) {
        this.pages = newPages;
    }

    /**
     *
     * @param newDatePublished
     *            set the date published
     */
    public void setDate(String newDatePublished) {
        this.datePublished = newDatePublished;
    }

    /**
     *
     * @param newReferenceType
     *            set the reference type
     */
    public void setType(ReferenceType newReferenceType) {
        this.type = newReferenceType;
    }

    /**
     * @return the datePublished
     */
    @Field(bridge = @FieldBridge(impl = DatePublishedBridge.class))
    public String getDatePublished() {
        return datePublished;
    }

    /**
     * @param newDatePublished
     *            the datePublished to set
     */
    public void setDatePublished(String newDatePublished) {
        this.datePublished = newDatePublished;
    }

    /**
     * @return the title
     */
    @Field
    public String getTitle() {
        return title;
    }

    /**
     * @return the volume
     */
    @Field
    public String getVolume() {
        return volume;
    }

    /**
     * @return the pages
     */
    @Field
    public String getPages() {
        return pages;
    }

    /**
     * @return the type
     */
    public ReferenceType getType() {
        return type;
    }

    /**
     * The list of all taxa associated with this reference.
     *
     * @return a set of taxa
     */
    @ManyToMany(fetch = FetchType.LAZY, mappedBy = "references")
    @Cascade({ CascadeType.SAVE_UPDATE })
    public Set<Taxon> getTaxa() {
        return taxa;
    }

    /**
     *
     * @param taxa
     *            Set the taxa associated with this reference
     */
    public void setTaxa(Set<Taxon> taxa) {
        this.taxa = taxa;
    }

    /**
     * @return the annotations
     */
    @OneToMany(fetch = FetchType.LAZY, orphanRemoval = true)
    @JoinColumn(name = "annotatedObjId")
    @Where(clause = "annotatedObjType = 'Reference'")
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
     * @return the abstract
     */
    @Lob
    @Field
    public String getReferenceAbstract() {
        return referenceAbstract;
    }

    /**
     * @param referenceAbstract the abstract to set
     */
    public void setReferenceAbstract(String referenceAbstract) {
        this.referenceAbstract = referenceAbstract;
    }

    /**
     * @return the number
     */
    @Field
    public String getNumber() {
        return number;
    }

    /**
     * @param number the number to set
     */
    public void setNumber(String number) {
        this.number = number;
    }

    /**
     * @return the keywords
     */
    @Field
    public String getKeywords() {
        return keywords;
    }

    /**
     * @param keywords the keywords to set
     */
    public void setKeywords(String keywords) {
        this.keywords = keywords;
    }

    @Transient
    @JsonIgnore
    public String getClassName() {
        return "Reference";
    }

    /**
     *
     * @param publisher Set the publisher
     */
    public void setPublisher(String publisher) {
        this.publisher = publisher;
    }

    /**
     *
     * @return the publisher
     */
    @Field
    public String getPublisher() {
        return publisher;
    }

    /**
     *
     * @param newPublishedInAuthor Set the published in author
     */
    public void setPublishedInAuthor(String newPublishedInAuthor) {
        this.publishedInAuthor = newPublishedInAuthor;
    }

    /**
     *
     */
    @Field
    public String getPublishedInAuthor() {
        return publishedInAuthor;
    }
}
