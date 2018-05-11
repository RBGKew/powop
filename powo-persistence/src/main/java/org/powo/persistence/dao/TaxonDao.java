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

import org.powo.model.Taxon;
import org.powo.pager.Page;

/**
 *
 * @author ben
 *
 */
public interface TaxonDao extends Dao<Taxon> {

	/**
	 * Returns the root taxa in the resultset (accepted taxa with no parent
	 * taxon).
	 * @param identifier TODO
	 * @param pageSize
	 *            The maximum number of results to return
	 * @param pageNumber
	 *            The offset (in pageSize chunks, 0-based) from the beginning of
	 *            the recordset
	 * @param fetch
	 *            Set the fetch profile
	 *
	 * @return a Page from the resultset
	 */
	List<Taxon> loadChildren(String identifier, Integer pageSize, Integer pageNumber, String fetch);

	/**
	 * @param example
	 *            an object with fields set to the required values
	 * @param ignoreCase
	 *            whether to treat Uppercase/Lowercase letters the same
	 * @param useLike
	 *            whether to enable <i>LIKE</i> in query
	 * @return a single page of objects that have the same properties as the
	 *         example
	 */
	Page<Taxon> searchByExample(Taxon example, boolean ignoreCase, boolean useLike);

}
