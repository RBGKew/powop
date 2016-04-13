package org.emonocot.model.convert;

import static org.junit.Assert.assertEquals;

import java.util.Arrays;
import java.util.List;

import org.emonocot.model.constants.DescriptionType;
import org.junit.Test;

public class DescriptionTypeListConverterTest {

	private final DescriptionTypeListConverter converter = new DescriptionTypeListConverter();
	private final TestCase[] testCases = {
			new TestCase("general", Arrays.asList(DescriptionType.general)),
			new TestCase("general|size", Arrays.asList(DescriptionType.general, DescriptionType.size)),
			new TestCase("  general  |  size  ", Arrays.asList(DescriptionType.general, DescriptionType.size)),
	};
	@Test
	public void testDescriptionTypeListConverter() {
		for(TestCase test : testCases) {
			assertEquals(converter.convert(test.input), test.expected);
		}
	}

	private class TestCase {
		public String input;
		public List<DescriptionType> expected;

		public TestCase(String input, List<DescriptionType> result) {
			this.input = input;
			this.expected = result;
		}
	}
}
