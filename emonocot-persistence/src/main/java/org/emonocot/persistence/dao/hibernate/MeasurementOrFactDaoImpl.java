package org.emonocot.persistence.dao.hibernate;

import java.util.HashMap;
import java.util.Map;

import org.emonocot.model.MeasurementOrFact;
import org.emonocot.model.hibernate.Fetch;
import org.emonocot.persistence.dao.MeasurementOrFactDao;
import org.hibernate.FetchMode;
import org.springframework.stereotype.Repository;

@Repository
public class MeasurementOrFactDaoImpl extends DaoImpl<MeasurementOrFact>
		implements MeasurementOrFactDao {
	
	private static Map<String, Fetch[]> FETCH_PROFILES;
	
	static {
	       FETCH_PROFILES = new HashMap<String, Fetch[]>();
	       FETCH_PROFILES.put("object-with-annotations", new Fetch[] {
		       		new Fetch("taxon", FetchMode.JOIN),
		          	new Fetch("annotations", FetchMode.SELECT)});
	}

	public MeasurementOrFactDaoImpl() {
		super(MeasurementOrFact.class);
	}

	@Override
	protected Fetch[] getProfile(String profile) {
		return MeasurementOrFactDaoImpl.FETCH_PROFILES.get(profile);
	}

	
}
