package org.emonocot.job.gbif;

import java.util.ArrayList;
import java.util.List;

public class DataProvider {
	
	private String name;
	
	private List<DataResource> dataResources = new ArrayList<DataResource>();
	
	private List<DataResource> resources = new ArrayList<DataResource>();

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public List<DataResource> getDataResources() {
		return dataResources;
	}

	public void setDataResources(List<DataResource> dataResources) {
		this.dataResources = dataResources;
	}

	public List<DataResource> getResources() {
		return resources;
	}

	public void setResources(List<DataResource> resources) {
		this.resources = resources;
	}	
}
