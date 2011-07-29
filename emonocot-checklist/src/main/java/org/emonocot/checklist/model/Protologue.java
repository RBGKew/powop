package org.emonocot.checklist.model;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.Table;

/**
 *
 * @author ben
 *
 */
@Entity
@Table(name = "Place_of_Publication")
public class Protologue {
   /**
    *
    */
   public static final String IDENTIFIER_PREFIX
       = "urn:kew.org:wcs:placeOfPublication:";

   /**
    *
    */
   @Id
   @Column(name = "Place_of_publication_id")
   private Integer id;

   /**
    *
    */
    @Column(name = "Place_of_publication")
    private String title;

    /**
     *
     */
    public Protologue() { }

    /**
     *
     * @param newTitle the title of the protologue
     * @param newId the Id of the protologue
     */
    public Protologue(final String newTitle, final Integer newId) {
        this.title = newTitle;
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
     * @return the identifier for this object.
     */
    public final String getIdentifier() {
        return Protologue.IDENTIFIER_PREFIX + this.id;
    }
}
