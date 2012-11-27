package org.emonocot.persistence.dao;

import org.emonocot.model.Reference;

/**
 *
 * @author ben
 *
 */
public interface ReferenceDao extends Dao<Reference> {

    /**
     * @param bibliographicCitation The source of the reference you want to find
     * @return a reference or null if it does not exist
     */
    Reference findByBibliographicCitation(String bibliographicCitation);

}
