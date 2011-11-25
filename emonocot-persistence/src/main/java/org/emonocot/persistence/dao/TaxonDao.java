package org.emonocot.persistence.dao;

import java.util.List;

import org.emonocot.model.pager.Page;
import org.emonocot.model.taxon.Family;
import org.emonocot.model.taxon.Taxon;

/**
 *
 * @author ben
 *
 */
public interface TaxonDao extends SearchableDao<Taxon> {

    /**
     * Returns the genera associated with this family.
     * TODO Remove once families are imported
     *
     * @param family the family
     * @return A list of genera
     */
    List<Taxon> getGenera(Family family);

    /**
     * Returns the number of genera in a family.
     * TODO Remove once families are imported
     *
     * @param family the family
     * @return the number of accepted genera
     */
    Integer countGenera(Family family);

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
     * @return a Page from the resultset
     */
    List<Taxon> loadChildren(String identifier, Integer pageSize, Integer pageNumber, String fetch);

}
