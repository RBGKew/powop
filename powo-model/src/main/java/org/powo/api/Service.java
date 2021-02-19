/*
 * This is eMonocot, a global online biodiversity information resource.
 *
 * Copyright © 2011–2015 The Board of Trustees of the Royal Botanic Gardens, Kew and The University of Oxford
 *
 * eMonocot is free software: you can redistribute it and/or modify it under the terms of the
 * GNU Affero General Public License as published by the Free Software Foundation, either version 3
 * of the License, or (at your option) any later version.
 *
 * eMonocot is distributed in the hope that it will be useful, but WITHOUT ANY WARRANTY; without even
 * the implied warranty of MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 * GNU Affero General Public License for more details.
 *
 * The complete text of the GNU Affero General Public License is in the source repository as the file
 * ‘COPYING’.  It is also available from <http://www.gnu.org/licenses/>.
 */
package org.powo.api;

import java.util.List;

import org.powo.model.Base;
import org.powo.pager.Page;

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
	void deleteById(Long id);

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
	 * Run the query using FetchMode=COMMIT. This means that pending changes aren't
	 * checked and flushed to disk. Should only be used when we can be sure that
	 * there are no pending updates that are relevant to the entity being found.
	 * 
	 * E.G. this is used by the non-owned processor to find Taxons, as Taxons are 
	 * not created or modified by that processor.
	 * 
	 * @param id
	 *            primary key of the object you would like to retrieve
	 * @return the object or null if that object does not exist
	 */
	T findPersisted(String identifier);

	/**
	 *
	 * @param t The object to save.
	 * @return the object
	 */
	T save(T t);

	/**
	 *
	 * @param t The object to save.
	 * @return the object
	 */
	void update(T t);

	/**
	 *
	 * @param t The object to save.
	 */
	void saveOrUpdate(T t);

	/**
	 * @param page Set the offset (in size chunks, 0-based), optional
	 * @param size Set the page size
	 * @param fetch Set the fetch profile to determine which relations are fetched
	 * @return A page of results
	 */
	Page<T> list(Integer page, Integer size);

	List<T> list();

	/**
	 * 
	 * @return a count of the type of object this service works on
	 */
	long count();

	/**
	 * @param t
	 *            the object to merge
	 * @return the merged object
	 */
	T merge(T t);
	
	void refresh(T t);
}
