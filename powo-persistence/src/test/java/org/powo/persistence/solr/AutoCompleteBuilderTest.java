package org.powo.persistence.solr;

import static org.junit.Assert.assertThat;
import static org.hamcrest.core.StringContains.containsString;
import static org.hamcrest.core.IsNot.not;

import java.util.Arrays;
import org.junit.Test;
import org.powo.persistence.solr.AutoCompleteBuilder;

public class AutoCompleteBuilderTest {

	@Test
	public void testWorkingSuggesters() {
		String query = new AutoCompleteBuilder()
				.setWorkingSuggesters(Arrays.asList("a", "b"))
				.setSuggesters(Arrays.asList("a", "c"))
				.build().toString();

		assertThat(query, containsString("suggest.dictionary=a"));
		assertThat(query, not(containsString("suggest.dictionary=c")));
	}

	@Test
	public void testScientificNameSuggesters() {
		String query = new AutoCompleteBuilder()
				.setWorkingSuggesters(Arrays.asList("scientific-name"))
				.setSuggesters(Arrays.asList("family", "genus", "species"))
				.build().toString();

		assertThat(query, containsString("suggest.dictionary=scientific-name"));
		assertThat(query, containsString("suggest.cfq=FAMILY"));
		assertThat(query, containsString("suggest.cfq=GENUS"));
		assertThat(query, containsString("suggest.cfq=SPECIES"));
	}

	@Test
	public void pageSizeDefaults() {
		String query = new AutoCompleteBuilder()
				.setWorkingSuggesters(Arrays.asList("a"))
				.setSuggesters(Arrays.asList("a"))
				.build().toString();
		assertThat(query, containsString("suggest.count=5"));
	}
}
