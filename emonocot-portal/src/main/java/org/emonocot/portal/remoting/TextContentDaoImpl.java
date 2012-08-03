package org.emonocot.portal.remoting;

import org.emonocot.model.description.TextContent;
import org.emonocot.persistence.dao.TextContentDao;
import org.springframework.stereotype.Repository;

@Repository
public class TextContentDaoImpl extends DaoImpl<TextContent> implements
		TextContentDao {

	public TextContentDaoImpl() {
		super(TextContent.class, "description");
	}

	
}
