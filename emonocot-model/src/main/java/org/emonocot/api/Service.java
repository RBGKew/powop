package org.emonocot.api;

import org.emonocot.model.Base;
import org.emonocot.pager.Page;
import org.springframework.transaction.annotation.Transactional;

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
     * @param id The primary key of the object required.
     * @return The object, or throw a
     *         HibernateObjectRetrievalFailureException if the
     *         object is not found
     */
   T load(Long id);

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
    * @param id The primary key of the object required.
    * @return The object, or null if the object is not found
    */
   T find(Long id);


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
   * @param id
   *            the primary key of the object you would like to retrieve
   * @param fetch Set the fetch profile to use
   * @return the object or throw and exception if that object does not exist
   */
  T load(Long id, String fetch);

 /**
  *
  * @param id
  *            primary key of the object you would like to retrieve
  * @param fetch Set the fetch profile to use
  * @return the object or null if that object does not exist
  */
  T find(Long id, String fetch);

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

  /**
   * @param page Set the offset (in size chunks, 0-based), optional
   * @param size Set the page size
   * @return A page of results
   */
  Page<T> list(Integer page, Integer size);

  /**
   * @param t
   *            the object to merge
   * @return the merged object
   */
  @Transactional
  T merge(T t);
}
