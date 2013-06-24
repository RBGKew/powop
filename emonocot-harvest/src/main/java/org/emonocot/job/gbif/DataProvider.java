package org.emonocot.job.gbif;

import java.util.ArrayList;
import java.util.List;

public class DataProvider {
	
	private String name;
	
	private List<DataResource> dataResources = new ArrayList<DataResource>();
	
	private DataResource dataResource;

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

	public DataResource getDataResource() {
		return dataResource;
	}

	public void setDataResource(DataResource dataResource) {
		this.dataResource = dataResource;
	}
}
