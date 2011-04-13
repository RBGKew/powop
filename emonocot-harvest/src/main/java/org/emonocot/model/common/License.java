package org.emonocot.model.common;

public enum License {
	PUBLIC_DOMAIN("http://creativecommons.org/licenses/publicdomain/"),
	ATTRIBUTION("http://creativecommons.org/licenses/by/3.0/"),
	ATTRIBUTION_NONCOMMERCIAL("http://creativecommons.org/licenses/by-nc/3.0/"),
	ATTRIBUTION_SHAREALIKE("http://creativecommons.org/licenses/by-sa/3.0/"),
	ATTRIBUTION_NONCOMMERCIAL_SHAREALIKE("http://creativecommons.org/licenses/by-nc-sa/3.0/");
	
	private String uri;
	
    private License(String uri) {
    	this.uri = uri;
    }
    
    public static License fromString(String uri) {
    	for(License l : License.values()) {
    		if(l.uri.equals(uri)) {
    			return l;
    		}
    	}
    	throw new IllegalArgumentException(uri + " is not an acceptable value for License");
    }
}
