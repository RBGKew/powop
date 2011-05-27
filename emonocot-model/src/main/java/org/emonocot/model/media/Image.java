package org.emonocot.model.media;

import javax.persistence.Entity;

import org.emonocot.model.common.Base;
import org.hibernate.search.annotations.Field;
import org.hibernate.search.annotations.Indexed;

/**
 *
 * @author ben
 *
 */
@Entity
@Indexed
public class Image extends Base {
    /**
     *
     */
    private String url;

    /**
     *
     * @param newUrl Set the url of the image
     */
    public void setUrl(String newUrl) {
        this.url = newUrl;
    }

    /**
    *
    * @return the url of the image
    */
    @Field
    public String getUrl() {
        return url;
    }
}
