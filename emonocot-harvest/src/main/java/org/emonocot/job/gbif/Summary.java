package org.emonocot.job.gbif;

public class Summary {
	
	private Integer start;
	
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
}
