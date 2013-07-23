/**
 *
 */
package org.emonocot.persistence.dao.hibernate;

import java.util.HashMap;
import java.util.Map;

import org.emonocot.model.SearchableObject;
import org.emonocot.model.hibernate.Fetch;
import org.emonocot.persistence.dao.SearchableObjectDao;
import org.hibernate.FetchMode;
import org.springframework.stereotype.Repository;

/**
 * @author jk00kg
 */
@Repository
public class SearchableObjectDaoImpl extends
        SearchableDaoImpl<SearchableObject> implements SearchableObjectDao {

    /**
     *
     */
    private static Map<String, Fetch[]> FETCH_PROFILES;

    static {
        FETCH_PROFILES = new HashMap<String, Fetch[]>();
        FETCH_PROFILES.put("taxon-with-image", new Fetch[] {
                new Fetch("image", FetchMode.SELECT),
                new Fetch("acceptedNameUsage", FetchMode.SELECT),
                new Fetch("taxon", FetchMode.SELECT)});
    }

    /**
     *
     */
    public SearchableObjectDaoImpl() {
        super(SearchableObject.class);
    }

    @Override
    protected final Fetch[] getProfile(final String profile) {
        return SearchableObjectDaoImpl.FETCH_PROFILES.get(profile);
    }
}
