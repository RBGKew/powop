/**
 *
 */
package org.emonocot.service.impl;

import org.emonocot.api.IdentificationKeyService;
import org.emonocot.model.IdentificationKey;
import org.emonocot.persistence.dao.IdentificationKeyDao;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

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

	@Override
	@Transactional(readOnly = false)
	@PreAuthorize("hasRole('PERMISSION_ADMINISTRATE') or hasRole('PERMISSION_DELETE_KEY')")
	public void deleteById(Long id) {		
		super.deleteById(id);
	}
    
}
