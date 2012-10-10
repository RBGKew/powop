package org.emonocot.persistence.dao.hibernate;

import java.util.HashMap;
import java.util.Map;

import org.emonocot.model.Description;
import org.emonocot.model.hibernate.Fetch;
import org.emonocot.persistence.dao.TextContentDao;
import org.hibernate.FetchMode;
import org.springframework.stereotype.Repository;

@Repository
public class TextContentDaoImpl extends DaoImpl<Description> implements
		TextContentDao {
	
  /**
   *
   */
   private static Map<String, Fetch[]> FETCH_PROFILES;

   static {
       FETCH_PROFILES = new HashMap<String, Fetch[]>();
       FETCH_PROFILES.put("text-content-with-related", new Fetch[] {
    		new Fetch("taxon", FetchMode.JOIN),
       		new Fetch("annotations", FetchMode.SELECT),
       		new Fetch("references", FetchMode.SELECT)});
   }

	public TextContentDaoImpl() {
		super(Description.class);
	}

	@Override
	protected Fetch[] getProfile(String profile) {
		return TextContentDaoImpl.FETCH_PROFILES.get(profile);
	}


}
