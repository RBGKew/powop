package org.emonocot.checklist.persistence;

import java.util.List;

import org.emonocot.checklist.model.Taxon;

public interface TaxonDao {

	/**
	 * Returns a list of taxa objects where the taxon name matches the search term exactly.
	 * @param search
	 * @return
	 */
	List<Taxon> search(String search);

	/**
	 * Returns a taxon object with the specified id.
	 * @param id
	 * @throws DataRetrievalFailureException if an object with the specified id is not found in the repository
	 * @return
	 */
	Taxon get(String id);
}
