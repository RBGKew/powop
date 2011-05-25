package org.emonocot.persistence.dao.hibernate;

import java.util.HashMap;
import java.util.Map;

import org.emonocot.model.hibernate.Fetch;
import org.emonocot.model.taxon.Taxon;
import org.emonocot.persistence.dao.TaxonDao;
import org.hibernate.FetchMode;
import org.springframework.stereotype.Repository;

/**
 *
 * @author ben
 *
 */
@Repository
public class TaxonDaoImpl extends DaoImpl<Taxon> implements TaxonDao {

    /**
     *
     */
    private static Map<String, Fetch[]> FETCH_PROFILES;

    static {
        FETCH_PROFILES = new HashMap<String, Fetch[]>();
        FETCH_PROFILES.put("taxon-with-related", new Fetch[] {
                new Fetch("parent", FetchMode.JOIN),
                new Fetch("accepted", FetchMode.JOIN),
                new Fetch("children", FetchMode.SELECT),
                new Fetch("synonyms", FetchMode.SELECT)
        });
    }

    /**
     *
     */
    public TaxonDaoImpl() {
        super(Taxon.class);
    }

    @Override
    public final boolean verify(final String identifer,
            final String scientificName) {
        // TODO Auto-generated method stub
        return false;
    }

    @Override
    protected final Fetch[] getProfile(final String profile) {
        return TaxonDaoImpl.FETCH_PROFILES.get(profile);
    }
}
