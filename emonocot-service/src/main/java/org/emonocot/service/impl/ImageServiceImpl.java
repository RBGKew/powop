package org.emonocot.service.impl;

import org.emonocot.model.media.Image;
import org.emonocot.persistence.dao.ImageDao;
import org.emonocot.service.ImageService;
import org.springframework.stereotype.Service;

/**
 *
 * @author ben
 *
 */
@Service
public class ImageServiceImpl extends ServiceImpl<Image, ImageDao> implements
        ImageService {

}
