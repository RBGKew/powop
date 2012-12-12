package org.emonocot.persistence.dao.hibernate;

import java.util.HashMap;
import java.util.Map;

import org.emonocot.model.VernacularName;
import org.emonocot.model.hibernate.Fetch;
import org.emonocot.persistence.dao.VernacularNameDao;
import org.hibernate.FetchMode;
import org.springframework.stereotype.Repository;

@Repository
public class VernacularNameDaoImpl extends DaoImpl<VernacularName> implements
		VernacularNameDao {

	private static Map<String, Fetch[]> FETCH_PROFILES;
	
	static {
	       FETCH_PROFILES = new HashMap<String, Fetch[]>();
	       FETCH_PROFILES.put("object-with-annotations", new Fetch[] {
	       		new Fetch("taxon", FetchMode.JOIN),
	          	new Fetch("annotations", FetchMode.SELECT)});
	}
	
	public VernacularNameDaoImpl() {
		super(VernacularName.class);
	}

	@Override
	protected Fetch[] getProfile(String profile) {
		return VernacularNameDaoImpl.FETCH_PROFILES.get(profile);
	}

	
}
