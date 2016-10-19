package org.emonocot.model.solr;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertTrue;
import static org.junit.Assert.assertThat;
import static org.hamcrest.Matchers.contains;
import static org.hamcrest.collection.IsIterableContainingInAnyOrder.containsInAnyOrder;

import java.util.Arrays;
import java.util.HashSet;
import java.util.Set;
import java.util.TreeSet;

import org.apache.commons.beanutils.BeanUtils;
import org.apache.solr.common.SolrInputDocument;
import org.emonocot.api.job.WCSPTerm;
import org.emonocot.model.Description;
import org.emonocot.model.Distribution;
import org.emonocot.model.Image;
import org.emonocot.model.MeasurementOrFact;
import org.emonocot.model.Taxon;
import org.emonocot.model.constants.DescriptionType;
import org.emonocot.model.constants.Location;
import org.emonocot.model.constants.MeasurementUnit;
import org.gbif.ecat.voc.Rank;
import org.gbif.ecat.voc.TaxonomicStatus;
import org.junit.Test;

import com.google.common.collect.ImmutableList;
import com.google.common.collect.ImmutableSet;

public class TaxonSolrInputDocumentTest {

	private class TestCase {
		public Rank rank;
		public String solrFieldName;

		public TestCase withRank(Rank rank) {
			this.rank = rank;
			return this;
		}

		public TestCase andSolrFieldName(String fieldName) {
			this.solrFieldName = fieldName;
			return this;
		}
	}

	private TestCase[] higerOrderTestCases = {
			new TestCase().withRank(Rank.FAMILY).andSolrFieldName("taxon.family_t"),
			new TestCase().withRank(Rank.Subfamily).andSolrFieldName("taxon.subfamily_t"),
			new TestCase().withRank(Rank.GENUS).andSolrFieldName("taxon.genus_t"),
			new TestCase().withRank(Rank.Tribe).andSolrFieldName("taxon.tribe_t"),
			new TestCase().withRank(Rank.Subtribe).andSolrFieldName("taxon.subtribe_t"),
	};

	@Test
	public void higherOrderTaxa() throws Exception {
		for(TestCase test : higerOrderTestCases) {
			testIndexRank(test);
		}
	}

	@Test
	public void higherOrderSynonyms() throws Exception {
		for(TestCase test : higerOrderTestCases) {
			testIndexSynonymRank(test);
		}
	}

	@Test
	public void simpleStringMappings() throws Exception {
		String[] fields = {
				"taxon.infraspecific_epithet_s",
				"taxon.name_published_in_string_s",
				"taxon.order_s",
				"taxon.scientific_name_authorship_t",
				"taxon.scientific_name_t",
				"taxon.specific_epithet_s",
				"taxon.subgenus_s",
				"taxon.verbatim_taxon_rank_s"
		};

		for(String field : fields) {
			Taxon taxon = new Taxon();
			String taxonField = TaxonSolrInputDocument.solrFieldToProperty(field);
			String expected = "Test Property " + taxonField;
			BeanUtils.setProperty(taxon, taxonField, expected);

			SolrInputDocument doc = new TaxonSolrInputDocument(taxon).build();

			assertTrue("Expected " + field, doc.containsKey(field));
			assertEquals(expected, doc.getFieldValue(field));
		}
	}

	@Test
	public void taxonRank() {
		Taxon taxon = new Taxon();
		taxon.setTaxonRank(Rank.KINGDOM);
		SolrInputDocument doc = new TaxonSolrInputDocument(taxon).build();

		String field = "taxon.taxon_rank_s";
		assertTrue("Expected " + field, doc.containsKey(field));
		assertEquals(Rank.KINGDOM.toString(), doc.getFieldValue(field));
	}

	@Test
	public void taxonomicStatus() {
		Taxon taxon = new Taxon();
		taxon.setTaxonomicStatus(TaxonomicStatus.Doubtful);
		SolrInputDocument doc = new TaxonSolrInputDocument(taxon).build();

		String field = "taxon.taxonomic_status_s";
		assertTrue("Expected " + field, doc.containsKey(field));
		assertEquals(TaxonomicStatus.Doubtful.toString(), doc.getFieldValue(field));
	}

