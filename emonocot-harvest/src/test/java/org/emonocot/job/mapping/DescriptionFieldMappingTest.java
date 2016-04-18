package org.emonocot.job.mapping;

import static org.junit.Assert.assertEquals;
import static org.easymock.EasyMock.*;

import java.lang.reflect.InvocationTargetException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Locale;
import java.util.SortedSet;
import java.util.TreeSet;

import org.apache.commons.beanutils.PropertyUtils;
import org.emonocot.harvest.common.HtmlSanitizer;
import org.emonocot.job.dwc.description.FieldSetMapper;
import org.emonocot.model.Description;
import org.emonocot.model.Reference;
import org.emonocot.model.constants.DescriptionType;
import org.junit.Test;
import org.springframework.core.convert.ConversionService;
import org.springframework.core.convert.TypeDescriptor;
import org.springframework.validation.BindException;

public class DescriptionFieldMappingTest {

	private static final TestCase[] simpleTestCases = {
			new TestCase("http://purl.org/dc/terms/audience", "Audience").sets("Audience").on("audience"),
			new TestCase("http://purl.org/dc/terms/creator", "Creator").sets("Creator").on("creator"),
			new TestCase("http://purl.org/dc/terms/contributor", "Contributor").sets("Contributor").on("contributor"),
			new TestCase("http://purl.org/dc/terms/description", "Description").sets("Description").on("description"),
			new TestCase("http://purl.org/dc/terms/identifier", "Identifier").sets("Identifier").on("identifier"),
			new TestCase("http://purl.org/dc/terms/language", "en").sets(Locale.ENGLISH).on("language"),
			new TestCase("http://purl.org/dc/terms/source", "Source").sets("Source").on("source"),
	};

	private final FieldSetMapper mapper = new FieldSetMapper();
	private final Description description = new Description();

	private final ConversionService conversionService = createMock(ConversionService.class);
	private final HtmlSanitizer htmlSanitizer = createMock(HtmlSanitizer.class);

	public void mockDependencies() {
		expect(htmlSanitizer.sanitize("Description")).andReturn("Description");
		expect(conversionService.convert("en", Locale.class)).andReturn(Locale.ENGLISH);

		mapper.setConversionService(conversionService);
		mapper.setHtmlSanitizer(htmlSanitizer);
		replay(conversionService, htmlSanitizer);
	}

	@Test
	public void testSimpleFieldMappings() throws BindException, IllegalAccessException, InvocationTargetException, NoSuchMethodException {
		mockDependencies();
		for(TestCase test : simpleTestCases) {
			mapper.mapField(description, test.fieldName, test.value);
			assertEquals(test.expected, PropertyUtils.getSimpleProperty(description, test.propertyName));
		}
	}

	@Test
	public void testMultiTypeMapping() throws BindException {
		String inputString = "reproductiveMorphology:fruits|sex:male";
		SortedSet<DescriptionType> expected = new TreeSet<>(Arrays.asList(DescriptionType.reproductiveMorphologyFruits, DescriptionType.sexMale));
		expect(conversionService.convert(inputString,
				TypeDescriptor.valueOf(String.class),
				TypeDescriptor.collection(SortedSet.class, TypeDescriptor.valueOf(DescriptionType.class))))
		.andReturn(expected);
		mapper.setConversionService(conversionService);
		replay(conversionService);

		mapper.mapField(description, "http://purl.org/dc/terms/type", inputString);
		assertEquals(description.getTypes(), expected);
	}

	@Test
	public void testMultiReferenceFieldMapping() throws BindException {
		mapper.mapField(description, "http://purl.org/dc/terms/references", "Reference 1,Reference 2");
		assertEquals(description.getReferences().size(), 2);

		List<String> ids = new ArrayList<>();
		for(Reference reference : description.getReferences()) {
			ids.add(reference.getIdentifier());
		}
		assertEquals(ids, Arrays.asList("Reference 1", "Reference 2"));
	}
}
