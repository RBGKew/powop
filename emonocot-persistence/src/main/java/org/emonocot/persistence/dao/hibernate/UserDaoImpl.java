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
package org.emonocot.persistence.dao.hibernate;

import java.util.HashMap;
import java.util.Map;

import org.emonocot.model.auth.Group;
import org.emonocot.model.auth.User;
import org.emonocot.model.hibernate.Fetch;
import org.emonocot.persistence.dao.UserDao;
import org.hibernate.Criteria;
import org.hibernate.Hibernate;
import org.hibernate.criterion.Restrictions;
import org.springframework.stereotype.Repository;

/**
 *
 * @author ben
 *
 */
@Repository
public class UserDaoImpl extends SearchableDaoImpl<User> implements UserDao {

	/**
	 *
	 */
	public UserDaoImpl() {
		super(User.class);
	}

	/**
	 *
	 */
	private static Map<String, Fetch[]> FETCH_PROFILES;

	static {
		FETCH_PROFILES = new HashMap<String, Fetch[]>();
	}

	/**
	 *
	 * @param profile Set the name of the fetch profile
	 * @return a list of associated objects to fetch
	 */
	protected final Fetch[] getProfile(final String profile) {
		return UserDaoImpl.FETCH_PROFILES.get(profile);
	}

	/**
	 *
	 * @param identifier
	 *            Set the identifier of the user you would like to retrieve
	 * @param fetch Set the fetch profile to use
	 * @return the user or throw and exception if that user does not exist
	 */
	@Override
	public final User load(String identifier, String fetch) {
		User user = super.load(identifier, fetch);
		initializeUserPermissions(user);
		return user;
	}

	/**
	 *
	 * @param user
	 *            Set the user to initialize
	 */
	private void initializeUserPermissions(final User user) {
		if (user != null) {
			Hibernate.initialize(user.getPermissions());
			Hibernate.initialize(user.getGroups());
			for (Group group : user.getGroups()) {
				Hibernate.initialize(group.getPermissions());
			}
		}
	}

	/**
	 *
	 * @param identifier
	 *            Set the identifier of the user you would like to retrieve
	 * @param fetch
	 *            Set the fetch profile to use
	 * @return the user or null if that user does not exist
	 */
	@Override
	public final User find(String identifier, String fetch) {
		User user = super.find(identifier, fetch);
		initializeUserPermissions(user);
		return user;
	}

	@Override
	protected boolean isSearchableObject() {
		return false;
	}

	@Override
	public User getUserByApiKey(String apiKey) {
		Criteria criteria = getSession().createCriteria(type).add(
				Restrictions.eq("apiKey", apiKey));
		User user = (User) criteria.uniqueResult();
		initializeUserPermissions(user);
		return user;
	}
}
