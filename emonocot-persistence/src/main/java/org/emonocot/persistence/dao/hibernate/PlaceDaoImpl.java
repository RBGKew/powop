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

import org.emonocot.model.Place;
import org.emonocot.model.hibernate.Fetch;
import org.emonocot.persistence.dao.PlaceDao;
import org.springframework.stereotype.Repository;

/**
 *
 * @author jk00kg
 *
 */
@Repository
public class PlaceDaoImpl extends DaoImpl<Place> implements PlaceDao {

	/**
	 *
	 */
	private static Map<String, Fetch[]> FETCH_PROFILES;

	static {
		FETCH_PROFILES = new HashMap<String, Fetch[]>();

	}

	/**
	 *
	 */
	public PlaceDaoImpl() {
		super(Place.class);
	}

	/**
	 * @param profile
	 *            Set the profile name
	 * @return an array of related objects to fetch
	 */
	@Override
	protected final Fetch[] getProfile(final String profile) {
		return new Fetch[]{};
	}
}
