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
package org.emonocot.persistence.dao;

import java.util.List;

import org.emonocot.model.auth.Group;

/**
 *
 * @author ben
 *
 */
public interface GroupDao extends SearchableDao<Group> {

	/**
	 *
	 * @param pageSize The maximum size of the list to be returned
	 * @param pageNumber The (0-based) offset from the start of the result set
	 * @return a list of group names, sorted alphabetically
	 */
	List<String> listNames(Integer pageSize, Integer pageNumber);

	/**
	 *
	 * @param group The group of interest
	 * @param pageSize The maximum size of the list to be returned
	 * @param pageNumber The (0-based) offset from the start of the result set
	 * @return a list of user names of users belonging to the group
	 */
	List<String> listMembers(Group group, Integer pageSize, Integer pageNumber);
}
