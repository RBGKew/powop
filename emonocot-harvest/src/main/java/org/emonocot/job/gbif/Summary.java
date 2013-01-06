package org.emonocot.job.gbif;

public class Summary {
	
	private Integer start;
	
	private Integer next;
	
	private Integer totalMatched;
	
	private Integer totalReturned;
	
	public void setStart(Integer start) {
		this.start = start;
	}
	
	public Integer getStart() {
		return start;
	}
	
	public void setTotalMatched(Integer totalMatched) {
		this.totalMatched = totalMatched;
	}
	
	public Integer getTotalMatched() {
		return totalMatched;
	}
	
	public void setTotalReturned(Integer totalReturned) {
		this.totalReturned = totalReturned;
	}
	
	public Integer getTotalReturned() {
		return totalReturned;
	}

	public Integer getNext() {
		return next;
	}

	public void setNext(Integer next) {
		this.next = next;
	}
}
