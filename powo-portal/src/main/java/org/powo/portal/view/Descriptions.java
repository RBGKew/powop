package org.powo.portal.view;

import java.util.ArrayList;
import java.util.Collection;
import java.util.Collections;
import java.util.Comparator;
import java.util.EnumMap;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Objects;
import java.util.Set;
import org.powo.model.Description;
import org.powo.model.Taxon;
import org.powo.model.constants.DescriptionType;
import org.powo.model.marshall.json.BaseSerializer;
import org.powo.model.registry.Organisation;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.databind.annotation.JsonSerialize;
import com.google.common.base.Strings;
import com.google.common.collect.ComparisonChain;
import com.google.common.collect.ImmutableList;
import com.google.common.collect.Ordering;
import com.google.common.primitives.Longs;

import lombok.Getter;

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

	@Getter
	public class DescriptionsByType {
		public DescriptionType type;
		public List<Description> descriptions;

		public DescriptionsByType(DescriptionType type) {
			this.type = type;
			this.descriptions = new ArrayList<>();
		}
	}

	@Getter
	public class DescriptionsBySource implements Comparable<DescriptionsBySource> {
		public List<DescriptionsByType> byType;

		@JsonIgnore
		public final Organisation source;

		@JsonSerialize(using = BaseSerializer.class)
		public final Taxon asTaxon;

		@JsonIgnore
		public boolean isFromSynonym;

		public String conceptSource;

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
			return Objects.hash(source.getTitle(), asTaxon.getScientificName(), isFromSynonym);
		}

		public String getSourceTitle() {
			return source.getTitle();
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

	@JsonSerialize(contentUsing = BaseSerializer.class)
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

			if(taxon.looksAccepted()) {
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

			if(!dbs.asTaxon.looksAccepted()) {
				dbs.isFromSynonym = true;
			}

			if(!dbs.byType.isEmpty()){
				// set concept source unless it looks like a URL. We make the assumption that the source
				// of every description for a given taxon is the same per organisation
				String source = dbs.byType.get(0).descriptions.get(0).getSource();
				if(!Strings.isNullOrEmpty(source) && !(source.startsWith("http://") || source.startsWith("https://") || source.startsWith("www."))) {
					dbs.conceptSource = source;
				}

				descriptionsBySource.add(dbs);
			}
		}
	}

	// Blacklist of description types that should not show up in the taxon display
	private static final ImmutableList<DescriptionType> doNotDisplay = ImmutableList.<DescriptionType>of(DescriptionType.summary, DescriptionType.snippet);

	// Descriptions with multiple types (e.g., morphology:general:flower|sex:female) should come
	// after ones with a single type
	private static final Comparator<Description> generalDescriptionsFirst = new Comparator<Description>() {
		public int compare(Description d1, Description d2) {
			return d1.getTypes().size() - d2.getTypes().size();
		}
	};

	private static final Ordering<DescriptionsByType> documentOrder = new Ordering<DescriptionsByType>() {
		public int compare(DescriptionsByType left, DescriptionsByType right) {
			return Longs.compare(left.descriptions.get(0).getId(), right.descriptions.get(0).getId());
		}
	};

	private Collection<DescriptionsByType> descriptionsByType(List<Description> descriptions) {
		Map<DescriptionType, DescriptionsByType> byType = new EnumMap<>(DescriptionType.class);

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

		return documentOrder.sortedCopy(byType.values());
	}

	private void addDescription(Map<DescriptionType, DescriptionsByType> byType, Description description) {
		for(DescriptionType type : description.getTypes()) {
			if(descriptionTypes.contains(type)) {
				if(!byType.containsKey(type)) {
					byType.put(type, new DescriptionsByType(type));
				}
				byType.get(type).descriptions.add(description);
			}
		}
	}
}
