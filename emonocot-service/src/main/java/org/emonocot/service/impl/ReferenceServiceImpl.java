package org.emonocot.service.impl;

import org.emonocot.model.reference.Reference;
import org.emonocot.persistence.dao.ReferenceDao;
import org.emonocot.service.ReferenceService;
import org.springframework.stereotype.Service;

/**
 *
 * @author ben
 *
 */
@Service
public class ReferenceServiceImpl extends ServiceImpl<Reference, ReferenceDao> implements
        ReferenceService {

}
