package org.emonocot.job.map;

import java.util.ArrayList;
import java.util.List;

public class MapSpec {
	
	private String layout = "A4 portrait";
	
	private String title = "";
	
	private boolean geodetic = false;
	
	private String srs = "EPSG:3857";
	
	private int dpi = 300;
	
	private String units = "m";
	
	private String outputFormat = "png";
	
	private List<Layer> layers = new ArrayList<Layer>();
	
	private List<Page> pages = new ArrayList<Page>();

	public String getLayout() {
		return layout;
	}

	public void setLayout(String layout) {
		this.layout = layout;
	}

	public String getTitle() {
		return title;
	}

	public void setTitle(String title) {
		this.title = title;
	}

	public String getSrs() {
		return srs;
	}

	public void setSrs(String srs) {
		this.srs = srs;
	}

	public int getDpi() {
		return dpi;
	}

	public void setDpi(int dpi) {
		this.dpi = dpi;
	}

	public String getUnits() {
		return units;
	}

	public void setUnits(String units) {
		this.units = units;
	}

	public String getOutputFormat() {
		return outputFormat;
	}

	public void setOutputFormat(String outputFormat) {
		this.outputFormat = outputFormat;
	}

	public List<Layer> getLayers() {
		return layers;
	}

	public void setLayers(List<Layer> layers) {
		this.layers = layers;
	}

	public List<Page> getPages() {
		return pages;
	}

	public void setPages(List<Page> pages) {
		this.pages = pages;
	}

	public boolean isGeodetic() {
		return geodetic;
	}

	public void setGeodetic(boolean geodetic) {
		this.geodetic = geodetic;
	}
	
}
