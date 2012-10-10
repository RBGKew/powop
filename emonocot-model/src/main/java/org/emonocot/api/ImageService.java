package org.emonocot.api;

import org.emonocot.model.Image;

/**
 *
 * @author ben
 *
 */
public interface ImageService extends SearchableService<Image> {

    /**
     *
     * @param url Set the Url
     * @return an image or null if one doesn't exist
     */
    Image findByUrl(String url);

}
