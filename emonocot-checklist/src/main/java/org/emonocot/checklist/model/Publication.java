package org.emonocot.checklist.model;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.Table;

import org.hibernate.annotations.Type;
import org.hibernate.annotations.TypeDef;

/**
 *
 * @author ben
 *
 */
@Entity
@Table(name = "Publication")
@TypeDef(name = "publicationTypeUserType",
        typeClass = PublicationTypeUserType.class)
public class Publication {
    /**
    *
    */
   public static final String IDENTIFIER_PREFIX
       = "urn:kew.org:wcs:publication:";

   /**
    *
    */
   @Id
   @Column(name = "Publication_id")
   private Integer id;

   /**
    *
    */
   @Column(name = "Full_title")
   private String title;

   /**
    *
    */
   @Column(name = "Author")
   private String author;

   /**
    *
    */
   @Column(name = "Published")
   private String publisher;

   /**
    *
    */
   @Column(name = "Publication_type_id")
   @Type(type = "publicationTypeUserType")
   private PublicationType type;

   /**
    *
    */
   public Publication() { }

   /**
    *
    * @param newTitle set the title
    * @param newAuthor set the author
    * @param newPublisher set the publisher
    * @param newType set the type
    * @param newId set the id
    */
    public Publication(final String newTitle, final String newAuthor,
            final String newPublisher, final PublicationType newType,
            final Integer newId) {
        this.title = newTitle;
        this.author = newAuthor;
        this.publisher = newPublisher;
        this.type = newType;
        this.id = newId;
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
     * @return the publisher
     */
    public final String getPublisher() {
        return publisher;
    }

    /**
     * @param newPublisher
     *            the publisher to set
     */
    public final void setPublisher(final String newPublisher) {
        this.publisher = newPublisher;
    }

    /**
     * @return the type
     */
    public final PublicationType getType() {
        return type;
    }

    /**
     * @param newType
     *            the type to set
     */
    public final void setType(final PublicationType newType) {
        this.type = newType;
    }

    /**
     * @return the identifier for this object.
     */
    public final String getIdentifier() {
        return Publication.IDENTIFIER_PREFIX + this.id;
    }
}
