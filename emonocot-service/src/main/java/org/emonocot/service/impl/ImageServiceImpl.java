package org.emonocot.service.impl;

import org.emonocot.api.ImageService;
import org.emonocot.model.Image;
import org.emonocot.persistence.dao.ImageDao;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

/**
 *
 * @author ben
 *
 */
@Service
public class ImageServiceImpl extends SearchableServiceImpl<Image, ImageDao>
        implements ImageService {

    /**
     *
     * @param newImageDao Set the image dao
     */
    @Autowired
    public final void setImageDao(final ImageDao newImageDao) {
        super.dao = newImageDao;
    }
}
