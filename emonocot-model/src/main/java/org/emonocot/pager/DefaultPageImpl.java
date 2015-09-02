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
package org.emonocot.pager;

import java.util.ArrayList;
import java.util.List;

import org.apache.solr.client.solrj.response.QueryResponse;

/**
 *
 * @author ben
 *
 * @param <T>
 */
public class DefaultPageImpl<T> extends AbstractPageImpl<T> {

	/**
	 *
	 */
	private static final long serialVersionUID = 7342101588074430414L;
	/**
	 *
	 */
	public static final Integer MAX_PAGE_LABELS = null;

	public DefaultPageImpl() {
		super(0, 0, 0, new ArrayList<T>(), null);
	}

	/**
	 *
	 * @param newCurrentIndex
	 *            the page of this result set (0-based), can be null
	 * @param count
	 *            the total number of results available for this query
	 * @param newPageSize
	 *            The size of pages (can be null if all results should be
	 *            returned if available)
	 * @param newRecords
	 *            A list of objects in this page (can be empty if there were no
	 *            results)
	 */
	public DefaultPageImpl(final Integer count, final Integer newCurrentIndex,
			final Integer newPageSize, final List<T> newRecords, QueryResponse queryResponse) {
		super(count, newCurrentIndex, newPageSize, newRecords, queryResponse);
	}

	@Override
	public final String createLabel(
			final String startLabel, final String endLabel) {
		return startLabel + AbstractPageImpl.LABEL_DIVIDER + endLabel;
	}
}
