package org.powo.portal.json;

import org.powo.model.Distribution;
import org.gbif.ecat.voc.EstablishmentMeans;
import org.powo.portal.view.Distributions;

public class DistributionResponse {

	private Distribution distribution;

	public DistributionResponse(Distribution distribution) {
		this.distribution = distribution;
	}

	public String getFeatureId() {
		return distribution.getLocation().getFeatureId().toString();
	}

	public String getTdwgCode() {
		return distribution.getLocation().getCode();
	}

	public int getTdwgLevel() {
		// Levels are 0 indexed in Location and 1 indexed in GeoServer
		return distribution.getLocation().getLevel() + 1;
	}

	public String getName() {
		return distribution.getLocation().getName();
	}

	public EstablishmentMeans getEstablishment() {
		return Distributions.nativeOrIntroduced(distribution.getEstablishmentMeans());
	}
}
