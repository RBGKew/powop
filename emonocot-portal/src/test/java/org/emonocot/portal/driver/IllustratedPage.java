package org.emonocot.portal.driver;

/**
 *
 * @author ben
 *
 */
public interface IllustratedPage {

    /**
     *
     * @param property TODO
     * @return the main image caption
     */
    String getMainImageProperty(String property);

    /**
     *
     * @return the main image url
     */
    String getMainImage();

    /**
     *
     * @return the number of thumbnails
     */
    int getThumbnails();

}
