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
     * @param bibliographicCitation The external identifier for this reference
     * @return a reference or null if it cannot be found
     */
    Reference findByBibliographicCitation(String bibliographicCitation);

}
