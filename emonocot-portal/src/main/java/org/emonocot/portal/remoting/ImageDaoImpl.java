package org.emonocot.portal.remoting;

import org.emonocot.model.Image;
import org.emonocot.pager.Page;
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

    public Page<Image> searchByExample(Image example, boolean ignoreCase,
            boolean useLike) {
        throw new UnsupportedOperationException("Remote searching by example is unimplemented");
    }

}
