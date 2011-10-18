package org.emonocot.model.reference;

import java.util.HashSet;
import java.util.Set;

import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.ManyToMany;
import javax.persistence.Transient;

import org.codehaus.jackson.annotate.JsonIgnore;
import org.emonocot.model.common.BaseData;
import org.emonocot.model.hibernate.DatePublishedBridge;
import org.emonocot.model.taxon.Taxon;
import org.hibernate.annotations.Cascade;
import org.hibernate.annotations.CascadeType;
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
    private static final long serialVersionUID = -5928234699377084008L;

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
    private String citation;

    /**
     *
     */
    private Set<Taxon> taxa = new HashSet<Taxon>();

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
     * @param author Set the author
     */
    public void setAuthor(String author) {
        this.author = author;
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
     * @param publishedIn Set the publication this reference was published in
     */
    public void setPublishedIn(String publishedIn) {
        this.publishedIn = publishedIn;
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
     * @param citation Set the full citation
     */
    public void setCitation(String citation) {
        this.citation = citation;
    }

    /**
     *
     * @param newTitle set the title
     */
    public void setTitle(String newTitle) {
        this.title = newTitle;
    }

    /**
     *
     * @param newVolume set the volume
     */
    public void setVolume(String newVolume) {
        this.volume = newVolume;
    }

    /**
     *
     * @param newPages set the pages
     */
    public void setPages(String newPages) {
        this.pages = newPages;
    }

    /**
     *
     * @param newDatePublished set the date published
     */
    public void setDate(String newDatePublished) {
        this.datePublished = newDatePublished;
    }

    /**
     *
     * @param newReferenceType set the reference type
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
     * @param newDatePublished the datePublished to set
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
    @Cascade({CascadeType.SAVE_UPDATE })
    public Set<Taxon> getTaxa() {
        return taxa;
    }

    /**
     *
     * @param taxa Set the taxa associated with this reference
     */
    public void setTaxa(Set<Taxon> taxa) {
        this.taxa = taxa;
    }

    @Transient
    @JsonIgnore
    public final String getClassName() {
      return "Reference";
    }
}
