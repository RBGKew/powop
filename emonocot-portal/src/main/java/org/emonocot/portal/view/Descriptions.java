package org.emonocot.portal.view;

import java.util.ArrayList;
import java.util.Collection;
import java.util.Collections;
import java.util.Comparator;
import java.util.EnumMap;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Set;

import org.emonocot.model.Description;
import org.emonocot.model.Taxon;
import org.emonocot.model.constants.DescriptionType;
import org.emonocot.model.registry.Organisation;

import com.google.common.collect.ComparisonChain;

/**
 * Transforms list of descriptions into nested structure for display grouped by
 * source and description type.  E.g,
 * 
 *   FTEA => [
 *   	Habit => [Perennial, ...],
 *   	Leaf  => [Basal leaves, ...],
 *   ],
 *   FWTA => [ ... ]
 */
public class Descriptions {

	public class DescriptionsByType {
		public String type;
		public List<Description> descriptions;

		public DescriptionsByType(String type) {
			this.type = type;
			this.descriptions = new ArrayList<>();
		}
	}

	public class DescriptionsBySource implements Comparable<DescriptionsBySource> {
		public List<DescriptionsByType> byType;
		public Organisation source;
		public Taxon asTaxon;
		public boolean isFromSynonym;

		public DescriptionsBySource(Organisation source) {
			this.source = source;
			this.byType = new ArrayList<>();
		}

		@Override
		public int compareTo(DescriptionsBySource o) {
			return ComparisonChain.start()
					.compareFalseFirst(isFromSynonym, o.isFromSynonym)
					.result();
		}
	}

	private Taxon taxon;
	private boolean isUses;
	private List<DescriptionsBySource> descriptionsBySource;
	private Set<DescriptionType> descriptionTypes;

	public Descriptions(Taxon taxon) {
		this(taxon, false);
	}

	public Descriptions(Taxon taxon, boolean isUses) {
		this.taxon = taxon;
		this.isUses = isUses;
		if(isUses) {
			this.descriptionTypes = DescriptionType.getAll(DescriptionType.use);
		} else {
			this.descriptionTypes = DescriptionType.getAllExcept(DescriptionType.use);
		}
	}

	public Collection<DescriptionsBySource> getBySource() {
		if(descriptionsBySource == null) {
			descriptionsBySource = new ArrayList<>();
			Map<Organisation, List<Description>> descriptionsByResource = new HashMap<>();

			if(taxon.isAccepted()) {
				partitionBySource(descriptionsByResource, taxon);
				for(Taxon synonym : taxon.getSynonymNameUsages()) {
					partitionBySource(descriptionsByResource, synonym);
				}
				Collections.sort(descriptionsBySource);
			}
		}

		return descriptionsBySource;
	}

	private void partitionBySource(Map<Organisation, List<Description>> map, Taxon taxon) {
		for(Description description : taxon.getDescriptions()) {
			if(!map.containsKey(description.getAuthority())) {
				map.put(description.getAuthority(), new ArrayList<Description>());
			}
			map.get(description.getAuthority()).add(description);
		}

		for(Map.Entry<Organisation, List<Description>> entry : map.entrySet()) {
			DescriptionsBySource dbs = new DescriptionsBySource(entry.getKey());
			dbs.byType = new ArrayList<>(descriptionsByType(entry.getValue()));

			if(!taxon.isAccepted()) {
				dbs.asTaxon = taxon;
				dbs.isFromSynonym = true;
			}

			if(!dbs.byType.isEmpty()){
				descriptionsBySource.add(dbs);
			}
		}
	}

	private Collection<DescriptionsByType> descriptionsByType(List<Description> descriptions) {
		Map<DescriptionType, DescriptionsByType> byType = new EnumMap<>(DescriptionType.class);
		Comparator<Description> generalDescriptionsFirst = new Comparator<Description>() {
			public int compare(Description d1, Description d2) {
				return d1.getTypes().size() - d2.getTypes().size();
			}
		};

		addDescriptions: for(Description description : descriptions) {
			if(!isUses) {
				// If any of described types are use types, we don't want to add it to the descriptions section
				for(DescriptionType type : description.getTypes()) {
					if(type.isA(DescriptionType.use)) {
						continue addDescriptions;
					}
				}
			}

			addDescription(byType, description);
		}

		for(DescriptionsByType dbt : byType.values()) {
			Collections.sort(dbt.descriptions, generalDescriptionsFirst);
		}

		return byType.values();
	}

	private void addDescription(Map<DescriptionType, DescriptionsByType> byType, Description description) {
		for(DescriptionType type : description.getTypes()) {
			if(descriptionTypes.contains(type)) {
				if(!byType.containsKey(type)) {
					byType.put(type, new DescriptionsByType(type.toString()));
				}
				byType.get(type).descriptions.add(description);
			}
		}
	}
}
