package org.emonocot.portal.view.geojson;

public class Geometry {
	
	private String type = "Point";
	
	private Double[] coordinates = new Double[2];

	public String getType() {
		return type;
	}

	public void setType(String type) {
		this.type = type;
	}

	public Double[] getCoordinates() {
		return coordinates;
	}

	public void setCoordinates(Double[] coordinates) {
		this.coordinates = coordinates;
	}

}
