package org.emonocot.service.impl;

import org.emonocot.api.ReferenceService;
import org.emonocot.model.reference.Reference;
import org.emonocot.persistence.dao.ReferenceDao;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

/**
 *
 * @author ben
 *
 */
@Service
public class ReferenceServiceImpl extends ServiceImpl<Reference, ReferenceDao>
        implements ReferenceService {

    /**
     *
     * @param referenceDao Set the reference dao
     */
    @Autowired
    public final void setReferenceDao(final ReferenceDao referenceDao) {
        super.dao = referenceDao;
    }

    /**
     * @param source The source of the reference you want to find
     * @return a reference or null if it does not exist
     */
    @Transactional(readOnly = true)
    public final Reference findBySource(final String source) {
        return dao.findBySource(source);
    }

}
