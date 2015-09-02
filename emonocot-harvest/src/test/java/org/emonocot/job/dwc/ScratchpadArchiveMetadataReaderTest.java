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
import static org.junit.Assert.assertThat;

import javax.validation.Validator;

import org.emonocot.api.OrganisationService;
import org.emonocot.job.dwc.read.ArchiveMetadataReader;
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
public class ScratchpadArchiveMetadataReaderTest {

	/**
	 *
	 */
	private Resource content = new ClassPathResource(
			"/org/emonocot/zingiberaceae");

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
		ExecutionContext executionContext = new ExecutionContext();

		JobExecution jobExecution = new JobExecution(0L);
		jobExecution.setExecutionContext(executionContext);
		archiveMetadataReader.setSourceService(sourceService);
		archiveMetadataReader.setValidator(validator);

		archiveMetadataReader.beforeStep(
				new StepExecution("test", jobExecution));
		archiveMetadataReader.readMetadata(content.getFile().getAbsolutePath(),
				"test", "false");

		assertNotNull("core file must be present",
				executionContext.getString("dwca.core.file"));
		assertEquals("fieldsTerminatedBy must be present", ",",
				executionContext.getString("dwca.core.fieldsTerminatedBy"));
		assertEquals("fieldsEnclosedBy must be present", '\"',
				executionContext.get("dwca.core.fieldsEnclosedBy"));
		assertEquals("encoding must be present", "UTF-8",
				executionContext.getString("dwca.core.encoding"));
		assertEquals("ignoreHeaderLines must be present", 0,
				executionContext.getInt("dwca.core.ignoreHeaderLines"));
		assertNotNull("field names must be present",
				executionContext.get("dwca.core.fieldNames"));
		String[] actualCoreFieldNames = (String[])
				executionContext.get("dwca.core.fieldNames");
		String[] expectedCoreFieldNames = new String[] {
				"http://rs.tdwg.org/dwc/terms/taxonID",
				"http://rs.tdwg.org/dwc/terms/scientificName",
				"http://rs.tdwg.org/dwc/terms/vernacularName",
				"http://rs.tdwg.org/dwc/terms/taxonomicStatus",
				"http://rs.tdwg.org/dwc/terms/taxonRank",
				"http://rs.tdwg.org/dwc/terms/scientificNameAuthorship",
				"http://purl.org/dc/terms/bibliographicCitation",
		"http://rs.tdwg.org/dwc/terms/parentNameUsageID" };
		for (String expectedCoreFieldName : expectedCoreFieldNames) {
			assertThat(actualCoreFieldNames,
					hasItemInArray(expectedCoreFieldName));
		}
		assertNotNull("description file must be present",
				executionContext.getString("dwca.description.file"));
		assertEquals("fieldsTerminatedBy must be present", ",",
				executionContext.getString(
						"dwca.description.fieldsTerminatedBy"));
		assertEquals("fieldsEnclosedBy must be present", '\"',
				executionContext.get("dwca.description.fieldsEnclosedBy"));
		assertEquals("encoding must be present", "UTF-8",
				executionContext.getString("dwca.description.encoding"));
		assertEquals("ignoreHeaderLines must be present", 0,
				executionContext.getInt("dwca.description.ignoreHeaderLines"));
		assertNotNull("field names must be present",
				executionContext.get("dwca.description.fieldNames"));

		String[] expectedDescriptionFieldNames = new String[] {
				"http://rs.tdwg.org/dwc/terms/taxonID",
				"http://purl.org/dc/terms/type",
				"http://purl.org/dc/terms/description"
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
		assertEquals("fieldsTerminatedBy must be present", ",",
				executionContext.getString(
						"dwca.image.fieldsTerminatedBy"));
		assertEquals("fieldsEnclosedBy must be present", '\"',
				executionContext.get("dwca.image.fieldsEnclosedBy"));
		assertEquals("encoding must be present", "UTF-8",
				executionContext.getString("dwca.image.encoding"));
		assertEquals("ignoreHeaderLines must be present", 0,
				executionContext.getInt("dwca.image.ignoreHeaderLines"));
		assertNotNull("field names must be present",
				executionContext.get("dwca.image.fieldNames"));

		String[] expectedImageFieldNames = new String[] {
				"http://rs.tdwg.org/dwc/terms/taxonID",
				"http://purl.org/dc/terms/identifier",
				"http://purl.org/dc/terms/title",
				"http://purl.org/dc/terms/format",
				"http://purl.org/dc/terms/license"
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
		assertEquals("fieldsTerminatedBy must be present", ",",
				executionContext.getString(
						"dwca.distribution.fieldsTerminatedBy"));
		assertEquals("fieldsEnclosedBy must be present", '\"',
				executionContext.get("dwca.distribution.fieldsEnclosedBy"));
		assertEquals("encoding must be present", "UTF-8",
				executionContext.getString("dwca.distribution.encoding"));
		assertEquals("ignoreHeaderLines must be present", 0,
				executionContext.getInt("dwca.distribution.ignoreHeaderLines"));
		assertNotNull("field names must be present",
				executionContext.get("dwca.distribution.fieldNames"));

		String[] expectedDistributionFieldNames = new String[] {
				"http://rs.tdwg.org/dwc/terms/taxonID",
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
		assertEquals("fieldsTerminatedBy must be present", ",",
				executionContext.getString(
						"dwca.reference.fieldsTerminatedBy"));
		assertEquals("fieldsEnclosedBy must be present", '\"',
				executionContext.get("dwca.reference.fieldsEnclosedBy"));
		assertEquals("encoding must be present", "UTF-8",
				executionContext.getString("dwca.reference.encoding"));
		assertEquals("ignoreHeaderLines must be present", 0,
				executionContext.getInt("dwca.reference.ignoreHeaderLines"));
		assertNotNull("field names must be present",
				executionContext.get("dwca.reference.fieldNames"));

		String[] expectedReferenceFieldNames = new String[] {
				"http://rs.tdwg.org/dwc/terms/taxonID",
				"http://purl.org/dc/terms/identifier",
				"",
				"http://purl.org/ontology/bibo/doi",
				"http://purl.org/ontology/bibo/isbn",
				"http://purl.org/ontology/bibo/issn",
				"",
				"",
				"http://purl.org/dc/terms/bibliographicCitation",
				"http://purl.org/dc/terms/title",
				"http://purl.org/dc/terms/creator",
				"http://purl.org/dc/terms/source",
				"http://purl.org/dc/terms/description",
				"http://purl.org/dc/terms/subject",
				"http://purl.org/dc/terms/language",
				"http://purl.org/dc/terms/type",
				"http://purl.org/dc/terms/date",
				"http://purl.org/dc/terms/created",
		"http://purl.org/dc/terms/modified" };

		String[] actualReferenceFieldNames = (String[])
				executionContext.get("dwca.reference.fieldNames");

		for (String expectedReferenceFieldName
				: expectedReferenceFieldNames) {
			assertThat(actualReferenceFieldNames,
					hasItemInArray(expectedReferenceFieldName));
		}
	}

}
