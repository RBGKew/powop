package org.emonocot.persistence.dao.hibernate;

import java.util.HashMap;
import java.util.Map;

import org.emonocot.model.Identification;
import org.emonocot.model.hibernate.Fetch;
import org.emonocot.persistence.dao.IdentificationDao;
import org.hibernate.FetchMode;
import org.springframework.stereotype.Repository;

@Repository
public class IdentificationDaoImpl extends DaoImpl<Identification> implements IdentificationDao {

	private static Map<String, Fetch[]> FETCH_PROFILES;

	static {
		FETCH_PROFILES = new HashMap<String, Fetch[]>();
		FETCH_PROFILES.put("object-with-annotations", new Fetch[] {
				new Fetch("taxon", FetchMode.JOIN),
				new Fetch("annotations", FetchMode.SELECT)});
	}

	public IdentificationDaoImpl() {
		super(Identification.class);
	}

	@Override
	protected final Fetch[] getProfile(final String profile) {
		return IdentificationDaoImpl.FETCH_PROFILES.get(profile);
	}
}