	@Test
	public void descriptions() throws Exception {
		Description[] descriptions = {
				buildDescription("Description 1. Blah blah blah", DescriptionType.morphologyGeneralBuds),
				buildDescription("Description 2. Blah blah blah", DescriptionType.morphologyReproductive, DescriptionType.sexMale),
				buildDescription("Use 1. Blah blah blah", DescriptionType.use),
				buildDescription("Use 2. Blah blah blah", DescriptionType.useAnimalFoodHerbage)
		};

		Taxon taxon = new Taxon();
		taxon.setDescriptions(ImmutableSet.<Description>copyOf(descriptions));

		SolrInputDocument doc = new TaxonSolrInputDocument(taxon).build();
		String expectedField1 = "taxon.description_appearance_t";
		String expectedField2 = "taxon.description_flower_t";
		String expectedUse = "taxon.description_use_t";
		String combinedDesceription = "taxon.description_t";

		assertTrue("Expected " + expectedField1, doc.containsKey(expectedField1));
		assertTrue("Expected " + expectedField2, doc.containsKey(expectedField2));
		assertTrue("Expected " + expectedUse, doc.containsKey(expectedUse));
		assertTrue("Expected " + combinedDesceription, doc.containsKey(combinedDesceription));
		assertEquals("Description 1. Blah blah blah", doc.getFieldValue(expectedField1));
		assertEquals("Description 2. Blah blah blah", doc.getFieldValue(expectedField2));
		assertThat(doc.getFieldValues(expectedUse), containsInAnyOrder((Object)"Use 1. Blah blah blah", (Object)"Use 2. Blah blah blah"));
	}

	@Test
	public void measurements() {
		MeasurementOrFact measurement = new MeasurementOrFact();
		measurement.setMeasurementUnit(MeasurementUnit.METERS);
		measurement.setMeasurementType(WCSPTerm.Lifeform);
		measurement.setMeasurementValue("42");
		Taxon taxon = new Taxon();
		taxon.setMeasurementsOrFacts(ImmutableSet.<MeasurementOrFact>of(measurement));

		SolrInputDocument doc = new TaxonSolrInputDocument(taxon).build();
		String expected = "taxon.measurement_lifeform_ds";

		assertTrue("Expected " + expected, doc.containsKey(expected));
		assertEquals(42.0, doc.getFieldValue(expected));
	}

	@Test
	public void facts() {
		MeasurementOrFact measurement = new MeasurementOrFact();
		measurement.setMeasurementType(WCSPTerm.Habitat);
		measurement.setMeasurementValue("Xeric Scrubland");
		Taxon taxon = new Taxon();
		taxon.setMeasurementsOrFacts(ImmutableSet.<MeasurementOrFact>of(measurement));

		SolrInputDocument doc = new TaxonSolrInputDocument(taxon).build();
		String expected = "taxon.fact_habitat_ss";

		assertTrue("Expected " + expected, doc.containsKey(expected));
		assertEquals("Xeric Scrubland", doc.getFieldValue(expected));
	}

	private String[] expectedDistributionKeys = {"taxon.distribution_t"};
	@Test
	public void level0Distribution() {
		Distribution level0 = new Distribution();
		level0.setLocation(Location.ANTARCTICA);

		Taxon taxon = new Taxon();
		taxon.setDistribution(ImmutableSet.<Distribution>of(level0));

		SolrInputDocument doc = new TaxonSolrInputDocument(taxon).build();

		for(String expectedKey : expectedDistributionKeys) {
			assertTrue("Expected " + expectedKey, doc.containsKey(expectedKey));
			System.out.println(Arrays.toString(doc.getFieldValues(expectedKey).toArray()));
		}

		// All subregions (level 1, 2, 3) should be indexed
		Object[] expectedLocationNames = {
				"Amsterdam-St.Paul Is.",
				"Antarctic",
				"Antarctic Continent",
				"Antarctica",
				"Bouvet I.",
				"Crozet Is.",
				"Falkland Is.",
				"Heard-McDonald Is.",
				"Kerguelen",
				"Macquarie Is.",
				"Marion-Prince Edward Is.",
				"South Georgia",
				"South Sandwich Is.",
				"Subantarctic Islands",
				"Tristan da Cunha"
		};

		assertThat(doc.getFieldValues(expectedDistributionKeys[0]), contains(expectedLocationNames));
	}

	@Test
	public void level1Distribution() {
		Distribution level1 = new Distribution();
		level1.setLocation(Location.SUBANTARCTIC_ISLANDS);

		Taxon taxon = new Taxon();
		taxon.setDistribution(ImmutableSet.<Distribution>of(level1));

		SolrInputDocument doc = new TaxonSolrInputDocument(taxon).build();

		for(String expectedKey : expectedDistributionKeys) {
			assertTrue("Expected " + expectedKey, doc.containsKey(expectedKey));
			System.out.println(Arrays.toString(doc.getFieldValues(expectedKey).toArray()));
		}

		// All subregions (level 2, 3) should be indexed, plus parent regions to level 0
		Object[] expectedLocationNames = {
				"Amsterdam-St.Paul Is.",
				"Antarctic",
				"Bouvet I.",
				"Crozet Is.",
				"Falkland Is.",
				"Heard-McDonald Is.",
				"Kerguelen",
				"Macquarie Is.",
				"Marion-Prince Edward Is.",
				"South Georgia",
				"South Sandwich Is.",
				"Subantarctic Islands",
				"Tristan da Cunha"
		};

		assertThat(doc.getFieldValues(expectedDistributionKeys[0]), contains(expectedLocationNames));
	}

