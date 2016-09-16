package org.emonocot.portal.json;

import java.util.ArrayList;
import java.util.List;

import org.emonocot.model.Distribution;
import org.emonocot.model.Taxon;

import com.vividsolutions.jts.geom.Coordinate;
import com.vividsolutions.jts.geom.Geometry;
import com.vividsolutions.jts.geom.GeometryCollection;
import com.vividsolutions.jts.geom.GeometryFactory;

public class TaxonResponse {

	private Taxon taxon;
	private List<DistributionResponse> distributions;

	public TaxonResponse(Taxon taxon) {
		this.taxon = taxon;
	}

	public String getScientificName() {
		return taxon.getScientificName();
	}

	public String getAuthor() {
		return taxon.getScientificNameAuthorship();
	}

	public List<DistributionResponse> getDistributions() {
		if(distributions == null) {
			distributions = new ArrayList<>(taxon.getDistribution().size());
			for(Distribution dist : taxon.getDistribution()) {
				distributions.add(new DistributionResponse(dist));
			}
		}

		return distributions;
	}

	public Coordinate[] getDistributionEnvelope() {
		List<Geometry> list = new ArrayList<Geometry>(taxon.getDistribution().size());
		for (Distribution d : taxon.getDistribution()) {
			list.add(d.getLocation().getEnvelope());
		}

		GeometryCollection geometryCollection = new GeometryCollection(list.toArray(new Geometry[list.size()]), new GeometryFactory());
		return geometryCollection.getEnvelope().getCoordinates();
	}
}
