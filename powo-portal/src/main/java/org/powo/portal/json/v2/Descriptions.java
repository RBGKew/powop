package org.powo.portal.json.v2;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import org.powo.model.Description;
import org.powo.model.Taxon;
import org.powo.model.constants.DescriptionType;
import org.powo.model.registry.Organisation;

import static java.util.stream.Collectors.groupingBy;
import static java.util.stream.Collectors.toList;

public class Descriptions extends HashMap<String, DescriptionsBySource> {

	private static final long serialVersionUID = -1392378268262660206L;

	public Descriptions(Taxon taxon) {
		partitionBySource(taxon, false);
		for(Taxon synonym : taxon.getSynonymNameUsages()) {
			partitionBySource(synonym, true);
		}
	}

	private void partitionBySource(Taxon taxon, boolean fromSynonym) {
		if (taxon.looksAccepted()) {
			Map<Organisation, List<Description>> res = taxon.getDescriptions().stream()
					.collect(groupingBy(Description::getAuthority, toList()));

			for(Organisation source : res.keySet()) {
				DescriptionsBySource dbs = new DescriptionsBySource();
				dbs.setSource(source);
				dbs.setAsTaxon(taxon);
				dbs.setDescriptions(partitionByType(res.get(source)));
				dbs.setFromSynonym(fromSynonym);
				put(source.getAbbreviation(), dbs);
			}
		}
	}

	private Map<DescriptionType, List<Description>> partitionByType(List<Description> desc) {
		return desc.stream()
				.collect(groupingBy(Description::getType, toList()));
	}
}
