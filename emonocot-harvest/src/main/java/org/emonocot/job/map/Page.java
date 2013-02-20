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
