package org.emonocot.service.impl;

import org.emonocot.model.common.Base;
import org.emonocot.persistence.dao.Dao;
import org.emonocot.service.Service;
import org.springframework.transaction.annotation.Transactional;

/**
 *
 * @author ben
 *
 * @param <T>
 */
public abstract class ServiceImpl<T extends Base, DAO extends Dao<T>> implements Service<T> {
    
    protected DAO dao;
    
    public abstract void setDao(DAO dao);

    @Override
    @Transactional(readOnly = true)
    public final T load(final String identifier) {
        return dao.load(identifier);
    }

    @Override
    @Transactional(readOnly = true)
    public T find(String identifier) {
        return dao.find(identifier);
    }

}
