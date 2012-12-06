package org.emonocot.persistence.dao.hibernate;

import java.util.HashMap;
import java.util.Map;

import org.emonocot.model.Description;
import org.emonocot.model.hibernate.Fetch;
import org.emonocot.persistence.dao.DescriptionDao;
import org.hibernate.FetchMode;
import org.springframework.stereotype.Repository;

@Repository
public class DescriptionDaoImpl extends DaoImpl<Description> implements
		DescriptionDao {
	
  /**
   *
   */
   private static Map<String, Fetch[]> FETCH_PROFILES;

   static {
       FETCH_PROFILES = new HashMap<String, Fetch[]>();
       FETCH_PROFILES.put("object-with-annotations", new Fetch[] {
    		new Fetch("taxon", FetchMode.JOIN),
       		new Fetch("annotations", FetchMode.SELECT),
       		new Fetch("references", FetchMode.SELECT)});
   }

	public DescriptionDaoImpl() {
		super(Description.class);
	}

	@Override
	protected Fetch[] getProfile(String profile) {
		return DescriptionDaoImpl.FETCH_PROFILES.get(profile);
	}
}
