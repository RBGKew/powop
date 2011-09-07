package org.emonocot.model.authority;

import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;

import org.apache.commons.lang.ObjectUtils;
import org.emonocot.model.common.Base;
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
    public Long getId() {
        return id;
    }

    /**
     * @param id
     *            the id to set
     */
    public void setId(Long id) {
        this.id = id;
    }

    /**
     * @return the name
     */
    @Field
    public String getName() {
        return name;
    }

    /**
     * @param name
     *            the name to set
     */
    public void setName(String name) {
        this.name = name;
    }

    /**
     * @return the uri
     */
    public String getUri() {
        return uri;
    }

    /**
     * @param uri the uri to set
     */
    public void setUri(String uri) {
        this.uri = uri;
    }

    @Override
    public boolean equals(Object other) {
        // check for self-comparison
        if (this == other) {
            return true;
        }
        if (other == null || other.getClass() != this.getClass()) {
            return false;
        }
        Authority authority = (Authority) other;
        return ObjectUtils.equals(this.id, authority.id);
    }

    @Override
    public int hashCode() {
        return ObjectUtils.hashCode(this.id);
    }
}
