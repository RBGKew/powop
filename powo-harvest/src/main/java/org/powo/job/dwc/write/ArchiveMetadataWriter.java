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
package org.powo.job.dwc.write;

import java.io.File;
import java.io.IOException;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import org.gbif.dwc.terms.DwcTerm;
import org.gbif.dwc.terms.GbifTerm;
import org.gbif.dwc.terms.Term;
import org.gbif.dwc.text.Archive;
import org.gbif.dwc.text.ArchiveField;
import org.gbif.dwc.text.ArchiveFile;
import org.gbif.dwc.text.MetaDescriptorWriter;
import org.gbif.metadata.eml.Agent;
import org.gbif.metadata.eml.Eml;
import org.gbif.metadata.eml.EmlWriter;
import org.joda.time.DateTime;
import org.joda.time.format.DateTimeFormat;
import org.joda.time.format.DateTimeFormatter;
import org.powo.api.job.ExtendedAcTerm;
import org.powo.api.job.TermFactory;
import org.springframework.batch.core.StepContribution;
import org.springframework.batch.core.scope.context.ChunkContext;
import org.springframework.batch.core.step.tasklet.Tasklet;
import org.springframework.batch.repeat.RepeatStatus;
import org.springframework.core.io.FileSystemResource;

import freemarker.template.TemplateException;
import lombok.Setter;

@Setter
public class ArchiveMetadataWriter implements Tasklet {

	private static final DateTimeFormatter dateTimeFormatter = DateTimeFormat.forPattern("dd/MM/YYYY");

	private static final Pattern defaultValuesPattern = Pattern.compile("((?:[^\\\\,]|\\\\.)*)(?:,|$)");

	private String archiveFile;

	private String[] taxonFields;

	private Map<String, String> taxonDefaultValues = new HashMap<>();

	private String[] descriptionFields;

	private Map<String, String> descriptionDefaultValues = new HashMap<>();

	private String[] distributionFields;

	private Map<String, String> distributionDefaultValues = new HashMap<>();

	private String[] referenceFields;

	private Map<String, String> referenceDefaultValues = new HashMap<>();

	private String[] imageFields;

	private Map<String, String> imageDefaultValues = new HashMap<>();

	private String[] typeAndSpecimenFields;

	private Map<String, String> typeAndSpecimenDefaultValues = new HashMap<>();

	private String[] measurementOrFactFields;

	private Map<String, String> measurementOrFactDefaultValues = new HashMap<>();

	private String[] vernacularNameFields;

	private Map<String, String> vernacularNameDefaultValues = new HashMap<>();

	private String[] identifierFields;

	private Map<String, String> identifierDefaultValues = new HashMap<>();

	private Character quoteCharacter;

	private String delimiter;

	private FileSystemResource outputDirectory;

	private String citationString;

	private String creatorEmail;

	private String creatorName;

	private String description;

	private String homepageUrl;

	private String identifier;

	private String logoUrl;

	private String publisherEmail;

	private String publisherName;

	private String rights;

	private String subject;

	private String title;

	private int ignoreHeaderLines = 0;

	Map<String, String> toDefaultValues(String defaultValueList) {
		Map<String, String> defaultValues = new HashMap<String, String>();
		if (defaultValueList != null && !defaultValueList.isEmpty()) {
			Matcher matcher = defaultValuesPattern.matcher(defaultValueList);
			while (matcher.find()) {
				String defaultValue = matcher.group(1);
				if (defaultValue.indexOf("=") != -1) {
					int i = defaultValue.indexOf("=");
					String key = defaultValue.substring(0, i);
					String value = defaultValue.substring(i + 1, defaultValue.length());
					value = value.replace("\\", "");
					defaultValues.put(key, value);
				}
			}
		}
		return defaultValues;
	}

