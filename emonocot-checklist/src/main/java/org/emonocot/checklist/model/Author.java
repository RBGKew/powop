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
@Table(name = "Authors")
public class Author {

   /**
    *
    */
   @Id
   @Column(name = "Author_id")
   private Integer id;

    /**
     *
     */
   @Column(name = "Author")
   private String name;

   /**
    * No-arg constructor required by hibernate.
    */
   public Author() { }

   /**
    *
    * @param newName set the name of the author
    */
    public Author(final String newName) {
        this.name = newName;
    }

    /**
     * @return the name
     */
    public final String getName() {
        return name;
    }

    /**
     * @param newName the name to set
     */
    public final void setName(final String newName) {
        this.name = newName;
    }

}
