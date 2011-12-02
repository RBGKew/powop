package org.emonocot.service.impl;

import org.emonocot.api.Service;
import org.emonocot.model.common.Base;
import org.emonocot.persistence.dao.Dao;
import org.springframework.transaction.annotation.Transactional;

/**
 *
 * @author ben
 *
 * @param <T> the type of object provided by this service
 * @param <DAO> the DAO used by this service
 */
public abstract class ServiceImpl<T extends Base, DAO extends Dao<T>>
        implements Service<T> {

    /**
     *
     */
    protected DAO dao;
    
    /**
     * @param identifier the identifier of the object
     * @param the fetch profile to use
     * @return the object
     */
    @Transactional(readOnly = true)
    public T load(String identifier, String fetch) {
        return dao.load(identifier, fetch);
    }
    
   /**
    *
    * @param identifier
    *            Set the identifier of the object you would like to delete
    */
   @Transactional
   public void delete(String identifier) {
       dao.delete(identifier);
   }

    /**
     * @param identifier the identifier of the object
     * @param the fetch profile to use
     * @return the object
     */
   @Transactional(readOnly = true)
    public T find(String identifier, String fetch) {
        return dao.find(identifier, fetch);
    }

    /**
     * @param identifier the identifier of the object
     * @return the object loaded using the default fetch profile
     */
    @Transactional(readOnly = true)
    public T load(final String identifier) {
        return dao.load(identifier);
    }

    /**
     * @param identifier the identifier of the object
     * @return the object, or null if the object does not exist
     */
    @Transactional(readOnly = true)
    public T find(String identifier) {
        return dao.find(identifier);
    }

    /**
     * @param t the object to save
     * @return the identifier of the object
     */
    @Transactional
    public T save(T t) {
        return dao.save(t);
    }

    /**
     * @param t the object to save
     * @return the identifier of the object
     */
    @Transactional
    public void saveOrUpdate(T t) {
        dao.saveOrUpdate(t);
    }
    
    /**
     * @param t the object to update
     * @return the identifier of the object
     */
    @Transactional
    public void update(T t) {
        dao.update(t);
    }
}
