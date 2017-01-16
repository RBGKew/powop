package org.emonocot.portal.view;

import java.util.ArrayList;
import java.util.Collection;
import java.util.Collections;
import java.util.Comparator;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Objects;
import java.util.Set;
import java.util.SortedMap;
import java.util.TreeMap;

import org.emonocot.model.Description;
import org.emonocot.model.Taxon;
import org.emonocot.model.constants.DescriptionType;
import org.emonocot.model.registry.Organisation;

import com.google.common.collect.ComparisonChain;
import com.google.common.collect.ImmutableList;

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

	public class DescriptionsByType implements Comparable<DescriptionsByType> {
		public String type;
		public List<Description> descriptions;

		public DescriptionsByType(String type) {
			this.type = type;
			this.descriptions = new ArrayList<>();
		}

		@Override
		public int compareTo(DescriptionsByType o) {
			return descriptions.get(0).getId().compareTo(o.descriptions.get(0).getId());
		}
	}

	public class DescriptionsBySource implements Comparable<DescriptionsBySource> {
		public List<DescriptionsByType> byType;
		public final Organisation source;
		public final Taxon asTaxon;
		public boolean isFromSynonym;

		public DescriptionsBySource(Organisation source, Taxon asTaxon) {
			this.source = source;
			this.asTaxon = asTaxon;
			this.byType = new ArrayList<>();
		}

		@Override
		public int compareTo(DescriptionsBySource other) {
			return ComparisonChain.start()
					.compareFalseFirst(isFromSynonym, other.isFromSynonym)
					.result();
		}

		@Override
		public boolean equals(Object other) {
			if(other == null) return false;
			if(getClass() != other.getClass()) return false;
			final DescriptionsBySource dbs = (DescriptionsBySource)other;
			return Objects.equals(source, dbs.source)
					&& Objects.equals(asTaxon, dbs.asTaxon);
		}

		@Override
		public int hashCode() {
			return Objects.hash( source.getTitle(), asTaxon.getScientificName(), isFromSynonym);
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

	public List<Taxon> getSynonymsIncluded() {
		List<Taxon> synonymsIncluded = new ArrayList<>();
		if(descriptionsBySource == null) {
			getBySource();
		}
		for(DescriptionsBySource desc : descriptionsBySource){
			if(desc.isFromSynonym){
				if(!synonymsIncluded.contains(desc.asTaxon)){
				synonymsIncluded.add(desc.asTaxon);
				}
			}
		}
		return synonymsIncluded;
	}
	
	public Collection<DescriptionsBySource> getBySource() {
		if(descriptionsBySource == null) {
			descriptionsBySource = new ArrayList<>();

			if(taxon.isAccepted()) {
				partitionBySource(taxon);
				for(Taxon synonym : taxon.getSynonymNameUsages()) {
					partitionBySource(synonym);
				}
				Collections.sort(descriptionsBySource);
			}
		}

		return descriptionsBySource;
	}

	private void partitionBySource(Taxon tx) {
		Map<DescriptionsBySource, List<Description>> map = new HashMap<>();
		for(Description description : tx.getDescriptions()) {
			DescriptionsBySource dbs = new DescriptionsBySource(description.getAuthority(), tx);
			if(!map.containsKey(dbs)) {
				map.put(dbs, new ArrayList<Description>());
			}

			map.get(dbs).add(description);
		}

		for(Map.Entry<DescriptionsBySource, List<Description>> entry : map.entrySet()) {
			DescriptionsBySource dbs = entry.getKey();
			dbs.byType = new ArrayList<>(descriptionsByType(entry.getValue()));

			if(!dbs.asTaxon.isAccepted()) {
				dbs.isFromSynonym = true;
			}

			if(!dbs.byType.isEmpty()){
				descriptionsBySource.add(dbs);
			}
		}
	}

	private static final ImmutableList<DescriptionType> doNotDisplay = ImmutableList.<DescriptionType>of(DescriptionType.summary, DescriptionType.snippet);
	private Collection<DescriptionsByType> descriptionsByType(List<Description> descriptions) {
		SortedMap<DescriptionType, DescriptionsByType> byType = new TreeMap<>();

		// Descriptions with multiple types (e.g., morphology:general:flower|sex:female) should come
		// after ones with a single type
		Comparator<Description> generalDescriptionsFirst = new Comparator<Description>() {
			public int compare(Description d1, Description d2) {
				return d1.getTypes().size() - d2.getTypes().size();
			}
		};

		addDescriptions: for(Description description : descriptions) {
			for(DescriptionType type : description.getTypes()) {
				// If any of described types are use types, we don't want to add it to the descriptions section
				if(!isUses && type.isA(DescriptionType.use)) {
					continue addDescriptions;
				}

				if(doNotDisplay.contains(type)) {
					continue addDescriptions;
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
