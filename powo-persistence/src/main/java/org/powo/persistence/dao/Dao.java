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
package org.powo.persistence.dao;

import java.util.List;

import org.powo.model.Base;

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
	 *            Set the identifier of the object you would like to delete
	 */
	void deleteById(Long id);

	/**
	 *
	 * @param identifier
	 *            Set the identifier of the object you would like to retrieve
	 * @return the object or null if that object does not exist
	 */
	T find(String identifier);

	/**
	 * Run the query using FetchMode=COMMIT. This means that pending changes aren't
	 * checked and flushed to disk. Should only be used when we can be sure that
	 * there are no pending updates that are relevant to the entity being found.
	 * 
	 * E.G. this is used by the non-owned processor (via TaxonService) to find Taxons, 
	 * as Taxons are not created or modified by that processor.
	 * 
	 * @param identifier
	 *            Set the identifier of the object you would like to retrieve
	 * @return the object or null if that object does not exist
	 */
	T findPersisted(String identifier);

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
	 * @param fetch Set the fetch profile to determine which relations are fetched
	 * @return A list of results
	 */
	List<T> list(Integer page, Integer size);

	/**
	 * @return all objects of given type
	 * @return
	 */
	List<T> list();

	void refresh(T t);
}
