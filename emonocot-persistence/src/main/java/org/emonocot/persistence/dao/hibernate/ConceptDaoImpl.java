package org.emonocot.persistence.dao.hibernate;

import java.util.HashMap;
import java.util.Map;

import org.emonocot.model.Concept;
import org.emonocot.model.hibernate.Fetch;
import org.emonocot.persistence.dao.ConceptDao;
import org.hibernate.FetchMode;
import org.springframework.stereotype.Repository;

/**
 *
 * @author ben
 *
 */
@Repository
public class ConceptDaoImpl extends DaoImpl<Concept> implements ConceptDao {

   /**
    *
    */
    private static Map<String, Fetch[]> FETCH_PROFILES;

    static {
        FETCH_PROFILES = new HashMap<String, Fetch[]>();
        FETCH_PROFILES.put("concept-page", new Fetch[] {
                new Fetch("taxon", FetchMode.JOIN),
                new Fetch("authority", FetchMode.JOIN),
                new Fetch("exactMatch", FetchMode.JOIN),
                new Fetch("related", FetchMode.JOIN),
                new Fetch("relatedTerms", FetchMode.SELECT),
                new Fetch("exactMatches", FetchMode.SELECT),
                new Fetch("comments", FetchMode.SELECT)
                });

    }

    /**
     *
     */
    public ConceptDaoImpl() {
        super(Concept.class);
    }

    /**
     * @param profile
     *            Set the profile name
     * @return an array of related objects to fetch
     */
    @Override
    protected final Fetch[] getProfile(final String profile) {
        return ConceptDaoImpl.FETCH_PROFILES.get(profile);
    }
}
