package org.emonocot.persistence.dao.hibernate;

import java.util.HashMap;
import java.util.Map;

import org.emonocot.model.geography.Place;
import org.emonocot.model.hibernate.Fetch;
import org.emonocot.persistence.dao.PlaceDao;
import org.springframework.stereotype.Repository;

/**
 *
 * @author jk00kg
 *
 */
@Repository
public class PlaceDaoImpl extends SearchableDaoImpl<Place> implements PlaceDao {

   /**
    *
    */
    private static Map<String, Fetch[]> FETCH_PROFILES;

    static {
        FETCH_PROFILES = new HashMap<String, Fetch[]>();

    }

    /**
     *
     */
    public PlaceDaoImpl() {
        super(Place.class);
    }

    /**
     * @param profile
     *            Set the profile name
     * @return an array of related objects to fetch
     */
    @Override
    protected final Fetch[] getProfile(final String profile) {
        return new Fetch[]{};
    }
}
