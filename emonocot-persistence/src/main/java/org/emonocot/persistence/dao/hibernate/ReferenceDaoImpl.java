package org.emonocot.persistence.dao.hibernate;

import java.util.HashMap;
import java.util.Map;

import org.emonocot.model.Reference;
import org.emonocot.model.hibernate.Fetch;
import org.emonocot.persistence.dao.ReferenceDao;
import org.hibernate.Criteria;
import org.hibernate.FetchMode;
import org.hibernate.criterion.Restrictions;
import org.springframework.stereotype.Repository;

/**
 *
 * @author ben
 *
 */
@Repository
public class ReferenceDaoImpl extends DaoImpl<Reference> implements
        ReferenceDao {
   /**
    *
    */
    private static Map<String, Fetch[]> FETCH_PROFILES;

    static {
        FETCH_PROFILES = new HashMap<String, Fetch[]>();
        FETCH_PROFILES.put("reference-with-taxa", new Fetch[] {
                new Fetch("taxa", FetchMode.SELECT)
                });
    }

    /**
     *
     */
    public ReferenceDaoImpl() {
        super(Reference.class);
    }

    /**
     * @param profile
     *            Set the profile name
     * @return an array of related objects to fetch
     */
    @Override
    protected final Fetch[] getProfile(final String profile) {
        return ReferenceDaoImpl.FETCH_PROFILES.get(profile);
    }

    /**
     * @param source The source of the reference you want to find
     * @return a reference or null if it does not exist
     */
    public final Reference findByBibliographicCitation(final String bibliographicCitation) {
        Criteria criteria = getSession().createCriteria(type).add(Restrictions.eq("bibliographicCitation", bibliographicCitation));
        return (Reference) criteria.uniqueResult();
    }
}
