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
package org.powo.portal.view.geojson;

import java.util.HashMap;
import java.util.Map;

import org.apache.commons.lang3.StringEscapeUtils;
import org.powo.model.TypeAndSpecimen;

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
