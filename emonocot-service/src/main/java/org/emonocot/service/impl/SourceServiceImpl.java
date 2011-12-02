package org.emonocot.service.impl;

import org.emonocot.api.SourceService;
import org.emonocot.model.source.Source;
import org.emonocot.persistence.dao.SourceDao;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.access.prepost.PostAuthorize;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

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
    
    /**
     * TODO Enable once the functional tests are working
     * @param identifier the identifier of the object
     * @param the fetch profile to use
     * @return the object
     */
    //@PostAuthorize("hasPermission(returnObject, 'READ')")
    @Transactional(readOnly = true)
    public Source load(String identifier, String fetch) {
        return dao.load(identifier, fetch);
    }
    
    /**
     * TODO Enable once the functional tests are working
     *
     * @param identifier the identifier of the object
     * @return the object loaded using the default fetch profile
     *
     */
    //@PostAuthorize("hasPermission(returnObject, 'READ')")
    @Transactional(readOnly = true)
    public final Source load(final String identifier) {
        return dao.load(identifier);
    }

}
