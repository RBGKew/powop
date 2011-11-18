package org.emonocot.portal.driver;

/**
 *
 * @author ben
 *
 */
public interface IllustratedPage {

    /**
     *
     * @return the main image caption
     */
    String getMainImageCaption();

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
