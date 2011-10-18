package org.emonocot.model.source;

import java.io.Serializable;

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
public class Source extends Base {

    /**
     *
     */
    private static final long serialVersionUID = -2463044801110563816L;

   /**
    *
    */
   private String uri;

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
}
