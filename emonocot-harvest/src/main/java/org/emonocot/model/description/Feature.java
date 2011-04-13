package org.emonocot.model.description;


public enum Feature {
	GENERAL_DESCRIPTION("http://rs.tdwg.org/ontology/voc/SPMInfoItems#GeneralDescription");
	
	private String uri;
	
	private Feature(String uri) {
		this.uri = uri;
	}
	
	public static Feature fromString(String uri) {
    	for(Feature f : Feature.values()) {
    		if(f.uri.equals(uri)) {
    			return f;
    		}
    	}
    	throw new IllegalArgumentException(uri + " is not an acceptable value for Feature");
    }

}
