package org.powo.model.solr;

import java.lang.reflect.InvocationTargetException;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Objects;
import java.util.Set;
import java.util.TreeSet;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import org.apache.commons.beanutils.BeanUtils;
import org.apache.solr.common.SolrInputDocument;
import org.gbif.ecat.voc.Rank;
import org.powo.api.ImageService;
import org.powo.common.HtmlSanitizer;
import org.powo.model.BaseData;
import org.powo.model.Description;
import org.powo.model.Distribution;
import org.powo.model.Image;
import org.powo.model.MeasurementOrFact;
import org.powo.model.Reference;
import org.powo.model.Taxon;
import org.powo.model.VernacularName;
import org.powo.model.constants.DescriptionType;
import org.powo.model.constants.Location;
import org.powo.model.helpers.CDNImageHelper;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.context.ApplicationContext;

import com.google.common.base.CaseFormat;

public class TaxonSolrInputDocument extends BaseSolrInputDocument {

	private static final Pattern fieldPattern = Pattern.compile("taxon.(.*)_(s|s_lower|ss_lower|t|b|i)");

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

	private ImageService imageService;

	private CDNImageHelper cdn;

	public TaxonSolrInputDocument(Taxon taxon) {
		this(taxon, null);
	}

	public TaxonSolrInputDocument(Taxon taxon, ApplicationContext ctx) {
		super(taxon);
		super.build();
		this.taxon = taxon;
		this.sources = new HashSet<>();
		if(ctx != null) {
			this.imageService = ctx.getBean(ImageService.class);
			this.cdn = ctx.getBean(CDNImageHelper.class);
		}
	}

	public SolrInputDocument build() {
		indexRank(Rank.FAMILY, "family");
		indexRank(Rank.Subfamily, "subfamily");
		indexRank(Rank.GENUS, "genus");
		indexRank(Rank.Tribe, "tribe");
		indexRank(Rank.Subtribe, "subtribe");

		addField(sid, "taxon.identifier_s", taxon.getIdentifier());
		addField(sid, "taxon.infraspecific_epithet_s_lower", taxon.getInfraspecificEpithet());
		addField(sid, "taxon.kingdom_s_lower", taxon.getKingdom());
		addField(sid, "taxon.order_s_lower", taxon.getOrder());
		addField(sid, "taxon.name_published_in_year_i", taxon.getNamePublishedInYear());
		addField(sid, "taxon.name_published_in_s_lower", taxon.getNamePublishedInString());
		addField(sid, "taxon.scientific_name_authorship_t", taxon.getScientificNameAuthorship());
		addField(sid, "taxon.scientific_name_s_lower", taxon.getScientificName());
		addField(sid, "taxon.specific_epithet_s_lower", taxon.getSpecificEpithet());
		addField(sid, "taxon.rank_s_lower", Objects.toString(taxon.getTaxonRank(), null));
		addField(sid, "taxon.taxonomic_status_s_lower", Objects.toString(taxon.getTaxonomicStatus(), null));
		addField(sid, "taxon.is_accepted_b", taxon.isAccepted());
		addField(sid, "taxon.looks_accepted_b", taxon.looksAccepted());
		addField(sid, "taxon.is_unplaced_b", taxon.getTaxonomicStatus() == null);
		if(taxon.getTaxonRank() == Rank.SPECIES){
			addField(sid, "taxon.species_s_lower", taxon.getScientificName());
		}

		if(taxon.getSynonymNameUsages() != null && !taxon.getSynonymNameUsages().isEmpty()) {
			Set<Taxon> synonymList = taxon.getSynonymNameUsages();
			for(Taxon synonym : synonymList){
				addField(sid, "taxon.synonyms_ss_lower", synonym.getScientificName());
			}
		}

		if(taxon.getAcceptedNameUsage() != null) {
			addField(sid, "taxon.accepted.identifier_s", taxon.getAcceptedNameUsage().getIdentifier());
			addField(sid, "taxon.accepted.scientific_name_s_lower", taxon.getAcceptedNameUsage().getScientificName());
			addField(sid, "taxon.accepted.scientific_name_authorship_t", taxon.getAcceptedNameUsage().getScientificNameAuthorship());
			addField(sid, "taxon.accepted.kingdom_s", taxon.getAcceptedNameUsage().getKingdom());
		}

		indexDescriptions();
		indexDistributions();
		indexVernacularNames();
		indexMeasurementOrFacts();
		indexImages();
		addSuggestionWeight();

		for(Reference r : taxon.getReferences()) {
			addSource(r);
		}

		for(String source : sources) {
			sid.addField("searchable.sources_ss", source);
			sid.addField("searchable.context_ss", normalized(source));
		}

		buildSortField();

		return sid;
	}

