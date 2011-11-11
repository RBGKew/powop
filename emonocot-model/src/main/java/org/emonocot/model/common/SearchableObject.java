package org.emonocot.model.common;

import javax.persistence.FetchType;
import javax.persistence.ManyToOne;
import javax.persistence.MappedSuperclass;

import org.emonocot.model.media.Image;

/**
 *
 * @author ben
 *
 */
@MappedSuperclass
public abstract class SearchableObject extends BaseData {

    /**
     *
     */
    private Image image;

    /**
     * @return the image
     */
    @ManyToOne(fetch = FetchType.LAZY)
    public Image getImage() {
        return image;
    }

    /**
     * @param image the image to set
     */
    public void setImage(Image image) {
        this.image = image;
    }
}
