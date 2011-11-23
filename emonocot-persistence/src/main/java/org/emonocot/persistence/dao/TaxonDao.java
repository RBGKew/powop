package org.emonocot.persistence.dao;

import java.util.List;

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

}
