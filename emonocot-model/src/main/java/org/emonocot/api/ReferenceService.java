package org.emonocot.api;

import org.emonocot.model.Reference;

/**
 *
 * @author ben
 *
 */
public interface ReferenceService extends Service<Reference> {

    /**
     *
     * @param source The external identifier for this reference
     * @return a reference or null if it cannot be found
     */
    Reference findBySource(String source);

}
