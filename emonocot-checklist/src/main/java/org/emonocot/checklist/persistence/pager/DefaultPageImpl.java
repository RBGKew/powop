package org.emonocot.checklist.persistence.pager;

import java.util.List;

public class DefaultPageImpl<T> extends AbstractPageImpl<T> {

	private static final long serialVersionUID = 7342101588074430414L;

	public DefaultPageImpl(Integer count, Integer currentIndex,Integer pageSize, List<T> records) {
		super(count, currentIndex, pageSize, records);
	}
}
