package org.emonocot.model.convert;

import static org.junit.Assert.assertEquals;

import java.util.Arrays;
import java.util.SortedSet;
import java.util.TreeSet;

import org.emonocot.model.constants.DescriptionType;
import org.junit.Test;

public class DescriptionTypeSetConverterTest {

	private final DescriptionTypeSetConverter converter = new DescriptionTypeSetConverter();
	private final TestCase[] testCases = {
			new TestCase("general", DescriptionType.general),
			new TestCase("general|size", DescriptionType.general, DescriptionType.size),
			new TestCase("  general  |  size  ", DescriptionType.general, DescriptionType.size),
	};
	@Test
	public void testDescriptionTypeListConverter() {
		for(TestCase test : testCases) {
			assertEquals(converter.convert(test.input), test.expected);
		}
	}

	private class TestCase {
		public String input;
		public SortedSet<DescriptionType> expected;

		public TestCase(String input, DescriptionType... results) {
			this.input = input;
			this.expected = new TreeSet<>(Arrays.asList(results));
		}
	}
}
