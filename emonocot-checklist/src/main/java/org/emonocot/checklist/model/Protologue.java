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
@Table(name = "Place_of_publication")
public class Protologue {

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
     */
    public Protologue(final String newTitle) {
        this.title = newTitle;
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
}
