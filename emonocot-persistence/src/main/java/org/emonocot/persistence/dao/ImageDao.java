package org.emonocot.persistence.dao;

import org.emonocot.model.media.Image;

/**
 *
 * @author ben
 *
 */
public interface ImageDao extends SearchableDao<Image> {

    /**
     * @param url Set the url
     * @return an image or null if one doesn't exist
     */
    Image findByUrl(String url);

}
