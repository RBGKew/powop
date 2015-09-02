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

import org.emonocot.model.Comment;
import org.emonocot.model.hibernate.Fetch;
import org.emonocot.persistence.dao.CommentDao;
import org.hibernate.FetchMode;
import org.springframework.stereotype.Repository;

/**
 * @author jk00kg
 *
 */
@Repository
public class CommentDaoImpl extends SearchableDaoImpl<Comment> implements CommentDao {

	/**
	 *
	 */
	private static Map<String, Fetch[]> FETCH_PROFILES;

	static {
		FETCH_PROFILES = new HashMap<String, Fetch[]>();
		FETCH_PROFILES.put("aboutData", new Fetch[] {
				new Fetch("user", FetchMode.JOIN),
				new Fetch("authority", FetchMode.JOIN),
				new Fetch("inResponseTo", FetchMode.JOIN),
				new Fetch("inResponseTo.user", FetchMode.SELECT),
				new Fetch("alternativeIdentifiers", FetchMode.SELECT),
				new Fetch("commentPage", FetchMode.SELECT),
				new Fetch("commentPage.authority", FetchMode.SELECT),
				new Fetch("aboutData", FetchMode.SELECT),
				new Fetch("aboutData.authority", FetchMode.SELECT),
				new Fetch("aboutData.organisation", FetchMode.SELECT)
		});
	}

	public CommentDaoImpl() {
		super(Comment.class);
	}

	/* (non-Javadoc)
	 * @see org.emonocot.persistence.dao.hibernate.DaoImpl#getProfile(java.lang.String)
	 */
	@Override
	protected Fetch[] getProfile(String profile) {
		Fetch[] fetch = FETCH_PROFILES.get(profile);
		if(fetch != null) {
			return fetch;
		} else {
			return FETCH_PROFILES.get("aboutData");
		}
	}

	@Override
	protected boolean isSearchableObject() {
		return false;
	}
}
