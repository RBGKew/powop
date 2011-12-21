package org.emonocot.persistence.dao;

import org.emonocot.model.reference.Reference;

/**
 *
 * @author ben
 *
 */
public interface ReferenceDao extends Dao<Reference> {

    /**
     * @param source The source of the reference you want to find
     * @return a reference or null if it does not exist
     */
    Reference findBySource(String source);

}
