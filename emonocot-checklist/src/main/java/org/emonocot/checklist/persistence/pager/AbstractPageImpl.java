package org.emonocot.checklist.persistence.pager;

import java.io.Serializable;
import java.util.List;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public abstract class AbstractPageImpl<T> implements Page<T>, Serializable {

	private static Logger logger = LoggerFactory.getLogger(AbstractPageImpl.class);
	protected Integer pagesAvailable;
	protected Integer prevIndex;
	protected Integer nextIndex;
	protected Integer currentIndex;
	protected Integer firstRecord;
	protected Integer lastRecord;
	protected Integer size;
	protected List<T> records;
	protected Integer pageSize;

	/**
	 * Constructor
	 * 
	 * @param currentIndex the page of this result set (0-based), can be null 
	 * @param count the total number of results available for this query
	 * @param pageSize The size of pages (can be null if all results should be returned if available)
	 * @param records A list of objects in this page (can be empty if there were no results)
	 */
	public AbstractPageImpl(Integer count, Integer currentIndex, Integer pageSize, List<T> records) {
        if(currentIndex != null) {
		    this.currentIndex = currentIndex;
        } else {
        	this.currentIndex = 0;
        }
		
        this.pageSize = pageSize;
		if(count == 0) {
			pagesAvailable = 1;
		} else if(pageSize != null) {
			 if( 0 == count % pageSize) {
				pagesAvailable = count / pageSize;                
			} else {
				pagesAvailable = (count / pageSize) + 1; 
			}
		} else {
			pagesAvailable = 1;
		}
		
		if(pagesAvailable == 1) {
			nextIndex = null;
			prevIndex = null;
		} else {
			if(0 < this.currentIndex) {
				prevIndex = this.currentIndex - 1;
			}
			if(this.currentIndex < (pagesAvailable - 1)) {
				nextIndex = this.currentIndex + 1;
			}
		}
		if(pageSize == null) {
			this.firstRecord = 1;
	    	this.lastRecord = records.size();
		} else {
    		this.firstRecord = (this.currentIndex * pageSize) + 1;
	    	this.lastRecord = (this.currentIndex * pageSize) + records.size();
		}
	    	
		this.size = count; 
		this.records = records;
	}

	public Integer getPagesAvailable() {
		return pagesAvailable;
	}

	public Integer getNextIndex() {
		return nextIndex;
	}

	public Integer getPrevIndex() {
		return prevIndex;
	}

	public Integer getCurrentIndex() {
		return currentIndex;
	}

	public Integer size() {
		return size;
	}

	public Integer getFirstRecord() {
		return firstRecord;
	}

	public Integer getLastRecord() {
		return lastRecord;
	}

	public List<T> getRecords() {
		return records;
	}

	public Integer getPageSize() {
		return pageSize;
	}
}