	private Object normalized(String source) {
		if (source != null) {
			return source.replaceAll("-", "");
		} else {
			return source;
		}
	}

	private void buildSortField() {
		StringBuilder sortable = new StringBuilder();
		if(taxon.getTaxonRank() != null) {
			sortable.append(taxon.getTaxonRank().termID());
		}
		if (!Rank.FAMILY.equals(taxon.getTaxonRank()) && taxon.getFamily() != null) {
			sortable.append(taxon.getFamily());
		}
		if(taxon.getScientificName() != null) {
			sortable.append(taxon.getScientificName().replaceAll("\\s", ""));
		}
		sid.addField("sortable", sortable.toString());
	}

	private void addSuggestionWeight() {
		int value = 0;
		if(taxon.getTaxonRank() != null) {
			value = 875 - taxon.getTaxonRank().termID();
		}

		sid.addField("suggest.weight_i", value);
	}

	private void indexImages() {
		int numImages = 3;
		List<Image> images = new ArrayList<>();

		if(taxon.looksAccepted()) {
			images.addAll(taxon.getImages());
			for(Taxon synonym : taxon.getSynonymNameUsages()) {
				images.addAll(synonym.getImages());
			}

			if(imageService == null) {
				logger.warn("ImageService is null, not adding images from subordinate taxa");
			} else {
				if(images.size() < numImages) {
					List<Image> img = imageService.getTopImages(taxon, numImages - images.size());
					images.addAll(img);
				}
			}
		}

		int index = 0;
		for(Image img : images) {
			if(index++ < numImages) {
				if(cdn == null) {
					logger.warn("CNDImageAdaptor is null, not adding image urls to index");
				} else {
					sid.addField("taxon.image_" + index + "_thumbnail_s", cdn.getThumbnailUrl(img));
					sid.addField("taxon.image_" + index + "_fullsize_s", cdn.getFullsizeUrl(img));
					sid.addField("taxon.image_" + index + "_caption_s", img.getTitle());
				}
			}
		}

		for(Image img : taxon.getImages()) {
			addSource(img);
		}

		sid.addField("taxon.images_not_empty_b", !images.isEmpty());
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
		if(taxon.looksAccepted()) {
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
			String htmlStripped = HtmlSanitizer.strip(d.getDescription());
			if(d.getType() != null) {
				if(d.getType().hasSearchCategory()) {
					sid.addField(String.format("taxon.description_%s_t", d.getType().getSearchCategory()), htmlStripped);
				} else if(DescriptionType.getAll(DescriptionType.use).contains(d.getType())) {
					sid.addField("taxon.description_use_t", htmlStripped);
				} else if(d.getTypes().contains(DescriptionType.snippet)) {
					sid.addField("taxon.description_snippet_t", htmlStripped);
				}
			}
			sid.addField("taxon.description_t", htmlStripped);
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
			sid.addField("taxon.distribution_ss_lower", name);
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
		String solrField = propertyToSolrField(property, "s_lower");
		try {
			if(rank.equals(taxon.getTaxonRank()) && BeanUtils.getProperty(taxon, property) == null) {
				addField(sid, solrField, taxon.getScientificName());
			} else {
				addField(sid, solrField, BeanUtils.getProperty(taxon, property));
			}
		} catch (IllegalAccessException | InvocationTargetException | NoSuchMethodException e) {
			logger.error("Error getting property {} from taxon. Does get{}() exist?", property, property);
		}
	}

	private void indexVernacularNames() {
		sid.addField("taxon.vernacular_names_not_empty_b", !taxon.getVernacularNames().isEmpty());
		for(VernacularName v : taxon.getVernacularNames()) {
			sid.addField("taxon.vernacular_names_t", v.getVernacularName());
			addSource(v);
		}
	}

	private void addSource(BaseData d) {
		if(d.getAuthority() != null) {
			sources.add(d.getAuthority().getIdentifier());
		}
	}
}
