package org.powo.portal.json.v2;

import java.util.List;
import java.util.Map;

import org.powo.model.Description;
import org.powo.model.Taxon;
import org.powo.model.constants.DescriptionType;
import org.powo.model.registry.Organisation;

import lombok.Getter;
import lombok.Setter;

@Setter
public class DescriptionsBySource {
	private Taxon asTaxon;

	private Organisation source;

	@Getter
	private boolean fromSynonym;

	@Getter
	private Map<DescriptionType, List<Description>> descriptions;

	public String getSource() {
		return source.getTitle();
	}

	public String getAsTaxon() {
		return asTaxon.getIdentifier();
	}
}
