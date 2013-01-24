/**
 *
 */
package org.emonocot.service.impl;

import org.emonocot.api.IdentificationKeyService;
import org.emonocot.model.IdentificationKey;
import org.emonocot.persistence.dao.IdentificationKeyDao;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

/**
 * @author jk00kg
 */
@Service
public class IdentificationKeyServiceImpl extends ServiceImpl<IdentificationKey, IdentificationKeyDao>  implements IdentificationKeyService {

    /**
     * @param newIdentificationKeyDao Set the identification key dao
     */
    @Autowired
    public final void setIdentificationKeyDao(
            final IdentificationKeyDao newIdentificationKeyDao) {
        super.dao = newIdentificationKeyDao;
    }
}
