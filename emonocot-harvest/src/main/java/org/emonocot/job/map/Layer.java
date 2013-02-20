package org.emonocot.job.map;

import java.util.HashMap;
import java.util.Map;

public class Layer {
	
	private String type = "";
	
	private String baseURL = "";
	
	private double[] maxExtent = new double[4];
	
	private boolean wrapDateLine = true;

	private int[] tileSize = new int[] {256,256};
	
	private double[] resolutions = new double[0];
	
	public Map<String,String> customParams = new HashMap<String,String>();
	
	private String format = "png";

	public String getType() {
		return type;
	}

	public void setType(String type) {
		this.type = type;
	}

	public String getBaseURL() {
		return baseURL;
	}

	public void setBaseURL(String baseURL) {
		this.baseURL = baseURL;
	}

	public double[] getMaxExtent() {
		return maxExtent;
	}

	public void setMaxExtent(double[] maxExtent) {
		this.maxExtent = maxExtent;
	}

	public int[] getTileSize() {
		return tileSize;
	}

	public void setTileSize(int[] tileSize) {
		this.tileSize = tileSize;
	}

	public double[] getResolutions() {
		return resolutions;
	}

	public void setResolutions(double[] resolutions) {
		this.resolutions = resolutions;
	}

	public String getFormat() {
		return format;
	}

	public void setFormat(String format) {
		this.format = format;
	}

	public boolean getWrapDateLine() {
		return wrapDateLine;
	}

	public void setWrapDateLine(boolean wrapDateLine) {
		this.wrapDateLine = wrapDateLine;
	}
	
	public Map<String, String> getCustomParams() {
		return customParams;
	}

	public void setCustomParams(Map<String, String> customParams) {
		this.customParams = customParams;
	}
	
}
