package org.emonocot.service.impl;

import org.emonocot.api.DescriptionService;
import org.emonocot.model.Description;
import org.emonocot.persistence.dao.DescriptionDao;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class DescriptionServiceImpl extends
		ServiceImpl<Description, DescriptionDao> implements DescriptionService {
	
    @Autowired
    public final void setDescriptionDao(final DescriptionDao newTextContentDao) {
        super.dao = newTextContentDao;
    }

}
