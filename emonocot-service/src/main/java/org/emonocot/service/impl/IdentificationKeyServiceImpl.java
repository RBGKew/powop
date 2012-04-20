/**
 *
 */
package org.emonocot.service.impl;

import org.emonocot.api.IdentificationKeyService;
import org.emonocot.model.key.IdentificationKey;
import org.emonocot.persistence.dao.IdentificationKeyDao;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

/**
 * @author jk00kg
 */
@Service
public class IdentificationKeyServiceImpl extends
        SearchableServiceImpl<IdentificationKey, IdentificationKeyDao>
        implements IdentificationKeyService {

    /**
     * @param newIdentificationKeyDao Set the identification key dao
     */
    @Autowired
    public final void setIdentificationKeyDao(
            final IdentificationKeyDao newIdentificationKeyDao) {
        super.dao = newIdentificationKeyDao;
    }

   /**
    *
    * @param source Set the source of the identification key
    * @return an identification key
    */
    public final IdentificationKey findBySource(final String source) {
        return dao.findBySource(source);
    }
}
