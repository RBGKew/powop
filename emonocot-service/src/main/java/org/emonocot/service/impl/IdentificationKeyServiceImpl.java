/**
 * 
 */
package org.emonocot.service.impl;

import org.emonocot.model.key.IdentificationKey;
import org.emonocot.persistence.dao.IdentificationKeyDao;
import org.emonocot.service.IdentificationKeyService;
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
     * @param newIdentificationKeyDao
     */
    @Autowired
    public final void setIdentificationKeyDao(
            final IdentificationKeyDao newIdentificationKeyDao) {
        super.dao = newIdentificationKeyDao;
    }
}
