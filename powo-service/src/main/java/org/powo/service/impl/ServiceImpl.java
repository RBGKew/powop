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
package org.powo.service.impl;

import java.util.ArrayList;
import java.util.List;

import org.powo.api.Service;
import org.powo.model.Base;
import org.powo.pager.DefaultPageImpl;
import org.powo.pager.Page;
import org.powo.persistence.dao.Dao;
import org.springframework.transaction.annotation.Transactional;

/**
 *
 * @author ben
 *
 * @param <T>
 *            the type of object provided by this service
 * @param <DAO>
 *            the DAO used by this service
 */
public abstract class ServiceImpl<T extends Base, DAO extends Dao<T>> implements Service<T> {

	protected DAO dao;

	/**
	 * @param identifier
	 *            the identifier of the object
	 * @param fetch
	 *            the fetch profile to use
	 * @return the object
	 */
	@Transactional(readOnly = true)
	public T load(String identifier, String fetch) {
		return dao.load(identifier, fetch);
	}

	/**
	 * @param id
	 *            the primary key of the object
	 * @param fetch
	 *            the fetch profile to use
	 * @return the object
	 */
	@Transactional(readOnly = true)
	public T load(Long id, String fetch) {
		return dao.load(id, fetch);
	}

	/**
	 *
	 * @param identifier
	 *            Set the identifier of the object you would like to delete
	 */
	@Transactional
	public void deleteById(final Long id) {
		dao.deleteById(id);
	}

	/**
	 *
	 * @param identifier
	 *            Set the identifier of the object you would like to delete
	 */
	@Transactional
	public void delete(final String identifier) {
		dao.delete(identifier);
	}

	/**
	 * @param identifier
	 *            the identifier of the object
	 * @param the
	 *            fetch profile to use
	 * @return the object
	 */
	@Transactional(readOnly = true)
	public T find(final String identifier, final String fetch) {
		return dao.find(identifier, fetch);
	}

	/**
	 * @param id
	 *            the primary key of the object
	 * @param the
	 *            fetch profile to use
	 * @return the object
	 */
	@Transactional(readOnly = true)
	public T find(final Long id, final String fetch) {
		return dao.find(id, fetch);
	}

	/**
	 * @param identifier
	 *            the identifier of the object
	 * @return the object loaded using the default fetch profile
	 */
	@Transactional(readOnly = true)
	public T load(final String identifier) {
		return dao.load(identifier);
	}

	/**
	 * @param id
	 *            the primary key of the object
	 * @return the object loaded using the default fetch profile
	 */
	@Transactional(readOnly = true)
	public T load(final Long id) {
		return dao.load(id);
	}

	/**
	 * @param identifier
	 *            the identifier of the object
	 * @return the object, or null if the object does not exist
	 */
	@Transactional(readOnly = true)
	public T find(String identifier) {
		return dao.find(identifier);
	}

	/**
	 * @param id
	 *            the primary key of the object
	 * @return the object, or null if the object does not exist
	 */
	@Transactional(readOnly = true)
	public T find(Long id) {
		return dao.find(id);
	}

	/**
	 * @param t
	 *            the object to save
	 * @return the identifier of the object
	 */
	@Transactional
	public T save(T t) {
		return dao.save(t);
	}

	/**
	 * @param t
	 *            the object to save
	 */
	@Transactional
	public void saveOrUpdate(T t) {
		dao.saveOrUpdate(t);
	}

	/**
	 * @param t
	 *            the object to update
	 */
	@Transactional
	public void update(T t) {
		dao.update(t);
	}

	/**
	 * @param t
	 *            the object to merge
	 * @return the merged object
	 */
	@Transactional
	public final T merge(final T t) {
		return dao.merge(t);
	}

	/**
	 * @param page Set the offset (in size chunks, 0-based), optional
	 * @param size Set the page size
	 * @return A page of results
	 */
	@Transactional
	public final Page<T> list(final Integer page, final Integer size, final String fetch) {
		Long numberOfResults = dao.count();
		if (numberOfResults == 0) {
			return new DefaultPageImpl<T>(new ArrayList<T>(), 0, 0);
		} else {
			return new DefaultPageImpl<T>(dao.list(page, size, fetch), page, size);
		}
	}

	@Transactional
	public final Page<T> list(final Integer page, final Integer size) {
		return list(page, size, null);
	}

	@Transactional
	public final List<T> list() {
		return dao.list();
	}

	@Transactional
	public final List<T> list(String fetch) {
		return dao.list(null, null, fetch);
	}

	public long count() {
		return dao.count();
	}

	@Transactional
	public void refresh(T t) {
		dao.refresh(t);
	}
}
