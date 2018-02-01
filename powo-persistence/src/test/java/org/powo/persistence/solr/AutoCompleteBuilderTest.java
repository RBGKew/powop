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
				.setSuggesters(Arrays.asList("a"))
				.build().toString();

		assertThat(query, containsString("suggest.dictionary=a"));
	}

	@Test
	public void testScientificNameSuggesters() {
		String query = new AutoCompleteBuilder()
				.setSuggesters(Arrays.asList("scientific-name"))
				.build().toString();

		assertThat(query, containsString("suggest.dictionary=scientific-name"));
	}

	@Test
	public void pageSizeDefaults() {
		String query = new AutoCompleteBuilder()
				.setSuggesters(Arrays.asList("a"))
				.build().toString();
		assertThat(query, containsString("suggest.count=5"));
	}
}
