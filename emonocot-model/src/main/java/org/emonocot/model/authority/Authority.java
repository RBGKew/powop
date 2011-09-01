package org.emonocot.model.authority;

import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;

import org.hibernate.search.annotations.DocumentId;
import org.hibernate.search.annotations.Field;
import org.hibernate.search.annotations.Indexed;

/**
 * Class that represents the authority an object is harvested from.
 *
 * @author ben
 *
 */
@Entity
@Indexed
public class Authority {

   /**
    *
    */
   private Long id;
   
   /**
    *
    */
   private String name;
   
   /**
    *
    */
   private String uri;

    /**
     * @return the id
     */
   @Id
   @GeneratedValue(generator = "system-increment")
   @DocumentId
    public final Long getId() {
        return id;
    }

    /**
     * @param id
     *            the id to set
     */
    public final void setId(Long id) {
        this.id = id;
    }

    /**
     * @return the name
     */
    @Field
    public final String getName() {
        return name;
    }

    /**
     * @param name
     *            the name to set
     */
    public final void setName(String name) {
        this.name = name;
    }

    /**
     * @return the uri
     */
    public final String getUri() {
        return uri;
    }

    /**
     * @param uri the uri to set
     */
    public final void setUri(String uri) {
        this.uri = uri;
    }
}
