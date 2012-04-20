package org.emonocot.api;

import org.emonocot.model.key.IdentificationKey;

/**
 * @author jk00kg
 *
 */
public interface IdentificationKeyService extends
        SearchableService<IdentificationKey> {

    /**
     *
     * @param source Set the source of the identification key
     * @return an identification key
     */
    IdentificationKey findBySource(String source);

}
