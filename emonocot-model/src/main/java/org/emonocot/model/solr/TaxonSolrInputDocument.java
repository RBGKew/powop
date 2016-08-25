package org.emonocot.model.solr;

import java.lang.reflect.InvocationTargetException;
import java.util.HashSet;
import java.util.Set;
import java.util.TreeSet;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import org.apache.commons.beanutils.BeanUtils;
import org.apache.commons.lang.ObjectUtils;
import org.apache.solr.common.SolrInputDocument;
import org.emonocot.model.Description;
import org.emonocot.model.Distribution;
import org.emonocot.model.Image;
import org.emonocot.model.MeasurementOrFact;
import org.emonocot.model.Reference;
import org.emonocot.model.Taxon;
import org.emonocot.model.VernacularName;
import org.emonocot.model.constants.Location;
import org.gbif.ecat.voc.Rank;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.google.common.base.CaseFormat;


public class TaxonSolrInputDocument extends BaseSolrInputDocument {

	public static String propertyToSolrField(String propertyName, String type) {
		return String.format("taxon.%s_%s",
				CaseFormat.LOWER_CAMEL.to(CaseFormat.LOWER_UNDERSCORE, propertyName),
				type);
	}

	public static String solrFieldToProperty(String field) {
		Pattern pattern = Pattern.compile("taxon.(.*)_\\w{1,2}");
		Matcher matcher = pattern.matcher(field);
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
		addField(sid, "taxon.name_published_in_string_s", taxon.getNamePublishedInString());
		addField(sid, "taxon.name_published_in_year_i", taxon.getNamePublishedInYear());
		addField(sid, "taxon.order_s", taxon.getOrder());
		addField(sid, "taxon.scientific_name_authorship_s", taxon.getScientificNameAuthorship());
		addField(sid, "taxon.scientific_name_t", taxon.getScientificName());
		addField(sid, "taxon.specific_epithet_s", taxon.getSpecificEpithet());
		addField(sid, "taxon.subgenus_s", taxon.getSubgenus());
		addField(sid, "taxon.taxon_rank_s", ObjectUtils.toString(taxon.getTaxonRank(), null));
		addField(sid, "taxon.taxonomic_status_s", ObjectUtils.toString(taxon.getTaxonomicStatus(), null));
		addField(sid, "taxon.verbatim_taxon_rank_s", taxon.getVerbatimTaxonRank()); 
		if(taxon.getTaxonRank() == Rank.SPECIES){
			addField(sid, "taxon.species_ss", taxon.getScientificName());
		}
		sid.addField("taxon.descriptions_not_empty_b", !taxon.getDescriptions().isEmpty());
		sid.addField("taxon.distribution_not_empty_b", !taxon.getDistribution().isEmpty());
		sid.addField("taxon.images_not_empty_b", !taxon.getImages().isEmpty());
		sid.addField("taxon.measurements_or_facts_not_empty_b", !taxon.getMeasurementsOrFacts().isEmpty());
		sid.addField("taxon.references_not_empty_b", !taxon.getReferences().isEmpty());
		sid.addField("taxon.types_and_specimens_not_empty_b", !taxon.getTypesAndSpecimens().isEmpty());
		sid.addField("taxon.vernacular_names_not_empty_b", !taxon.getVernacularNames().isEmpty());
		sid.addField("taxon.name_used_b", !taxon.getIdentifications().isEmpty());
		sid.addField("taxon.has_data_b", hasUsefulData(sid));
		if(taxon.getSynonymNameUsages() != null && !taxon.getSynonymNameUsages().isEmpty()) {
			Set<Taxon> synonymList = taxon.getSynonymNameUsages();
			for(Taxon synonym : synonymList){
				addField(sid, "taxon.scientific_name_t", synonym.getScientificName());
			}			
		}
		indexDescriptions();
		indexDistributions();
		indexVernacularNames();
		indexMeasurementOrFacts();

		for(Image i : taxon.getImages()) {
			if(i != null && i.getAuthority() != null) {
				sources.add(i.getAuthority().getIdentifier());
			}
		}

		for(Reference r : taxon.getReferences()) {
			if(r != null && r.getAuthority() != null) {
				sources.add(r.getAuthority().getIdentifier());
			}
		}
		for(String source : sources) {
			sid.addField("searchable.sources_ss", source);
		}

		return sid;
	}

	private void indexMeasurementOrFacts() {
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

			if(m.getAuthority() != null) {
				sources.add(m.getAuthority().getIdentifier());
			}
		}
	}

	/*
	 * Used to determine if a taxon has useful data or if it is just a name
	 */
	private boolean hasUsefulData(SolrInputDocument sid) {
		String[] usefulFields = {
				"taxon.descriptions_not_empty_b",
				"taxon.distribution_not_empty_b",
				"taxon.images_not_empty_b",
				"taxon.measurements_or_facts_not_empty_b",
				"taxon.name_used_b",
				"taxon.references_not_empty_b",
				"taxon.types_and_specimens_not_empty_b",
				"taxon.vernacular_names_not_empty_b"
		};

		for(String field : usefulFields) {
			if((Boolean)sid.getFieldValue(field)) {
				return true;
			}
		}

		return false;
	}

	private void indexDescriptions() {
		for(Description d : taxon.getDescriptions()) {
			if(d.getType() != null && d.getType().hasSearchCategory()) {
				sid.addField(String.format("taxon.description_%s_t", d.getType().getSearchCategory()), d.getDescription());
			}
			sid.addField("taxon.description_t", d.getDescription());

			if(d.getAuthority() != null) {
				sources.add(d.getAuthority().getIdentifier());
			}
		}

	}

	private void indexDistributions() {
		TreeSet<String> locationNames = new TreeSet<>();
		TreeSet<String> locationCodes = new TreeSet<>();
		//Distributions need splitting into all/native/introduced. Naming convention: 
		//taxon.distribution_ss
		//taxon.distribution_native_ss
		//taxon.distribution_introduced_ss
		for(Distribution d : taxon.getDistribution()) {
			locationNames.add(d.getLocation().getName());
			locationCodes.add(d.getLocation().getCode());

			indexChildLocations(locationNames, locationCodes, d.getLocation().getChildren());
			indexParentLocations(locationNames, locationCodes, d.getLocation().getParent());

			if(d.getAuthority() != null) {
				sources.add(d.getAuthority().getIdentifier());
			}
		}

		for(String name : locationNames) {
			sid.addField("taxon.distribution_ss", name);
		}
		for(String code : locationCodes) {
			sid.addField("taxon.distribution_code_ss", code);
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
		String solrField = propertyToSolrField(property, "ss");
		try {
			if(rank.equals(taxon.getTaxonRank()) && BeanUtils.getProperty(taxon, property) == null) {
				addField(sid, solrField, taxon.getScientificName());
			} else {
				addField(sid, solrField, BeanUtils.getProperty(taxon, property));
			}

			// When a taxon is an accepted name, the synonyms should also be indexed
			if(taxon.getSynonymNameUsages() != null && !taxon.getSynonymNameUsages().isEmpty()) {
				Set<Taxon> synonymList = taxon.getSynonymNameUsages();
				for(Taxon taxon : synonymList){
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
		for(VernacularName v : taxon.getVernacularNames()) {
			sid.addField("taxon.vernacular_names_ss", v.getVernacularName());
			if(v.getAuthority() != null) {
				sources.add(v.getAuthority().getIdentifier());
			}
		}
	}
}
