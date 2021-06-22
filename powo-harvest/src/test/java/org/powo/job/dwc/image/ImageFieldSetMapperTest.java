package org.powo.job.dwc.image;

import static org.easymock.EasyMock.createMock;
import static org.easymock.EasyMock.expect;
import static org.easymock.EasyMock.replay;
import static org.junit.Assert.assertEquals;

import java.lang.reflect.InvocationTargetException;
import java.util.SortedSet;

import org.apache.commons.beanutils.PropertyUtils;
import org.junit.Before;
import org.junit.Test;
import org.powo.job.dwc.TestCase;
import org.powo.model.Image;
import org.powo.model.constants.DescriptionType;
import org.powo.model.constants.MediaFormat;
import org.powo.model.constants.MediaType;
import org.springframework.core.convert.ConversionService;
import org.springframework.core.convert.TypeDescriptor;
import org.springframework.validation.BindException;

import com.google.common.collect.ImmutableSet;
import com.google.common.collect.ImmutableSortedSet;

public class ImageFieldSetMapperTest {

	private static final TestCase[] simpleTestCases = {
			// Standard DC terms
			new TestCase("http://purl.org/dc/terms/audience", "Audience").sets("Audience").on("audience"),
			new TestCase("http://purl.org/dc/terms/contributor", "Contributor").sets("Contributor").on("contributor"),
			new TestCase("http://purl.org/dc/terms/creator", "Creator").sets("Creator").on("creator"),
			new TestCase("http://purl.org/dc/terms/description", "Description").sets("Description").on("description"),
			new TestCase("http://purl.org/dc/terms/format", "jpg").sets(MediaFormat.jpg).on("format"),
			new TestCase("http://purl.org/dc/terms/identifier", "Identifier").sets("Identifier").on("identifier"),
			new TestCase("http://purl.org/dc/terms/publisher", "Publisher").sets("Publisher").on("publisher"),
			new TestCase("http://purl.org/dc/terms/references", "References").sets("References").on("references"),
			new TestCase("http://purl.org/dc/terms/subject", "Subject").sets("Subject").on("subject"),
			new TestCase("http://purl.org/dc/terms/title", "Title").sets("Title").on("title"),
			new TestCase("http://purl.org/dc/terms/type", "Image").sets(MediaType.Image).on("type"),

			// Audubon terms 
			new TestCase("http://rs.tdwg.org/ac/terms/caption", "Caption").sets("Caption").on("caption"),
			new TestCase("http://rs.tdwg.org/ac/terms/providerManagedID", "PMID").sets("PMID").on("providerManagedId"),
			new TestCase("http://rs.tdwg.org/ac/terms/subjectPart", "floralDiagram").sets(ImmutableSet.<DescriptionType>of(DescriptionType.floralDiagram)).on("subjectPart"),
			new TestCase("http://rs.tdwg.org/ac/terms/accessURI", "http://blargedy.com/").sets("http://blargedy.com/").on("accessUri"),

			// Adobe XMP terms
			new TestCase("http://ns.adobe.com/xap/1.0/Rating", "1.0").sets(1.0).on("rating"),
			new TestCase("http://ns.adobe.com/xap/1.0/rights/Owner", "Owner").sets("Owner").on("owner"),
	};

	@Before
	public void mockDependencies() {
		expect(conversionService.convert("jpg", MediaFormat.class)).andReturn(MediaFormat.jpg);
		expect(conversionService.convert("Image", MediaType.class)).andReturn(MediaType.Image);
		expect(conversionService.convert("floralDiagram",
				TypeDescriptor.valueOf(String.class),
				TypeDescriptor.collection(SortedSet.class, TypeDescriptor.valueOf(DescriptionType.class))))
		.andReturn(ImmutableSortedSet.<DescriptionType>of(DescriptionType.floralDiagram));
		expect(conversionService.convert("1.0", Double.class)).andReturn(new Double(1));

		mapper.setConversionService(conversionService);
		replay(conversionService);
	}

	private final FieldSetMapper mapper = new FieldSetMapper();
	private final Image image = new Image();

	private final ConversionService conversionService = createMock(ConversionService.class);

	@Test
	public void testSimpleFieldMappings() throws BindException, IllegalAccessException, InvocationTargetException, NoSuchMethodException {
		for(TestCase test : simpleTestCases) {
			mapper.mapField(image, test.fieldName, test.value);
			assertEquals(test.expected, PropertyUtils.getSimpleProperty(image, test.propertyName));
		}
	}

	@Test
  public void testFixUri() {
    var validUri = "http://example.com/image/inga%20alba.jpg";
    assertEquals(validUri, mapper.fixAccessUri(validUri));

    var invalidUri = "http://example.com/image/inga alba.jpg";
    assertEquals(validUri, mapper.fixAccessUri(invalidUri));

    var missingProtocolUri = "example.com/image/inga alba.jpg";
    assertEquals("http://example.com/image/inga%20alba.jpg", mapper.fixAccessUri(missingProtocolUri));
  }

}
