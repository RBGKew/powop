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

public class Page {

	private double[] center;
	private double scale;
	private String mapTitle = "";
	private double[] bbox;
	private String comment = "";
	private boolean geodetic;
	private int rotation;

	public double[] getCenter() {
		return center;
	}
	public void setCenter(double[] center) {
		this.center = center;
	}
	public double getScale() {
		return scale;
	}
	public void setScale(double scale) {
		this.scale = scale;
	}
	public String getMapTitle() {
		return mapTitle;
	}
	public void setMapTitle(String mapTitle) {
		this.mapTitle = mapTitle;
	}
	public String getComment() {
		return comment;
	}
	public void setComment(String comment) {
		this.comment = comment;
	}
	public int getRotation() {
		return rotation;
	}
	public void setRotation(int rotation) {
		this.rotation = rotation;
	}
	public boolean getGeodetic() {
		return geodetic;
	}
	public void setGeodetic(boolean geodetic) {
		this.geodetic = geodetic;
	}
	public double[] getBbox() {
		return bbox;
	}
	public void setBbox(double[] bbox) {
		this.bbox = bbox;
	}



}
