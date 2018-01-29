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
package org.powo.pager;

import java.io.Serializable;
import java.math.RoundingMode;
import java.util.List;
import com.google.common.math.IntMath;

import lombok.Data;

@Data
public class DefaultPageImpl<T> implements Page<T>, Serializable {

	private static final long serialVersionUID = 3235700796905201107L;

	private Integer page;

	private Integer perPage;

	private Integer totalPages;

	private Integer totalResults;

	private List<T> results;

	public DefaultPageImpl(List<T> results, int page, int size) {
		this.perPage = size;
		this.page = page;
		this.totalResults = results.size();
		this.totalPages = perPage > 0 ? IntMath.divide(totalResults, perPage, RoundingMode.CEILING) : 0;
		this.results = results;
	}
}