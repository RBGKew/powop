package org.emonocot.persistence.dao.hibernate;

import java.util.HashMap;
import java.util.Map;

import org.emonocot.model.hibernate.Fetch;
import org.emonocot.model.registry.Organisation;
import org.emonocot.persistence.dao.OrganisationDao;
import org.hibernate.FetchMode;
import org.springframework.stereotype.Repository;

/**
 *
 * @author ben
 *
 */
@Repository
public class OrganisationDaoImpl extends DaoImpl<Organisation> implements
        OrganisationDao {
    /**
    *
    */
    private static Map<String, Fetch[]> FETCH_PROFILES;

    static {
        FETCH_PROFILES = new HashMap<String, Fetch[]>();
        FETCH_PROFILES.put("source-with-jobs", new Fetch[] {new Fetch(
                "jobs", FetchMode.SELECT)});
    }

    /**
     *
     */
    public OrganisationDaoImpl() {
        super(Organisation.class);
    }

    /**
     * @param profile
     *            Set the profile name
     * @return an array of related objects to fetch
     */
    protected final Fetch[] getProfile(final String profile) {
        return OrganisationDaoImpl.FETCH_PROFILES.get(profile);
    }
}
