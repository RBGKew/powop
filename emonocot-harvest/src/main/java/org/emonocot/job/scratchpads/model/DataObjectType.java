package org.emonocot.job.scratchpads.model;

public enum DataObjectType {
	TEXT("http://purl.org/dc/dcmitype/Text"), 
	STILL_IMAGE("http://purl.org/dc/dcmitype/StillImage"), 
	MOVING_IMAGE("http://purl.org/dc/dcmitype/MovingImage"), 
	SOUND("http://purl.org/dc/dcmitype/Sound");
	
	private String uri;
	
	private DataObjectType(String uri) {
		this.uri = uri;
	}
	
	@Override
	public String toString() {
		return uri;
	}
	
	public static DataObjectType fromString(String value) {
		for(DataObjectType d : DataObjectType.values()) {
			if(d.uri.equals(value)) {
				return d;
			}
		}
		
		throw new IllegalArgumentException(value + " is not a valid string representation of DataObjectType");
	}
}
