package org.emonocot.service.impl;

import org.emonocot.api.OrganisationService;
import org.emonocot.model.registry.Organisation;
import org.emonocot.persistence.dao.OrganisationDao;
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
public class OrganisationServiceImpl extends SearchableServiceImpl<Organisation, OrganisationDao> implements
        OrganisationService {

    /**
     *
     * @param sourceDao Set the source dao
     */
    @Autowired
    public final void setSourceDao(OrganisationDao sourceDao) {
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
    public final Organisation load(final String identifier, final String fetch) {
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
    public final Organisation load(final String identifier) {
        return dao.load(identifier);
    }

}
