package org.emonocot.portal.remoting;

import java.util.List;

import org.emonocot.model.taxon.Family;
import org.emonocot.model.taxon.Rank;
import org.emonocot.model.taxon.Taxon;
import org.emonocot.model.taxon.TaxonomicStatus;
import org.emonocot.persistence.dao.TaxonDao;
import org.hibernate.Criteria;
import org.hibernate.criterion.Projections;
import org.hibernate.criterion.Restrictions;
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
    public TaxonDaoImpl() {
        super(Taxon.class, "taxon");
    }

    /**
     * Returns the genera associated with this family.
     * TODO Remove once families are imported
     *
     * @param family
     *            the family
     * @return A list of genera
     */
    public final List<Taxon> getGenera(final Family family) {
        return null;
    }

    /**
     * Returns the number of genera in a family.
     * TODO Remove once families are imported
     *
     * @param family the family
     * @return the number of accepted genera
     */
    public final Integer countGenera(final Family family) {
        return null;
    }

    /**
     * Returns the root taxa in the resultset (accepted taxa with no parent
     * taxon).
     * @param pageSize
     *            The maximum number of results to return
     * @param pageNumber
     *            The offset (in pageSize chunks, 0-based) from the beginning of
     *            the recordset
     * @param fetch
     *            Set the fetch profile
     *
     * @return a Page from the resultset
     */
    public final List<Taxon> loadChildren(String identifier,
            final Integer pageSize, final Integer pageNumber, final String fetch) {
        // TODO Auto-generated method stub
        return null;
    }

}
