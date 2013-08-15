package org.emonocot.service.impl;

import org.emonocot.api.ConceptService;
import org.emonocot.model.Concept;
import org.emonocot.persistence.dao.ConceptDao;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

/**
 *
 * @author ben
 *
 */
@Service
public class ConceptServiceImpl extends ServiceImpl<Concept, ConceptDao>
        implements ConceptService {

    /**
     *
     * @param conceptDao Set the image dao
     */
    @Autowired
    public final void setConceptDao(final ConceptDao conceptDao) {
        super.dao = conceptDao;
    }
}
