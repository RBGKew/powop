package org.emonocot.api;

import java.util.List;

import org.emonocot.model.Taxon;


/**
 *
 * @author ben
 *
 */
public interface TaxonService extends SearchableService<Taxon> {

    /**
     * Returns the root taxa in the resultset (accepted taxa with no parent
     * taxon).
     * @param identifier TODO
     * @param pageSize
     *            The maximum number of results to return
     * @param pageNumber
     *            The offset (in pageSize chunks, 0-based) from the beginning of
     *            the recordset
     * @param fetch
     *            Set the fetch profile
     *
     * @return a List from the resultset
     */
    List<Taxon> loadChildren(String identifier, Integer pageSize, Integer pageNumber, String fetch);
}
