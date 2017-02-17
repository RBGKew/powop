package org.emonocot.model.solr;

import java.lang.reflect.InvocationTargetException;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;
import java.util.TreeSet;
import java.util.UUID;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import org.apache.commons.beanutils.BeanUtils;
import org.apache.commons.lang.ObjectUtils;
import org.apache.solr.common.SolrInputDocument;
import org.emonocot.model.BaseData;
import org.emonocot.model.Description;
import org.emonocot.model.Distribution;
import org.emonocot.model.Image;
import org.emonocot.model.MeasurementOrFact;
import org.emonocot.model.Reference;
import org.emonocot.model.Taxon;
import org.emonocot.model.VernacularName;
import org.emonocot.model.constants.DescriptionType;
import org.emonocot.model.constants.Location;
import org.gbif.ecat.voc.Rank;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.google.common.base.CaseFormat;


public class TaxonSolrInputDocument extends BaseSolrInputDocument {
	
	private static final Pattern fieldPattern = Pattern.compile("taxon.(.*)_\\w{1,2}");

	public static String propertyToSolrField(String propertyName, String type) {
		return String.format("taxon.%s_%s",
				CaseFormat.LOWER_CAMEL.to(CaseFormat.LOWER_UNDERSCORE, propertyName),
				type);
	}

	public static String solrFieldToProperty(String field) {
		Matcher matcher = fieldPattern.matcher(field);
		return CaseFormat.LOWER_UNDERSCORE.to(CaseFormat.LOWER_CAMEL, matcher.matches() ? matcher.group(1) : field);
	}

	private Logger logger = LoggerFactory.getLogger(TaxonSolrInputDocument.class);

	private Set<String> sources;

	private Taxon taxon;

	public TaxonSolrInputDocument(Taxon taxon) {
		super(taxon);
		super.build();
		this.taxon = taxon;
		this.sources = new HashSet<>();
	}

	public SolrInputDocument build() {
		sid.addField("searchable.label_sort", taxon.getScientificName());

		indexRank(Rank.FAMILY, "family");
		indexRank(Rank.Subfamily, "subfamily");
		indexRank(Rank.GENUS, "genus");
		indexRank(Rank.Tribe, "tribe");
		indexRank(Rank.Subtribe, "subtribe");


		addField(sid, "taxon.infraspecific_epithet_s", taxon.getInfraspecificEpithet());
		addField(sid, "taxon.kingdom_s", taxon.getKingdom());
		addField(sid, "taxon.name_published_in_string_s", taxon.getNamePublishedInString());
		addField(sid, "taxon.name_published_in_year_i", taxon.getNamePublishedInYear());
		addField(sid, "taxon.order_s", taxon.getOrder());
		addField(sid, "taxon.scientific_name_authorship_t", taxon.getScientificNameAuthorship());
		addField(sid, "taxon.scientific_name_s", taxon.getScientificName());
		addField(sid, "taxon.scientific_name_t", taxon.getScientificName());
		addSuggest(taxon.getScientificName(), "Name");
		addField(sid, "taxon.specific_epithet_s", taxon.getSpecificEpithet());
		addField(sid, "taxon.subgenus_s", taxon.getSubgenus());
		addField(sid, "taxon.taxon_rank_s", ObjectUtils.toString(taxon.getTaxonRank(), null));
		addField(sid, "taxon.taxonomic_status_s", ObjectUtils.toString(taxon.getTaxonomicStatus(), null));
		addField(sid, "taxon.verbatim_taxon_rank_s", taxon.getVerbatimTaxonRank()); 
		if(taxon.getTaxonRank() == Rank.SPECIES){
			addField(sid, "taxon.species_t", taxon.getScientificName());
		}

		sid.addField("taxon.references_not_empty_b", !taxon.getReferences().isEmpty());
		sid.addField("taxon.types_and_specimens_not_empty_b", !taxon.getTypesAndSpecimens().isEmpty());
		sid.addField("taxon.name_used_b", !taxon.getIdentifications().isEmpty());
		if(taxon.getSynonymNameUsages() != null && !taxon.getSynonymNameUsages().isEmpty()) {
			Set<Taxon> synonymList = taxon.getSynonymNameUsages();
			for(Taxon synonym : synonymList){
				addField(sid, "taxon.synonyms_t", synonym.getScientificName());
			}
		}
		indexDescriptions();
		indexDistributions();
		indexVernacularNames();
		indexMeasurementOrFacts();
		indexImages();

		for(Reference r : taxon.getReferences()) {
			addSource(r);
		}

		for(String source : sources) {
			sid.addField("searchable.sources_ss", source);
		}

		return sid;
	}

	private void indexImages() {
		boolean hasImages = false;
		if(taxon.isAccepted()) {
			hasImages |= !taxon.getImages().isEmpty();
			for(Image i : taxon.getImages()) {
				addSource(i);
			}

			for(Taxon synonym : taxon.getSynonymNameUsages()) {
				hasImages |= !synonym.getImages().isEmpty();
				for(Image i : synonym.getImages()) {
					addSource(i);
				}
			}
		}

		sid.addField("taxon.images_not_empty_b", hasImages);
	}

