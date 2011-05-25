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
    
    @Override
    public T load(String identifier, String fetch) {
        return dao.load(identifier, fetch);
    }

    @Override
    public T find(String identifier, String fetch) {
        return dao.find(identifier, fetch);
    }

    protected DAO dao;

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

    @Override
    @Transactional
    public Long save(T t) {
        return dao.save(t);
    }
}
