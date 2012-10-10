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

    /**
     * @param url Set the url
     * @return an image or null if one doesn't exist
     */
    public final Image findByUrl(final String url) {
        // TODO Auto-generated method stub
        return null;
    }

    public Page<Image> searchByExample(Image example, boolean ignoreCase,
            boolean useLike) {
        throw new UnsupportedOperationException("Remote searching by example is unimplemented");
    }

}
