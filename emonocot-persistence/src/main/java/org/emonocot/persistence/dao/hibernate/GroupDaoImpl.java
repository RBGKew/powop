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
import java.util.List;
import java.util.Map;

import org.emonocot.model.auth.Group;
import org.emonocot.model.hibernate.Fetch;
import org.emonocot.persistence.dao.GroupDao;
import org.hibernate.FetchMode;
import org.hibernate.Query;
import org.springframework.stereotype.Repository;

/**
 *
 * @author ben
 *
 */
@Repository
public class GroupDaoImpl extends SearchableDaoImpl<Group> implements GroupDao {

	/**
	 *
	 */
	private static Map<String, Fetch[]> FETCH_PROFILES;

	static {
		FETCH_PROFILES = new HashMap<String, Fetch[]>();
		FETCH_PROFILES.put("group-page", new Fetch[] {
				new Fetch("members", FetchMode.SELECT),
				new Fetch("permissions", FetchMode.SELECT)});
	}

	/**
	 *
	 */
	public GroupDaoImpl() {
		super(Group.class);
	}

	/**
	 * @param pageSize the maximum size of the list to return
	 * @param pageNumber the (0-based) offset from the start of the result set
	 * @return a list of group names
	 */
	public final List<String> listNames(final Integer pageSize,
			final Integer pageNumber) {
		Query query = getSession().createQuery("select group.identifier from Group as group");
		if (pageSize != null) {
			query.setMaxResults(pageSize);
			if (pageNumber != null) {
				query.setFirstResult(pageSize * pageNumber);
			}
		}
		return (List<String>) query.list();
	}

	/**
	 * @param group set the group
	 * @param pageSize the maximum size of the list to return
	 * @param pageNumber the (0-based) offset from the start of the result set
	 * @return a list of member names
	 */
	public final List<String> listMembers(final Group group,
			final Integer pageSize, final Integer pageNumber) {
		Query query = getSession().createQuery("select member.identifier from Group as group join group.members as member where group.identifier = :group");
		query.setParameter("group", group.getName());
		if (pageSize != null) {
			query.setMaxResults(pageSize);
			if (pageNumber != null) {
				query.setFirstResult(pageSize * pageNumber);
			}
		}
		return (List<String>) query.list();
	}

	@Override
	protected final Fetch[] getProfile(final String profile) {
		return GroupDaoImpl.FETCH_PROFILES.get(profile);
	}

	@Override
	protected boolean isSearchableObject() {
		return false;
	}
}
