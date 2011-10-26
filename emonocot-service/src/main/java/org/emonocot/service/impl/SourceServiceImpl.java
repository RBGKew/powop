package org.emonocot.service.impl;

/** 
* import org.emonocot.api.TaxonService;
* import org.emonocot.model.taxon.Taxon;
* import org.emonocot.persistence.dao.TaxonDao;
* import org.springframework.beans.factory.annotation.Autowired;
* import org.springframework.stereotype.Service;
* import org.springframework.transaction.annotation.Transactional;
**/

import org.emonocot.api.SourceService;
import org.emonocot.model.source.Source;
import org.emonocot.persistence.dao.SourceDao;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

/**
 *
 * @author ben
 *
 */
@Service
public class SourceServiceImpl extends SearchableServiceImpl<Source, SourceDao> implements
        SourceService {

    @Autowired
    public void setSourceDao(SourceDao sourceDao) {
        super.dao = sourceDao;
    }

}
