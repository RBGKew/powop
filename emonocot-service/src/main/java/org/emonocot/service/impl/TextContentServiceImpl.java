package org.emonocot.service.impl;

import org.emonocot.api.TextContentService;
import org.emonocot.model.description.TextContent;
import org.emonocot.persistence.dao.TextContentDao;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class TextContentServiceImpl extends
		ServiceImpl<TextContent, TextContentDao> implements TextContentService {
	
    @Autowired
    public final void setTextContentDao(final TextContentDao newTextContentDao) {
        super.dao = newTextContentDao;
    }

}
