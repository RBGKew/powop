package org.emonocot.portal.remoting;

import java.util.List;

import org.emonocot.model.Taxon;
import org.emonocot.pager.Page;
import org.emonocot.persistence.dao.TaxonDao;
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

    public Page<Taxon> searchByExample(Taxon example, boolean ignoreCase,
            boolean useLike) {
        throw new UnsupportedOperationException("Remote searching by example is unimplemented");
    }

}
