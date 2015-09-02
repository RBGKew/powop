/*
 * This is eMonocot, a global online biodiversity information resource.
 *
 * Copyright © 2011–2015 The Board of Trustees of the Royal Botanic Gardens, Kew and The University of Oxford
 *
 * eMonocot is free software: you can redistribute it and/or modify it under the terms of the
 * GNU Affero General Public License as published by the Free Software Foundation, either version 3
 * of the License, or (at your option) any later version.
 *
 * eMonocot is distributed in the hope that it will be useful, but WITHOUT ANY WARRANTY; without even
 * the implied warranty of MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 * GNU Affero General Public License for more details.
 *
 * The complete text of the GNU Affero General Public License is in the source repository as the file
 * ‘COPYING’.  It is also available from <http://www.gnu.org/licenses/>.
 */
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
