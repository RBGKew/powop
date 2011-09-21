package org.emonocot.portal.remoting;

import org.emonocot.model.media.Image;
import org.emonocot.persistence.dao.ImageDao;
import org.springframework.stereotype.Repository;

/**
 *
 * @author ben
 *
 */
@Repository
public class ImageDaoImpl extends DaoImpl<Image> implements ImageDao {

    /**
     *
     */
    public ImageDaoImpl() {
        super(Image.class, "image");
    }

}