	public RepeatStatus execute(StepContribution stepContribution, final ChunkContext chunkContext) throws Exception {
		Archive archive = new Archive();

		archive.setCore(buildArchiveFile(taxonFields, taxonDefaultValues, DwcTerm.Taxon, DwcTerm.taxonID, "taxon.txt",
				ignoreHeaderLines, "UTF-8", quoteCharacter, delimiter));

		if (descriptionFields != null) {
			archive.addExtension(buildArchiveFile(descriptionFields, descriptionDefaultValues, GbifTerm.Description,
					DwcTerm.taxonID, "description.txt", ignoreHeaderLines, "UTF-8", quoteCharacter, delimiter));
		}
		if (distributionFields != null) {
			archive.addExtension(buildArchiveFile(distributionFields, distributionDefaultValues, GbifTerm.Distribution,
					DwcTerm.taxonID, "distribution.txt", ignoreHeaderLines, "UTF-8", quoteCharacter, delimiter));
		}
		if (referenceFields != null) {
			archive.addExtension(buildArchiveFile(referenceFields, referenceDefaultValues, GbifTerm.Reference,
					DwcTerm.taxonID, "reference.txt", ignoreHeaderLines, "UTF-8", quoteCharacter, delimiter));
		}
		if (imageFields != null) {
			archive.addExtension(buildArchiveFile(imageFields, imageDefaultValues, ExtendedAcTerm.Multimedia,
					DwcTerm.taxonID, "image.txt", ignoreHeaderLines, "UTF-8", quoteCharacter, delimiter));
		}
		if (typeAndSpecimenFields != null) {
			archive.addExtension(buildArchiveFile(typeAndSpecimenFields, typeAndSpecimenDefaultValues,
					GbifTerm.TypesAndSpecimen, DwcTerm.taxonID, "typeAndSpecimen.txt", ignoreHeaderLines, "UTF-8",
					quoteCharacter, delimiter));
		}
		if (measurementOrFactFields != null) {
			archive.addExtension(buildArchiveFile(measurementOrFactFields, measurementOrFactDefaultValues,
					DwcTerm.MeasurementOrFact, DwcTerm.taxonID, "measurementOrFact.txt", ignoreHeaderLines, "UTF-8",
					quoteCharacter, delimiter));
		}
		if (vernacularNameFields != null) {
			archive.addExtension(buildArchiveFile(vernacularNameFields, vernacularNameDefaultValues,
					GbifTerm.VernacularName, DwcTerm.taxonID, "vernacularName.txt", ignoreHeaderLines, "UTF-8",
					quoteCharacter, delimiter));
		}
		if (identifierFields != null) {
			archive.addExtension(buildArchiveFile(identifierFields, identifierDefaultValues, GbifTerm.Identifier,
					DwcTerm.taxonID, "identifier.txt", ignoreHeaderLines, "UTF-8", quoteCharacter, delimiter));
		}

		archive.setMetadataLocation("eml.xml");
		File workDirectory = new File(outputDirectory.getFile(), archiveFile);
		if (!workDirectory.exists()) {
			workDirectory.mkdir();
		}

		File metaFile = new File(workDirectory, "meta.xml");
		try {
			MetaDescriptorWriter.writeMetaFile(metaFile, archive);
		} catch (TemplateException te) {
			throw new IOException("Exception writing meta.xml", te);
		}

		File emlFile = new File(workDirectory, "eml.xml");
		Eml eml = getEml();
		try {
			EmlWriter.writeEmlFile(emlFile, eml);
		} catch (TemplateException te) {
			throw new IOException("Exception writing eml.xml", te);
		}

		return RepeatStatus.FINISHED;
	}

	private Eml getEml() {
		Eml eml = new Eml();
		if (citationString != null) {
			DateTime now = new DateTime();
			Integer year = new Integer(now.getYear());
			citationString = citationString.replace("{0}", year.toString()).replace("{1}", dateTimeFormatter.print(now));
		}

		eml.setCitation(citationString, identifier);
		Agent resourceCreator = new Agent();
		resourceCreator.setEmail(creatorEmail);
		resourceCreator.setFirstName(creatorName);
		eml.setResourceCreator(resourceCreator);
		eml.setDescription(description);
		eml.setHomepageUrl(homepageUrl);
		eml.setLogoUrl(logoUrl);
		eml.setTitle(title);
		eml.setSubject(subject);
		eml.setPublished(new Date());
		Agent metadataProvider = new Agent();
		metadataProvider.setEmail(publisherEmail);
		metadataProvider.setFirstName(publisherName);
		eml.setMetadataProvider(metadataProvider);
		eml.setIntellectualRights(rights);

		return eml;
	}

	private ArchiveFile buildArchiveFile(String[] fieldNames, Map<String, String> defaultValues, Term rowType,
			Term idTerm, String location, Integer ignoreHeaderLines, String encoding, Character fieldsEnclosedBy,
			String fieldsTerminatedBy) {
		ArchiveFile archiveFile = new ArchiveFile();
		ArchiveField idField = new ArchiveField();
		idField.setIndex(0);
		idField.setTerm(idTerm);
		archiveFile.setId(idField);

		for (int i = 1; i < fieldNames.length; i++) {
			Term term = TermFactory.findTerm(fieldNames[i]);
			ArchiveField archiveField = new ArchiveField();
			archiveField.setTerm(term);
			archiveField.setIndex(i);
			if (defaultValues.containsKey(fieldNames[i])) {
				archiveField.setDefaultValue(defaultValues.get(fieldNames[i]));
				defaultValues.remove(fieldNames[i]);
			}
			archiveFile.addField(archiveField);
		}

		for (String fieldName : defaultValues.keySet()) {
			Term term = TermFactory.findTerm(fieldName);
			ArchiveField archiveField = new ArchiveField();
			archiveField.setTerm(term);
			archiveField.setDefaultValue(defaultValues.get(fieldName));
			archiveFile.addField(archiveField);
		}

		archiveFile.setRowType(rowType.qualifiedName());
		archiveFile.setIgnoreHeaderLines(ignoreHeaderLines);
		archiveFile.setEncoding(encoding);
		archiveFile.setFieldsEnclosedBy(fieldsEnclosedBy);
		archiveFile.setFieldsTerminatedBy(fieldsTerminatedBy);
		archiveFile.addLocation(location);

		return archiveFile;
	}
}