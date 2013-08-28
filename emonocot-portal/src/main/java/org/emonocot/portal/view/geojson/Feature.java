package org.emonocot.portal.view.geojson;

import java.util.HashMap;
import java.util.Map;

import org.apache.commons.lang.StringEscapeUtils;
import org.emonocot.model.TypeAndSpecimen;

public class Feature {
	
	private String type = "Feature";
	
	private Geometry geometry = new Geometry();
	
	private Map<String,String> properties = new HashMap<String,String>();

	public String getType() {
		return type;
	}

	public void setType(String type) {
		this.type = type;
	}

	public Geometry getGeometry() {
		return geometry;
	}

	public void setGeometry(Geometry geometry) {
		this.geometry = geometry;
	}

	public Map<String, String> getProperties() {
		return properties;
	}

	public void setProperties(Map<String, String> properties) {
		this.properties = properties;
	}
	
	public static Feature fromTypeAndSpecimen(TypeAndSpecimen typeAndSpecimen) {
		Feature feature = new Feature();
		addProperty("catalogNumber",typeAndSpecimen.getCatalogNumber(),feature);
		addProperty("collectionCode",typeAndSpecimen.getCollectionCode(),feature);
		addProperty("institutionCode",typeAndSpecimen.getInstitutionCode(),feature);
		addProperty("locality",typeAndSpecimen.getLocality(),feature);
		addProperty("source",typeAndSpecimen.getSource(),feature);
		feature.getGeometry().getCoordinates()[0] = typeAndSpecimen.getDecimalLongitude();
		feature.getGeometry().getCoordinates()[1] = typeAndSpecimen.getDecimalLatitude();
		return feature;
	}
	
	private static void addProperty(String propertyName, String property, Feature feature) {
		if(property != null && !property.isEmpty()) {
			feature.getProperties().put(propertyName, StringEscapeUtils.escapeXml(property));
		}
	}

}
