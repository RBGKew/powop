package org.emonocot.checklist.persistence;

import java.util.List;

import org.emonocot.checklist.model.Taxon;

/**
 *
 * @author ben
 *
 */
public interface TaxonDao extends IdentifiableService<Taxon> {

    /**
     * Returns a list of taxa objects where the taxon name matches the search
     * term exactly.
     *
     * @param search The taxon name to search for
     * @return a list of taxon objects (empty if no taxon matches the search
     *         string)
     */
    List<Taxon> search(String search);

    /**
     * Returns a taxon object with the specified id.
     *
     * @param id
     *            The taxon identifier
     * @return The taxon with an identifier matching the one provided, or throws
     *         a DataRetrievalFailureException if none match
     */
    Taxon get(Integer id);
}