	private void indexMeasurementOrFacts() {
		sid.addField("taxon.measurements_or_facts_not_empty_b", !taxon.getMeasurementsOrFacts().isEmpty());
		for(MeasurementOrFact m : taxon.getMeasurementsOrFacts()) {
			String typeName = CaseFormat.LOWER_CAMEL.to(CaseFormat.LOWER_UNDERSCORE, m.getMeasurementType().simpleName());
			// If a unit is specified, assume it is a numeric measurement
			if(m.getMeasurementUnit() != null) {
				try {
					Double value = Double.parseDouble(m.getMeasurementValue());
					sid.addField("taxon.measurement_" + typeName + "_ds", value);
				} catch (NumberFormatException e) {
					logger.warn("Could not parse {} as a double", m.getMeasurementValue());
				}
			} else {
				sid.addField("taxon.fact_" + typeName + "_ss", m.getMeasurementValue());
			}

			addSource(m);
		}
	}

	private void indexDescriptions() {
		boolean hasDescriptions = false;
		if(taxon.isAccepted()) {
			hasDescriptions |= doIndexDescriptions(taxon);
			for(Taxon synonym : taxon.getSynonymNameUsages()) {
				hasDescriptions |= doIndexDescriptions(synonym);
			}
		}

		sid.addField("taxon.descriptions_not_empty_b", hasDescriptions);
	}

	private boolean doIndexDescriptions(Taxon t) {
		boolean hasDescriptions = false;
		for(Description d : t.getDescriptions()) {
			hasDescriptions = true;
			if(d.getType() != null) {
				if(d.getType().hasSearchCategory()) {
					sid.addField(String.format("taxon.description_%s_t", d.getType().getSearchCategory()), d.getDescription());
				} else if(DescriptionType.getAll(DescriptionType.use).contains(d.getType())) {
					sid.addField("taxon.description_use_t", d.getDescription());
				}
			}
			sid.addField("taxon.description_t", d.getDescription());
			addSource(d);
		}

		return hasDescriptions;
	}

	private void indexDistributions() {
		sid.addField("taxon.distribution_not_empty_b", !taxon.getDistribution().isEmpty());
		TreeSet<String> locationNames = new TreeSet<>();
		TreeSet<String> locationCodes = new TreeSet<>();
		for(Distribution d : taxon.getDistribution()) {
			locationNames.add(d.getLocation().getName());
			locationCodes.add(d.getLocation().getCode());

			indexChildLocations(locationNames, locationCodes, d.getLocation().getChildren());
			indexParentLocations(locationNames, locationCodes, d.getLocation().getParent());
			addSource(d);
		}

		for(String name : locationNames) {
			sid.addField("taxon.distribution_t", name);
			addSuggest(name, "Location");
		}
	}

	private void indexParentLocations(Set<String> names, Set<String> codes, Location parent) {
		if(parent == null) {
			return;
		}

		names.add(parent.getName());
		codes.add(parent.getCode());
		indexParentLocations(names, codes, parent.getParent());
	}

	private void indexChildLocations(Set<String> names, Set<String> codes, Set<Location> locations) {
		if(locations == null) {
			return;
		}

		for(Location location : locations) {
			names.add(location.getName());
			codes.add(location.getCode());
			indexChildLocations(names, codes, location.getChildren());
		}
	}

	private void indexRank(Rank rank, String property) {
		String solrField = propertyToSolrField(property, "t");
		try {
			if(rank.equals(taxon.getTaxonRank()) && BeanUtils.getProperty(taxon, property) == null) {
				addField(sid, solrField, taxon.getScientificName());
			} else {
				addField(sid, solrField, BeanUtils.getProperty(taxon, property));
			}

			// When a taxon is an accepted name, the synonyms should also be indexed
			if(taxon.getSynonymNameUsages() != null && !taxon.getSynonymNameUsages().isEmpty()) {
				Set<Taxon> synonymList = taxon.getSynonymNameUsages();
				for(Taxon taxon : synonymList) {
					if(rank.equals(taxon.getTaxonRank()) && BeanUtils.getProperty(taxon, property) == null) {
						addField(sid, solrField, taxon.getScientificName());
					} else {
						addField(sid, solrField, BeanUtils.getProperty(taxon, property));
					}
				}

			}
		} catch (IllegalAccessException | InvocationTargetException | NoSuchMethodException e) {
			logger.error("Error getting property {} from taxon. Does get{}() exist?", property, property);
		}
	}

	private void indexVernacularNames() {
		sid.addField("taxon.vernacular_names_not_empty_b", !taxon.getVernacularNames().isEmpty());
		for(VernacularName v : taxon.getVernacularNames()) {
			sid.addField("taxon.vernacular_names_t", v.getVernacularName());
			addSuggest(v.getVernacularName(), "Common Name");
			addSource(v);
		}
	}
	
	private void addSuggest(String text, String fieldName) {
		SolrInputDocument child = new SolrInputDocument();
		child.addField("id", UUID.randomUUID());
		child.addField("suggester.text_t", text);
		child.addField("suggester.payload_s", fieldName);
		sid.addChildDocument(child);
	}
	
	private void addSource(BaseData d) {
		if(d.getAuthority() != null) {
			sources.add(d.getAuthority().getIdentifier());
		}
	}
}
