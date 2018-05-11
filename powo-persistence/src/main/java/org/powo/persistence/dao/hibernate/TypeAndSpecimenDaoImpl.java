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
package org.powo.persistence.dao.hibernate;

import java.util.HashMap;
import java.util.Map;

import org.hibernate.Criteria;
import org.hibernate.criterion.Restrictions;
import org.powo.model.TypeAndSpecimen;
import org.powo.model.hibernate.Fetch;
import org.powo.persistence.dao.TypeAndSpecimenDao;
import org.springframework.stereotype.Repository;

@Repository
public class TypeAndSpecimenDaoImpl extends SearchableDaoImpl<TypeAndSpecimen> implements
TypeAndSpecimenDao {

	private static Map<String, Fetch[]> FETCH_PROFILES;

	static {
		FETCH_PROFILES = new HashMap<String, Fetch[]>();
	}

	public TypeAndSpecimenDaoImpl() {
		super(TypeAndSpecimen.class);
	}

	@Override
	protected Fetch[] getProfile(String profile) {
		return TypeAndSpecimenDaoImpl.FETCH_PROFILES.get(profile);
	}

	@Override
	public TypeAndSpecimen findByCatalogNumber(String catalogNumber) {
		Criteria criteria = getSession().createCriteria(type).add(Restrictions.eq("catalogNumber", catalogNumber));
		return (TypeAndSpecimen) criteria.uniqueResult();
	}

	@Override
	protected boolean isSearchableObject() {
		return false;
	}

}
