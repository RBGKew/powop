package org.emonocot.service.impl;

import java.util.List;

import org.emonocot.api.TaxonService;
import org.emonocot.model.Taxon;
import org.emonocot.persistence.dao.TaxonDao;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

/**
 *
 * @author ben
 *
 */
@Service
public class TaxonServiceImpl extends SearchableServiceImpl<Taxon, TaxonDao>
        implements TaxonService {

    /**
     *
     * @param taxonDao Set the taxon dao
     */
    @Autowired
    public final void setTaxonDao(TaxonDao taxonDao) {
        super.dao = taxonDao;
    }

    /**
     * Returns the child taxa of the taxon with identifier specified.
     * @param identifier Set the identifier
     *
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
    @Transactional(readOnly = true)
    public final List<Taxon> loadChildren(final String identifier,
            final Integer pageSize, final Integer pageNumber, final String fetch) {
        return dao.loadChildren(identifier, pageSize, pageNumber, fetch);
    }

}
