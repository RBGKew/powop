package org.tdwg.ubif;

import com.thoughtworks.xstream.annotations.XStreamAsAttribute;

public class Generator {
	
	@XStreamAsAttribute
	private String name;
	
	@XStreamAsAttribute
	private String notes;
	
	@XStreamAsAttribute
	private String version;
	
	@XStreamAsAttribute
	private String routine;

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public String getNotes() {
		return notes;
	}

	public void setNotes(String notes) {
		this.notes = notes;
	}

	public String getVersion() {
		return version;
	}

	public void setVersion(String version) {
		this.version = version;
	}

	public String getRoutine() {
		return routine;
	}

	public void setRoutine(String routine) {
		this.routine = routine;
	}

}
