package org.powo.persistence.dao.hibernate;

import java.util.HashMap;
import java.util.Map;

import org.hibernate.FetchMode;
import org.powo.model.Identification;
import org.powo.model.hibernate.Fetch;
import org.powo.persistence.dao.IdentificationDao;
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