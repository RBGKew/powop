package org.emonocot.api;

import java.util.Map;

import org.emonocot.model.common.Base;
import org.emonocot.model.pager.Page;

/**
 *
 * @author ben
 *
 * @param <T>
 */
public interface Service<T extends Base> {
    /**
     *
     * @param identifer The identifier of the object required.
     * @return The object, or throw a
     *         HibernateObjectRetrievalFailureException if the
     *         object is not found
     */
    T load(String identifer);

   /**
    *
    * @param identifier
    *            Set the identifier of the object you would like to delete
    */
   void delete(String identifier);

    /**
     *
     * @param identifier The identifier of the object required.
     * @return The object, or null if the object is not found
     */
    T find(String identifier);

    /**
    *
    * @param identifier
    *            Set the identifier of the object you would like to retrieve
    * @param fetch Set the fetch profile to use
    * @return the object or throw and exception if that object does not exist
    */
   T load(String identifier, String fetch);

  /**
   *
   * @param identifier
   *            Set the identifier of the object you would like to retrieve
   * @param fetch Set the fetch profile to use
   * @return the object or null if that object does not exist
   */
   T find(String identifier, String fetch);

   /**
    *
    * @param t The object to save.
    * @return the object
    */
   T save(T t);

   /**
    *
    * @param t The object to save.
    */
  void saveOrUpdate(T t);
}
