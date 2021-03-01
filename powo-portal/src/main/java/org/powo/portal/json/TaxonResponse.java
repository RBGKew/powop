package org.powo.portal.json;

import java.util.ArrayList;
import java.util.List;

import org.powo.model.Distribution;
import org.powo.model.Taxon;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonInclude.Include;
import com.vividsolutions.jts.geom.Coordinate;
import com.vividsolutions.jts.geom.Geometry;
import com.vividsolutions.jts.geom.GeometryCollection;
import com.vividsolutions.jts.geom.GeometryFactory;

@JsonInclude(Include.NON_NULL)
public class TaxonResponse {

	private Taxon taxon;
	private List<DistributionResponse> distributions;

	public TaxonResponse(Taxon taxon) {
		this.taxon = taxon;
	}

	public String getScientificName() {
		return taxon == null ? null : taxon.getScientificName();
	}

	public String getAuthor() {
		return taxon == null ? null : taxon.getScientificNameAuthorship();
	}

	public List<DistributionResponse> getDistributions() {
		if (distributions == null && taxon != null) {
			distributions = new ArrayList<>(taxon.getDistribution().size());
			for (Distribution dist : taxon.getDistribution()) {
				distributions.add(new DistributionResponse(dist));
			}
		}

		return distributions;
	}

	public Coordinate[] getDistributionEnvelope() {
		if (taxon == null) {
			return null;
		}

		List<Geometry> list = new ArrayList<Geometry>(taxon.getDistribution().size());
		for (Distribution d : taxon.getDistribution()) {
			list.add(d.getLocation().getEnvelope());
		}

		GeometryCollection geometryCollection = new GeometryCollection(list.toArray(new Geometry[list.size()]), new GeometryFactory());
		return geometryCollection.getEnvelope().getCoordinates();
	}

	public String getError() {
		if (taxon == null) {
			return "Not Found";
		} else {
			return null;
		}
	}
}
