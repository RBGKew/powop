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
    public final void setSourceDao(SourceDao sourceDao) {
        super.dao = sourceDao;
    }

    /**
     *
     * @param identifier the identifier of the object
     * @param fetch the fetch profile to use
     * @return the object
     */
    @PostAuthorize("hasPermission(returnObject, 'READ') or hasRole('PERMISSION_ADMINISTRATE')")
    @Transactional(readOnly = true)
    public final Source load(final String identifier, final String fetch) {
        return dao.load(identifier, fetch);
    }

    /**
     *
     * @param identifier the identifier of the object
     * @return the object loaded using the default fetch profile
     *
     */
    @PostAuthorize("hasPermission(returnObject, 'READ') or hasRole('PERMISSION_ADMINISTRATE')")
    @Transactional(readOnly = true)
    public final Source load(final String identifier) {
        return dao.load(identifier);
    }

}
