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
@Table(name = "Publication_edition")
public class Article {

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
    */
    public Article(final String newAuthor, final String newTitle,
            final Publication newPublication) {
        this.author = newAuthor;
        this.title = newTitle;
        this.publication = newPublication;
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

}
