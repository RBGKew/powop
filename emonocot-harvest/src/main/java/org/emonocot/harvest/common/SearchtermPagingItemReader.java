/**
 * 
 */
package org.emonocot.harvest.common;

import org.emonocot.api.SearchableService;
import org.emonocot.model.SearchableObject;
import org.springframework.batch.item.database.AbstractPagingItemReader;

/**
 * @author jk
 *
 */
public class SearchtermPagingItemReader<T extends SearchableObject> extends AbstractPagingItemReader<T> {

	/**
	 * 
	 */
	private SearchableService<T> service = null;

	/**
	 * 
	 */
	private String queryString = null;
	
	
	/**
	 * @param service the service to set
	 */
	public final void setService(final SearchableService<T> service) {
		this.service = service;
		logger.info("Service set " + service.toString());
	}

	/**
	 * @param queryString the queryString to set
	 */
	public final void setQueryString(final String queryString) {
		this.queryString = queryString;
	}

	/* (non-Javadoc)
	 * @see org.springframework.batch.item.database.AbstractPagingItemReader#doReadPage()
	 */
	@Override
	protected void doReadPage() {
		if (queryString != null) {
			results = service.search(queryString, null, getPageSize(), getPage(),
				null, null, null, "object-page").getRecords();
			logger.debug("Search for " + queryString + " (page number " + getPage() + " got a page of " + results.size()
					+ " (results");
		} else {
			logger.debug("Paging through all objects provided by service currently on page " + getPage());
			results = service.list(getPage(), getPageSize(), "object-page").getRecords();
		}
	}

	/* (non-Javadoc)
	 * @see org.springframework.batch.item.database.AbstractPagingItemReader#doJumpToPage(int)
	 */
	@Override
	protected void doJumpToPage(int itemIndex) {
	}

}
