package org.emonocot.portal.remoting;

import org.emonocot.model.Description;
import org.emonocot.persistence.dao.TextContentDao;
import org.springframework.stereotype.Repository;

@Repository
public class TextContentDaoImpl extends DaoImpl<Description> implements
		TextContentDao {

	public TextContentDaoImpl() {
		super(Description.class, "description");
	}

	
}
