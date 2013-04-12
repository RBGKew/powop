package org.emonocot.persistence.dao.hibernate;

import java.util.HashMap;
import java.util.Map;

import org.emonocot.model.IdentificationKey;
import org.emonocot.model.hibernate.Fetch;
import org.emonocot.persistence.dao.IdentificationKeyDao;
import org.hibernate.FetchMode;
import org.springframework.stereotype.Repository;

/**
 * @author jk00kg
 */
@Repository
public class IdentificationKeyDaoImpl extends
        DaoImpl<IdentificationKey> implements IdentificationKeyDao {

    /**
     *
     */
    private static Map<String, Fetch[]> FETCH_PROFILES;

    static {
        FETCH_PROFILES = new HashMap<String, Fetch[]>();
        FETCH_PROFILES.put("object-page", new Fetch[] {
                new Fetch("taxa", FetchMode.SELECT),
                new Fetch("authority", FetchMode.SELECT),
                new Fetch("sources", FetchMode.SELECT)});
        FETCH_PROFILES.put("front-cover", new Fetch[] {
                new Fetch("taxa", FetchMode.SELECT)});
    }

    /**
     *
     */
    public IdentificationKeyDaoImpl() {
        super(IdentificationKey.class);
    }

    @Override
    public final Fetch[] getProfile(final String profile) {
        return FETCH_PROFILES.get(profile);
    }

}
