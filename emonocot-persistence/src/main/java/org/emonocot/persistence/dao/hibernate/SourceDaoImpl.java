package org.emonocot.persistence.dao.hibernate;

import java.util.HashMap;
import java.util.Map;

import org.emonocot.model.hibernate.Fetch;
import org.emonocot.model.source.Source;
import org.emonocot.persistence.dao.SourceDao;
import org.hibernate.FetchMode;
import org.springframework.stereotype.Repository;

/**
 *
 * @author ben
 *
 */
@Repository
public class SourceDaoImpl extends DaoImpl<Source> implements SourceDao {
	
	private static Map<String, Fetch[]> FETCH_PROFILES;

	   static {
	       FETCH_PROFILES = new HashMap<String, Fetch[]>();
	       
	   }

    /**
     *
     */
    public SourceDaoImpl() {
        super(Source.class);
    }

    
    @Override
    protected final Fetch[] getProfile(final String profile) {
        return SourceDaoImpl.FETCH_PROFILES.get(profile);
    }

}