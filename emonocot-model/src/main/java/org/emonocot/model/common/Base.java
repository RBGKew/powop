package org.emonocot.model.common;

import org.apache.commons.lang.ObjectUtils;
import org.joda.time.DateTime;

public class Base {
	private Long id;
	
	private License license;
	
	private DateTime created;

	private DateTime modified;
	
	private String creator;
	
	public License getLicense() {
		return license;
	}

	public DateTime getCreated() {
		return created;
	}

	public DateTime getModified() {
		return modified;
	}

	public String getSource() {
		return source;
	}

	public void setId(Long id) {
		this.id = id;
	}

	private String source;
	
	public void setCreated(DateTime created) {
		this.created = created;
	}
	
	public void setModified(DateTime modified) {
		this.modified = modified;
	}
	
	public void setLicense(License license) {
		this.license = license;	
	}
	
	public void setSource(String source) {
		this.source = source;
	}
	
	public Long getId() {
		return id;
	}
	
	public void setCreator(String creator) {
		this.creator = creator;
	}
    
	public String getCreator() {
		return creator;
	}
	
	@Override
	public boolean equals(Object other) {
		//check for self-comparison
	    if (this == other) {
	    	return true;
	    }
	    if (other == null || other.getClass() != this.getClass())  {
	    	return false;
	    }
	    Base base = (Base)other;
	    return ObjectUtils.equals(this.created,base.created)
	        && ObjectUtils.equals(this.modified, base.modified)
	        && ObjectUtils.equals(this.license, base.license)
	        && ObjectUtils.equals(this.creator, base.creator);
	}
}
