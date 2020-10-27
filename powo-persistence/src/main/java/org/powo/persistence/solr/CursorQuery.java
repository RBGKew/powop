package org.powo.persistence.solr;

import org.apache.solr.client.solrj.SolrQuery;
import org.apache.solr.common.params.CursorMarkParams;

public class CursorQuery extends BaseQueryOption {

	@Override
	public void addQueryOption(String key, String value, SolrQuery query) {
		String cursorMark;
		if (value.trim().isEmpty()) {
			return;
		}

		if ("*".equals(value)) {
			cursorMark = CursorMarkParams.CURSOR_MARK_START;
		} else {
			cursorMark = value;
		}

		query.set(CursorMarkParams.CURSOR_MARK_PARAM, cursorMark);
	}
}
