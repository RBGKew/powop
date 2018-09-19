package org.powo.model.solr;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertTrue;
import static org.junit.Assert.assertThat;
import static org.hamcrest.Matchers.contains;
import java.util.Arrays;
import java.util.TreeSet;

import org.apache.commons.beanutils.BeanUtils;
import org.apache.solr.common.SolrInputDocument;
import org.gbif.ecat.voc.Rank;
import org.powo.model.constants.TaxonomicStatus;
import org.junit.Test;
import org.powo.api.job.WCSPTerm;
import org.powo.model.Description;
import org.powo.model.Distribution;
import org.powo.model.Image;
import org.powo.model.MeasurementOrFact;
import org.powo.model.Taxon;
import org.powo.model.constants.DescriptionType;
import org.powo.model.constants.Location;
import org.powo.model.constants.MeasurementUnit;
import org.powo.model.solr.TaxonSolrInputDocument;
import com.google.common.collect.ImmutableList;
import com.google.common.collect.ImmutableSet;

public class TaxonSolrInputDocumentTest {

	private class TestCase {
		public Rank rank;
		public String solrFieldName;
		public Description description;

		public TestCase withRank(Rank rank) {
			this.rank = rank;
			return this;
		}

		public TestCase andSolrFieldName(String fieldName) {
			this.solrFieldName = fieldName;
			return this;
		}

		public TestCase withDescription(String description, DescriptionType... types) {
			this.description = new Description();
			this.description.setDescription(description);
			this.description.setTypes(new TreeSet<>(Arrays.asList(types)));
			return this;
		}
	}

	private TestCase[] higerOrderTestCases = {
			new TestCase().withRank(Rank.FAMILY).andSolrFieldName("taxon.family_s_lower"),
			new TestCase().withRank(Rank.Subfamily).andSolrFieldName("taxon.subfamily_s_lower"),
			new TestCase().withRank(Rank.GENUS).andSolrFieldName("taxon.genus_s_lower"),
			new TestCase().withRank(Rank.Tribe).andSolrFieldName("taxon.tribe_s_lower"),
			new TestCase().withRank(Rank.Subtribe).andSolrFieldName("taxon.subtribe_s_lower"),
	};

	@Test
	public void higherOrderTaxa() throws Exception {
		for(TestCase test : higerOrderTestCases) {
			testIndexRank(test);
		}
	}

	@Test
	public void simpleStringMappings() throws Exception {
		String[] fields = {
				"taxon.infraspecific_epithet_s_lower",
				"taxon.kingdom_s_lower",
				"taxon.order_s_lower",
				"taxon.scientific_name_authorship_t",
				"taxon.scientific_name_s_lower",
				"taxon.specific_epithet_s_lower",
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

		String field = "taxon.rank_s_lower";
		assertTrue("Expected " + field, doc.containsKey(field));
		assertEquals(Rank.KINGDOM.toString(), doc.getFieldValue(field));
	}

	@Test
	public void taxonomicStatus() {
		Taxon taxon = new Taxon();
		taxon.setTaxonomicStatus(TaxonomicStatus.Doubtful);
		SolrInputDocument doc = new TaxonSolrInputDocument(taxon).build();

		String field = "taxon.taxonomic_status_s_lower";
		assertTrue("Expected " + field, doc.containsKey(field));
		assertEquals(TaxonomicStatus.Doubtful.toString(), doc.getFieldValue(field));
	}

	@Test
	public void descriptions() throws Exception {
		// solr field names need to match those in suggester definitions in solrconfig.xml
		TestCase[] descriptionTests = {
				new TestCase().withDescription("Appearance description", DescriptionType.morphologyGeneral).andSolrFieldName("taxon.description_appearance_t"),
				new TestCase().withDescription("Flower description", DescriptionType.morphologyReproductiveFlower).andSolrFieldName("taxon.description_flower_t"),
				new TestCase().withDescription("Leaf description", DescriptionType.morphologyLeaf).andSolrFieldName("taxon.description_leaf_t"),
				new TestCase().withDescription("Inflorescence description", DescriptionType.morphologyReproductiveInflorescence).andSolrFieldName("taxon.description_inflorescence_t"),
				new TestCase().withDescription("Fruit description", DescriptionType.morphologyReproductiveFruit).andSolrFieldName("taxon.description_fruit_t"),
				new TestCase().withDescription("Seed description", DescriptionType.morphologyReproductiveSeed).andSolrFieldName("taxon.description_seed_t"),
				new TestCase().withDescription("Cloning description", DescriptionType.vegetativeMultiplication).andSolrFieldName("taxon.description_vegitativePropagation_t"),
				new TestCase().withDescription("Use description", DescriptionType.useAnimalFoodHerbage).andSolrFieldName("taxon.description_use_t")
		};

		for(TestCase test : descriptionTests) {
			Taxon taxon = new Taxon();
			taxon.setDescriptions(ImmutableSet.<Description>of(test.description));
			SolrInputDocument doc = new TaxonSolrInputDocument(taxon).build();
			assertTrue("Expected " + test.solrFieldName, doc.containsKey(test.solrFieldName));
			assertEquals(test.description.getDescription(), doc.getFieldValue(test.solrFieldName));
		}
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

	private String[] expectedDistributionKeys = {"taxon.distribution_ss_lower"};
	@Test
	public void level0Distribution() {
		Distribution level0 = new Distribution();
		level0.setLocation(Location.ANTARCTICA);

		Taxon taxon = new Taxon();
		taxon.setDistribution(ImmutableSet.<Distribution>of(level0));

		SolrInputDocument doc = new TaxonSolrInputDocument(taxon).build();

		for(String expectedKey : expectedDistributionKeys) {
			assertTrue("Expected " + expectedKey, doc.containsKey(expectedKey));
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

	@Test
	public void testFamilySortable() {
		Taxon taxon = new Taxon();
		taxon.setTaxonRank(Rank.FAMILY);
		taxon.setFamily("Asteraceae");
		taxon.setScientificName("Asteraceae");
		SolrInputDocument doc = new TaxonSolrInputDocument(taxon).build();

		assertEquals("325Asteraceae", doc.getFieldValue("sortable"));
	}

	@Test
	public void testGenusSortable() {
		Taxon taxon = new Taxon();
		taxon.setTaxonRank(Rank.GENUS);
		taxon.setFamily("Asteraceae");
		taxon.setScientificName("Echinacea");
		SolrInputDocument doc = new TaxonSolrInputDocument(taxon).build();

		assertEquals("425AsteraceaeEchinacea", doc.getFieldValue("sortable"));
	}

	@Test
	public void testBinomialSortable() {
		Taxon taxon = new Taxon();
		taxon.setTaxonRank(Rank.SPECIES);
		taxon.setFamily("Asteraceae");
		taxon.setScientificName("Echinacea purpurea");
		SolrInputDocument doc = new TaxonSolrInputDocument(taxon).build();

		assertEquals("600AsteraceaeEchinaceapurpurea", doc.getFieldValue("sortable"));
	}

	@Test
	public void testTrinomialSortable() {
		Taxon taxon = new Taxon();
		taxon.setTaxonRank(Rank.Form);
		taxon.setFamily("Asteraceae");
		taxon.setScientificName("Hieracium sabaudum f. bladonii");
		SolrInputDocument doc = new TaxonSolrInputDocument(taxon).build();

		assertEquals("750AsteraceaeHieraciumsabaudumf.bladonii", doc.getFieldValue("sortable"));
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
}
