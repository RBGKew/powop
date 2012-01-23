package org.emonocot.checklist.model;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.Table;

/**
 *
 * @author ben
 *
 */
@Entity
@Table(name = "Publication_Edition")
public class Article {
   /**
    *
    */
   public static final String IDENTIFIER_PREFIX
       = "urn:kew.org:wcs:publicationEdition:";

   /**
    *
    */
   @Id
   @Column(name = "Publication_edition_id")
   private Integer id;

   /**
    *
    */
   @Column(name = "Article_author")
   private String author;

   /**
    *
    */
   @Column(name = "Published_date")
   private String publishedDate;

   /**
    *
    */
   @Column(name = "Volume")
   private String volume;

   /**
    *
    */
   @Column(name = "Page_number_from")
   private String pageFrom;

   /**
    *
    */
   @Column(name = "Page_number_to")
   private String pageTo;

   /**
    *
    */
   @Column(name = "Article_title")
   private String title;

   /**
    *
    */
   @ManyToOne(fetch = FetchType.EAGER)
   @JoinColumn(name = "Publication_id")
   private Publication publication;

   /**
    *
    */
   public Article() { }

   /**
    *
    * @param newAuthor Set the author of the article
 * @param newTitle Set the title of the article
 * @param newPublication Set the parent publication of this one
 * @param newId Set the id of this publication
 * @param newVolume Set the volume of the publication
 * @param newPageFrom Set the first page of the publication
 * @param newPageTo Set the last page of the publication
 * @param newPublishedDate Set the published date
    */
    public Article(final String newAuthor, final String newTitle,
            final Publication newPublication, final Integer newId,
            final String newVolume, final String newPageFrom,
            final String newPageTo, final String newPublishedDate) {
        this.author = newAuthor;
        this.title = newTitle;
        this.publication = newPublication;
        this.id = newId;
        this.volume = newVolume;
        this.pageFrom = newPageFrom;
        this.pageTo = newPageTo;
        this.publishedDate = newPublishedDate;
    }

    /**
     * @return the author
     */
    public final String getAuthor() {
        return author;
    }

    /**
     * @param newAuthor
     *            the author to set
     */
    public final void setAuthor(final String newAuthor) {
        this.author = newAuthor;
    }

    /**
     * @return the title
     */
    public final String getTitle() {
        return title;
    }

    /**
     * @param newTitle
     *            the title to set
     */
    public final void setTitle(final String newTitle) {
        this.title = newTitle;
    }

    /**
     * @return the publication
     */
    public final Publication getPublication() {
        return publication;
    }

    /**
     * @param newPublication
     *            the publication to set
     */
    public final void setPublication(final Publication newPublication) {
        this.publication = newPublication;
    }

    /**
     * @return the identifier for this object.
     */
    public final String getIdentifier() {
        return Article.IDENTIFIER_PREFIX + this.id;
    }

    /**
     *
     * @param newPublishedDate Set the published date
     */
    public final void setPublishedDate(final String newPublishedDate) {
        this.publishedDate = newPublishedDate;
    }

    /**
     *
     * @return the published date
     */
    public final String getPublishedDate() {
        return publishedDate;
    }

    /**
     *
     * @param newVolume Set the volume
     */
    public final void setVolume(final String newVolume) {
        this.volume = newVolume;
    }

    /**
     *
     * @return the volume
     */
    public final String getVolume() {
        return volume;
    }

    /**
     * @return the pageFrom
     */
    public final String getPageFrom() {
        return pageFrom;
    }

    /**
     * @param newPageFrom the pageFrom to set
     */
    public final void setPageFrom(final String newPageFrom) {
        this.pageFrom = newPageFrom;
    }

    /**
     * @return the pageTo
     */
    public final String getPageTo() {
        return pageTo;
    }

    /**
     * @param newPageTo the pageTo to set
     */
    public final void setPageTo(final String newPageTo) {
        this.pageTo = newPageTo;
    }
}
