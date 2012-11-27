package org.emonocot.persistence.dao.hibernate;

import java.util.HashMap;
import java.util.Map;

import org.emonocot.model.Identifier;
import org.emonocot.model.hibernate.Fetch;
import org.emonocot.persistence.dao.IdentifierDao;
import org.springframework.stereotype.Repository;

@Repository
public class IdentifierDaoImpl extends DaoImpl<Identifier> implements
		IdentifierDao {

	private static Map<String, Fetch[]> FETCH_PROFILES;
	
	static {
	       FETCH_PROFILES = new HashMap<String, Fetch[]>();
	}

	public IdentifierDaoImpl() {
		super(Identifier.class);
	}

	@Override
	protected Fetch[] getProfile(String profile) {
		return IdentifierDaoImpl.FETCH_PROFILES.get(profile);
	}

}
