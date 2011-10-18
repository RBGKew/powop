package org.emonocot.persistence.dao.hibernate;

import org.emonocot.model.hibernate.Fetch;
import org.emonocot.model.source.Source;
import org.emonocot.persistence.dao.SourceDao;
import org.springframework.stereotype.Repository;

/**
 *
 * @author ben
 *
 */
@Repository
public class SourceDaoImpl extends DaoImpl<Source> implements SourceDao {

	public SourceDaoImpl() {
		super(Source.class);
	}

	protected Fetch[] getProfile(String profile) {
		// TODO Auto-generated method stub
		return null;
	}
   

}