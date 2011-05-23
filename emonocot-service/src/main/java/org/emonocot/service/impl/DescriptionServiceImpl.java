package org.emonocot.service.impl;

import org.emonocot.model.description.Content;
import org.emonocot.model.description.Feature;
import org.emonocot.model.description.TextContent;
import org.emonocot.model.taxon.Taxon;
import org.emonocot.persistence.dao.DescriptionDao;
import org.emonocot.service.DescriptionService;
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

    @Override
    @Transactional(readOnly = true)
    public final TextContent getTextContent(final Feature feature,
            final Taxon taxon) {
        return dao.getTextContent(feature, taxon);
    }

    @Override
    @Autowired
    public void setDao(DescriptionDao dao) {
        this.dao = dao;
    }

}
