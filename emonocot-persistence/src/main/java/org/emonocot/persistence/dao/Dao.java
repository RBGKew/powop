package org.emonocot.persistence.dao;

import java.util.List;

import org.emonocot.model.Base;

/**
 *
 * @author ben
 *
 * @param <T>
 */
public interface Dao<T extends Base> {

    /**
     *
     * @param identifier
     *            Set the identifier of the object you would like to retrieve
     * @return the object or throw and exception if that object does not exist
     */
    T load(String identifier);
    
    /**
    *
    * @param id
    *            the primary key of the object you would like to retrieve
    * @return the object or throw and exception if that object does not exist
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
    * @param identifier
    *            Set the identifier of the object you would like to retrieve
    * @return the object or null if that object does not exist
    */
    T find(String identifier);
    
    /**
    *
    * @param id
    *            the primary key of the object you would like to retrieve
    * @return the object or null if that object does not exist
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
   * @param id the primary key of the object you would like to retrieve
   * @param fetch Set the fetch profile to use
   * @return the object or throw and exception if that object does not exist
   */
  T load(Long id, String fetch);

 /**
  *
  * @param id the primery key of the object you would like to retrieve
  * @param fetch Set the fetch profile to use
  * @return the object or null if that object does not exist
  */
  T find(Long id, String fetch);

  /**
   *
   * @param t The object to save.
   * @return the id of the object
   */
  T save(T t);

 /**
  *
  * @param t The object to save (or update).
  */
  void saveOrUpdate(T t);

  /**
  *
  * @param t The object to update.
  */
  void update(T t);

  /**
  *
  * @param t The object to merge.
  * @return the merged object
  */
  T merge(T t);

  /**
   * @return the total number of objects
   */
  Long count();

  /**
   * @param page Set the offset (in size chunks, 0-based), optional
   * @param size Set the page size
   * @return A list of results
   */
  List<T> list(Integer page, Integer size);
}
