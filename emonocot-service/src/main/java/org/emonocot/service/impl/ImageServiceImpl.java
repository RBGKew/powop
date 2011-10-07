package org.emonocot.service.impl;

import org.emonocot.api.ImageService;
import org.emonocot.model.media.Image;
import org.emonocot.persistence.dao.ImageDao;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

/**
 *
 * @author ben
 *
 */
@Service
public class ImageServiceImpl extends ServiceImpl<Image, ImageDao> implements
        ImageService {
    
    @Autowired
    public void setImageDao(ImageDao imageDao) {
        super.dao = imageDao;
    }

}
