package org.emonocot.portal.remoting;

import org.emonocot.model.Description;
import org.emonocot.persistence.dao.DescriptionDao;
import org.springframework.stereotype.Repository;

@Repository
public class DescriptionDaoImpl extends DaoImpl<Description> implements
		DescriptionDao {

	public DescriptionDaoImpl() {
		super(Description.class, "description");
	}

	
}
