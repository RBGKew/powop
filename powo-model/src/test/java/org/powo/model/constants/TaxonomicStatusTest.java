package org.powo.model.constants;

import static org.junit.Assert.assertEquals;
import static org.powo.model.constants.TaxonomicStatus.Accepted;
import static org.powo.model.constants.TaxonomicStatus.Artificial_Hybrid;
import static org.powo.model.constants.TaxonomicStatus.DeterminationSynonym;
import static org.powo.model.constants.TaxonomicStatus.Doubtful;
import static org.powo.model.constants.TaxonomicStatus.Synonym;
import static org.powo.model.constants.TaxonomicStatus.Unplaced;
import static org.powo.model.constants.TaxonomicStatus.Heterotypic_Synonym;
import static org.powo.model.constants.TaxonomicStatus.Homotypic_Synonym;
import static org.powo.model.constants.TaxonomicStatus.IntermediateRankSynonym;
import static org.powo.model.constants.TaxonomicStatus.Misapplied;
import static org.powo.model.constants.TaxonomicStatus.Proparte_Synonym;
import static org.powo.model.constants.TaxonomicStatus.fromString;

import org.junit.Test;

public class TaxonomicStatusTest {
	@Test
	public void testAcceptedFromString() {
		assertEquals(fromString("Accepted"), Accepted);
		assertEquals(fromString("accepted"), Accepted);
		assertEquals(fromString("accepted "), Accepted);
		assertEquals(fromString(" accepted"), Accepted);
	}

	@Test
	public void testSynonymFromString() {
		assertEquals(fromString("Synonym"), Synonym);
		assertEquals(fromString("synonym"), Synonym);
		assertEquals(fromString("synonym "), Synonym);
		assertEquals(fromString(" synonym"), Synonym);
	}

	@Test
	public void testHeterotypicSynoymFromString() {
		assertEquals(fromString("Heterotypic_Synonym"), Heterotypic_Synonym);
		assertEquals(fromString("Heterotypicsynonym"), Heterotypic_Synonym);
		assertEquals(fromString("Heterotypic synonym "), Heterotypic_Synonym);
		assertEquals(fromString(" heterotypic synonym"), Heterotypic_Synonym);
	}

	@Test
	public void testHomotypicSynoymFromString() {
		assertEquals(fromString("Homotypic_Synonym"), Homotypic_Synonym);
		assertEquals(fromString("Homotypicsynonym"), Homotypic_Synonym);
		assertEquals(fromString("Homotypic synonym "), Homotypic_Synonym);
		assertEquals(fromString(" homotypic synonym"), Homotypic_Synonym);
	}

	@Test
	public void testProparteSynoymFromString() {
		assertEquals(fromString("Proparte_Synonym"), Proparte_Synonym);
		assertEquals(fromString("ProparteSynonym"), Proparte_Synonym);
		assertEquals(fromString("propartesynonym"), Proparte_Synonym);
		assertEquals(fromString("Proparte Synonym"), Proparte_Synonym);
	}

	@Test
	public void testMisappliedFromString() {
		assertEquals(fromString("Misapplied"), Misapplied);
		assertEquals(fromString("misapplied"), Misapplied);
		assertEquals(fromString(" Misapplied"), Misapplied);
		assertEquals(fromString("Misapplied "), Misapplied);
	}

	@Test
	public void testIntermediateRankSynonymFromString() {
		assertEquals(fromString("IntermediateRankSynonym"), IntermediateRankSynonym);
		assertEquals(fromString("IntermediateRank_Synonym"), IntermediateRankSynonym);
		assertEquals(fromString("Intermediate_Rank_Synonym"), IntermediateRankSynonym);
		assertEquals(fromString("Intermediate Rank Synonym"), IntermediateRankSynonym);
		assertEquals(fromString("intermediate rank synonym "), IntermediateRankSynonym);
	}

	@Test
	public void testDeterminationSynonymFromString() {
		assertEquals(fromString("DeterminationSynonym"), DeterminationSynonym);
		assertEquals(fromString("Determination Synonym"), DeterminationSynonym);
		assertEquals(fromString("Determination_Synonym"), DeterminationSynonym);
		assertEquals(fromString("Determinationsynonym"), DeterminationSynonym);
		assertEquals(fromString("determination synonym "), DeterminationSynonym);
	}

	@Test
	public void testDoubtfulFromString() {
		assertEquals(fromString("Doubtful"), Doubtful);
		assertEquals(fromString("doubtful"), Doubtful);
		assertEquals(fromString("Doubtful "), Doubtful);
	}

	@Test
	public void testArtificialHybridFromString() {
		assertEquals(fromString("Artificial_Hybrid"), Artificial_Hybrid);
		assertEquals(fromString("Artificial Hybrid"), Artificial_Hybrid);
		assertEquals(fromString("Artificial_hybrid"), Artificial_Hybrid);
		assertEquals(fromString("artificialhybrid "), Artificial_Hybrid);
	}

	@Test
	public void testUnplacedFromString() {
		assertEquals(fromString("Unplaced"), Unplaced);
		assertEquals(fromString("unplaced"), Unplaced);
		assertEquals(fromString("Unplaced "), Unplaced);
	}
}
