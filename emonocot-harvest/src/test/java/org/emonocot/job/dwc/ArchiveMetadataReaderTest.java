/*
 * This is eMonocot, a global online biodiversity information resource.
 *
 * Copyright © 2011–2015 The Board of Trustees of the Royal Botanic Gardens, Kew and The University of Oxford
 *
 * eMonocot is free software: you can redistribute it and/or modify it under the terms of the
 * GNU Affero General Public License as published by the Free Software Foundation, either version 3
 * of the License, or (at your option) any later version.
 *
 * eMonocot is distributed in the hope that it will be useful, but WITHOUT ANY WARRANTY; without even
 * the implied warranty of MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 * GNU Affero General Public License for more details.
 *
 * The complete text of the GNU Affero General Public License is in the source repository as the file
 * ‘COPYING’.  It is also available from <http://www.gnu.org/licenses/>.
 */
package org.emonocot.job.dwc;

import static org.hamcrest.collection.IsArrayContaining.hasItemInArray;
import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertNull;
import static org.junit.Assert.assertThat;

import java.util.HashSet;

import javax.validation.ConstraintViolation;
import javax.validation.Validator;

import org.easymock.EasyMock;
import org.emonocot.api.OrganisationService;
import org.emonocot.job.dwc.read.ArchiveMetadataReader;
import org.emonocot.model.registry.Organisation;
import org.junit.Test;
import org.springframework.batch.core.JobExecution;
import org.springframework.batch.core.StepExecution;
import org.springframework.batch.item.ExecutionContext;
import org.springframework.core.io.ClassPathResource;
import org.springframework.core.io.Resource;

/**
 *
 * @author ben
 */
public class ArchiveMetadataReaderTest {

	/**
	 *
	 */
	private Resource content = new ClassPathResource(
			"/org/emonocot/job/dwc/test");

	/**
	 *
	 */
	private ArchiveMetadataReader archiveMetadataReader
	= new ArchiveMetadataReader();

	/**
	 *
	 */
	private OrganisationService sourceService;

	/**
	 *
	 */
	private Validator validator;