	@Test
	public void level2Distribution() {
		Distribution level2 = new Distribution();
		level2.setLocation(Location.SSA);

		Taxon taxon = new Taxon();
		taxon.setDistribution(ImmutableSet.<Distribution>of(level2));

		SolrInputDocument doc = new TaxonSolrInputDocument(taxon).build();

		for(String expectedKey : expectedDistributionKeys) {
			assertTrue("Expected " + expectedKey, doc.containsKey(expectedKey));
		}

		// All subregions (level 3) should be indexed, plus parent regions to level 0
		Object[] expectedLocationNames = {
				"Antarctic", "South Sandwich Is.","Subantarctic Islands"
		};

		assertThat(doc.getFieldValues(expectedDistributionKeys[0]), contains(expectedLocationNames));
	}

	@Test
	public void level3Distribution() {
		Distribution level3 = new Distribution();
		level3.setLocation(Location.SSA_OO);

		Taxon taxon = new Taxon();
		taxon.setDistribution(ImmutableSet.<Distribution>of(level3));

		SolrInputDocument doc = new TaxonSolrInputDocument(taxon).build();

		for(String expectedKey : expectedDistributionKeys) {
			assertTrue("Expected " + expectedKey, doc.containsKey(expectedKey));
			System.out.println(Arrays.toString(doc.getFieldValues(expectedKey).toArray()));
		}

		// All parent regions to level 0 should be indexed
		Object[] expectedLocationNames = {
				"Antarctic", "South Sandwich Is.","Subantarctic Islands"
		};

		assertThat(doc.getFieldValues(expectedDistributionKeys[0]), contains(expectedLocationNames));
	}

	@Test
	public void testIndexingSynonymDataUnderAcceptedName() {
		Taxon taxon = new Taxon();
		Taxon synonym = new Taxon();
		Description description = buildDescription("Blarg", DescriptionType.general);
		Image image = new Image();

		synonym.setDescriptions(ImmutableSet.<Description>of(description));
		synonym.setImages(ImmutableList.<Image>of(image));
		synonym.setTaxonomicStatus(TaxonomicStatus.Synonym);
		taxon.setSynonymNameUsages(ImmutableSet.<Taxon>of(synonym));

		SolrInputDocument taxonDoc = new TaxonSolrInputDocument(taxon).build();
		SolrInputDocument synonymDoc = new TaxonSolrInputDocument(synonym).build();
		assertTrue("Expected taxon.description_t", taxonDoc.containsKey("taxon.description_t"));
		assertTrue("Expected taxon.descriptions_not_empty_b to be true", (Boolean)taxonDoc.getFieldValue("taxon.descriptions_not_empty_b"));
		assertTrue("Expected taxon.images_not_empty_b to be true", (Boolean)taxonDoc.getFieldValue("taxon.images_not_empty_b"));

		assertFalse("Didn't expect taxon.description_t", synonymDoc.containsKey("taxon.description_t"));
		assertFalse("Expected taxon.descriptions_not_empty_b to be false", (Boolean)synonymDoc.getFieldValue("taxon.descriptions_not_empty_b"));
		assertFalse("Expected taxon.images_not_empty_b to be false", (Boolean)synonymDoc.getFieldValue("taxon.images_not_empty_b"));
	}

	private Description buildDescription(String description, DescriptionType... types) {
		Description desc = new Description();
		desc.setDescription(description);
		desc.setTypes(new TreeSet<>(Arrays.asList(types)));
		return desc;
	}


	private void testIndexRank(TestCase test) {
		Taxon family = new Taxon();
		family.setTaxonRank(test.rank);
		family.setScientificName(test.rank + ": Orchidacae");

		SolrInputDocument doc = new TaxonSolrInputDocument(family).build();

		assertTrue("Expected " + test.solrFieldName, doc.containsKey(test.solrFieldName));
		assertEquals(test.rank + ": Orchidacae", doc.getFieldValue(test.solrFieldName));
	}

	private void testIndexSynonymRank(TestCase test) throws Exception {
		Taxon accepted = new Taxon();
		Taxon synonym = new Taxon();
		Set<Taxon> newSynonyms = new HashSet<Taxon>();
		String acceptedName = test.rank + ": Orchidacae";
		String synonymName = test.rank + ": Poaceae";

		BeanUtils.setProperty(accepted, test.rank.toString().toLowerCase(), acceptedName);
		BeanUtils.setProperty(synonym, test.rank.toString().toLowerCase(), synonymName);
		newSynonyms.add(synonym);
		synonym.setAcceptedNameUsage(accepted);
		accepted.setSynonymNameUsages(newSynonyms);
		SolrInputDocument doc = new TaxonSolrInputDocument(accepted).build();
		assertTrue("Expected " + test.solrFieldName, doc.containsKey(test.solrFieldName));
		assertEquals(Arrays.asList(acceptedName, synonymName), doc.getFieldValues(test.solrFieldName));
	}
}
