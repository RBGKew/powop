package org.emonocot.service.impl;

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
public class SourceServiceImpl extends ServiceImpl<Source, SourceDao> implements
        SourceService {

    /**
     *
     * @param sourceDao Set the source dao
     */
    @Autowired
    public void setSourceDao(SourceDao sourceDao) {
        super.dao = sourceDao;
    }

}