	/**
	 * @throws Exception if there is a problem accessing the file
	 */
	@Test
	public final void testReadMetadata() throws Exception {
		Organisation source = new Organisation();
		source.setIdentifier("CATE-Araceae");
		sourceService = EasyMock.createMock(OrganisationService.class);
		validator = EasyMock.createMock(Validator.class);
		EasyMock.expect(sourceService.find(EasyMock.eq("test"))).andReturn(
				source);
		EasyMock.expect(validator.validate(EasyMock.eq(source))).andReturn(
				new HashSet<ConstraintViolation<Organisation>>());
		sourceService.saveOrUpdate(EasyMock.eq(source));
		EasyMock.replay(sourceService, validator);
		ExecutionContext executionContext = new ExecutionContext();

		JobExecution jobExecution = new JobExecution(0L);
		jobExecution.setExecutionContext(executionContext);
		archiveMetadataReader.setSourceService(sourceService);
		archiveMetadataReader.setValidator(validator);

		archiveMetadataReader.beforeStep(
				new StepExecution("test", jobExecution));
		archiveMetadataReader.readMetadata(content.getFile().getAbsolutePath(),
				"test", "false");
		EasyMock.verify(sourceService, validator);

		assertNotNull("core file must be present",
				executionContext.getString("dwca.core.file"));
		assertEquals("fieldsTerminatedBy must be present", "\t",
				executionContext.getString("dwca.core.fieldsTerminatedBy"));
		assertEquals("encoding must be present", "UTF-8",
				executionContext.getString("dwca.core.encoding"));
		assertEquals("ignoreHeaderLines must be present", 0,
				executionContext.getInt("dwca.core.ignoreHeaderLines"));
		assertNotNull("field names must be present",
				executionContext.get("dwca.core.fieldNames"));
		String[] actualCoreFieldNames = (String[])
				executionContext.get("dwca.core.fieldNames");
		String[] expectedCoreFieldNames = new String[]{
				"http://rs.tdwg.org/dwc/terms/taxonID",
				"http://rs.tdwg.org/dwc/terms/scientificName",
				"http://rs.tdwg.org/dwc/terms/scientificNameID",
				"http://rs.tdwg.org/dwc/terms/scientificNameAuthorship",
				"http://rs.tdwg.org/dwc/terms/taxonRank",
				"http://rs.tdwg.org/dwc/terms/taxonomicStatus",
				"http://rs.tdwg.org/dwc/terms/parentNameUsageID",
				"http://rs.tdwg.org/dwc/terms/acceptedNameUsageID",
				"http://rs.tdwg.org/dwc/terms/genus",
				"http://rs.tdwg.org/dwc/terms/subgenus",
				"http://rs.tdwg.org/dwc/terms/specificEpithet",
				"http://rs.tdwg.org/dwc/terms/infraspecificEpithet",
				"http://purl.org/dc/terms/identifier",
				"http://purl.org/dc/terms/modified",
				"http://purl.org/dc/terms/source"
		};
		for (String expectedCoreFieldName : expectedCoreFieldNames) {
			assertThat(actualCoreFieldNames,
					hasItemInArray(expectedCoreFieldName));
		}
		assertNotNull("description file must be present",
				executionContext.getString("dwca.description.file"));
		assertEquals("fieldsTerminatedBy must be present", "\t",
				executionContext.getString(
						"dwca.description.fieldsTerminatedBy"));
		assertEquals("encoding must be present", "UTF-8",
				executionContext.getString("dwca.description.encoding"));
		assertEquals("ignoreHeaderLines must be present", 0,
				executionContext.getInt("dwca.description.ignoreHeaderLines"));
		assertNotNull("field names must be present",
				executionContext.get("dwca.description.fieldNames"));

		String[] expectedDescriptionFieldNames = new String[] {
				"http://rs.tdwg.org/dwc/terms/taxonID",
				"http://purl.org/dc/terms/created",
				"http://purl.org/dc/terms/modified",
				"http://purl.org/dc/terms/description",
				"http://purl.org/dc/terms/type",
				"http://purl.org/dc/terms/references"
		};

		String[] actualDescriptionFieldNames = (String[])
				executionContext.get("dwca.description.fieldNames");

		for (String expectedDescriptionFieldName
				: expectedDescriptionFieldNames) {
			assertThat(actualDescriptionFieldNames,
					hasItemInArray(expectedDescriptionFieldName));
		}

		assertNotNull("image file must be present",
				executionContext.getString("dwca.image.file"));
		assertEquals("fieldsTerminatedBy must be present", "\t",
				executionContext.getString(
						"dwca.image.fieldsTerminatedBy"));
		assertEquals("encoding must be present", "UTF-8",
				executionContext.getString("dwca.image.encoding"));
		assertEquals("ignoreHeaderLines must be present", 0,
				executionContext.getInt("dwca.image.ignoreHeaderLines"));
		assertNotNull("field names must be present",
				executionContext.get("dwca.image.fieldNames"));

		String[] expectedImageFieldNames = new String[] {
				"http://rs.tdwg.org/dwc/terms/taxonID",
				"http://purl.org/dc/terms/created",
				"http://purl.org/dc/terms/modified",
				"http://purl.org/dc/terms/identifier",
				"http://purl.org/dc/terms/title"
		};

		String[] actualImageFieldNames = (String[])
				executionContext.get("dwca.image.fieldNames");

		for (String expectedImageFieldName
				: expectedImageFieldNames) {
			assertThat(actualImageFieldNames,
					hasItemInArray(expectedImageFieldName));
		}

		assertNotNull("distribution file must be present",
				executionContext.getString("dwca.distribution.file"));
		assertEquals("fieldsTerminatedBy must be present", "\t",
				executionContext.getString(
						"dwca.distribution.fieldsTerminatedBy"));
		assertEquals("encoding must be present", "UTF-8",
				executionContext.getString("dwca.distribution.encoding"));
		assertEquals("ignoreHeaderLines must be present", 0,
				executionContext.getInt("dwca.distribution.ignoreHeaderLines"));
		assertNotNull("field names must be present",
				executionContext.get("dwca.distribution.fieldNames"));

		String[] expectedDistributionFieldNames = new String[] {
				"http://rs.tdwg.org/dwc/terms/taxonID",
				"http://purl.org/dc/terms/created",
				"http://purl.org/dc/terms/modified",
				"http://rs.tdwg.org/dwc/terms/occurrenceStatus",
				"http://rs.tdwg.org/dwc/terms/locationID"
		};

		String[] actualDistributionFieldNames = (String[])
				executionContext.get("dwca.distribution.fieldNames");

		for (String expectedDistributionFieldName
				: expectedDistributionFieldNames) {
			assertThat(actualDistributionFieldNames,
					hasItemInArray(expectedDistributionFieldName));
		}

		assertNotNull("reference file must be present",
				executionContext.getString("dwca.reference.file"));
		assertEquals("fieldsTerminatedBy must be present", "\t",
				executionContext.getString(
						"dwca.reference.fieldsTerminatedBy"));
		assertEquals("encoding must be present", "UTF-8",
				executionContext.getString("dwca.reference.encoding"));
		assertEquals("ignoreHeaderLines must be present", 0,
				executionContext.getInt("dwca.reference.ignoreHeaderLines"));
		assertNotNull("field names must be present",
				executionContext.get("dwca.reference.fieldNames"));

		String[] expectedReferenceFieldNames = new String[] {
				"http://rs.tdwg.org/dwc/terms/taxonID",
				"http://purl.org/dc/terms/created",
				"http://purl.org/dc/terms/modified",
				"http://purl.org/dc/terms/identifier",
				"http://purl.org/dc/terms/title",
				"http://purl.org/dc/terms/type"
		};

		String[] actualReferenceFieldNames = (String[])
				executionContext.get("dwca.reference.fieldNames");

		for (String expectedReferenceFieldName
				: expectedReferenceFieldNames) {
			assertThat(actualReferenceFieldNames,
					hasItemInArray(expectedReferenceFieldName));
		}
		assertEquals(source.getDescription(), "An open-access taxonomic web-revision of the Araceae");
		assertEquals(source.getCreator(), "Anna Haigh");
		assertEquals(source.getCreatorEmail(), "a.haigh@kew.org");
		assertEquals(source.getIdentifier(), "CATE-Araceae");
		assertEquals(source.getRights(), "The data in this site, unless otherwise specified (under 'rights'), is licensed using the Creative Commons Attribution Non-Commercial Share Alike License (cc by-nc-sa).");
		assertEquals(source.getLogoUrl(), "https://www.cate-araceae.org/css/images/cate-logo.png");
		assertEquals(source.getPublisherEmail(), "admin@cate-araceae.org");
		assertEquals(source.getPublisherName(), "Ben Clark");
		assertEquals(source.getBibliographicCitation(), "Haigh, A., Clark, B., Reynolds, L., Mayo, S.J., Croat, T.B., Lay, L., Boyce, P.C., Mora, M., Bogner, J., Sellaro, M., Wong, S.Y., Kostelac, C., Grayum, M.H., Keating, R.C., Ruckert, G., Naylor, M.F. and Hay, A., CATE Araceae, 14 Dec 2011 . 17 Dec 2011.");
		assertEquals(source.getTitle(), "CATE Araceae");
		assertEquals(source.getUri(), "http://www.cate-araceae.org");
		assertEquals(source.getCreated().toString(), "2011-04-27T00:00:00.000+01:00");
		assertNull(source.getModified());
	}

}
