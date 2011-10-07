package org.emonocot.service.impl;

import org.emonocot.api.DescriptionService;
import org.emonocot.model.description.Content;
import org.emonocot.model.description.Feature;
import org.emonocot.model.description.TextContent;
import org.emonocot.model.taxon.Taxon;
import org.emonocot.persistence.dao.DescriptionDao;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

/**
 *
 * @author ben
 *
 */
@Service
public class DescriptionServiceImpl extends ServiceImpl<Content, DescriptionDao> implements
        DescriptionService {

    @Transactional(readOnly = true)
    public final TextContent getTextContent(final Feature feature,
            final Taxon taxon) {
        return dao.getTextContent(feature, taxon);
    }

    @Autowired
    public void setDescriptionDao(DescriptionDao descriptionDao) {
        this.dao = descriptionDao;
    }

}
