package org.emonocot.service.impl;

import org.emonocot.api.ReferenceService;
import org.emonocot.model.reference.Reference;
import org.emonocot.persistence.dao.ReferenceDao;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

/**
 *
 * @author ben
 *
 */
@Service
public class ReferenceServiceImpl extends ServiceImpl<Reference, ReferenceDao> implements
        ReferenceService {
    
    @Autowired
    public void setReferenceDao(ReferenceDao referenceDao) {
        super.dao = referenceDao;
    }

}
