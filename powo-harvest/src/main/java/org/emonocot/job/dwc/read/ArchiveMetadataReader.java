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
package org.emonocot.job.dwc.read;

import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.LineNumberReader;
import java.nio.charset.Charset;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Set;

import javax.validation.ConstraintViolation;
import javax.validation.Validator;

import org.emonocot.api.OrganisationService;
import org.emonocot.api.job.ExtendedAcTerm;
import org.emonocot.api.job.SkosTerm;
import org.emonocot.model.registry.Organisation;
import org.gbif.dwc.terms.DwcTerm;
import org.gbif.dwc.terms.GbifTerm;
import org.gbif.dwc.terms.Term;
import org.gbif.dwc.text.Archive;
import org.gbif.dwc.text.ArchiveField;
import org.gbif.dwc.text.ArchiveFile;
import org.gbif.dwc.text.UnsupportedArchiveException;
import org.gbif.metadata.BasicMetadata;
import org.gbif.metadata.eml.Eml;
import org.gbif.metadata.eml.EmlFactory;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.batch.core.ExitStatus;
import org.springframework.batch.core.StepExecution;
import org.springframework.batch.core.StepExecutionListener;
import org.springframework.batch.item.ExecutionContext;
import org.springframework.beans.factory.annotation.Autowired;
import org.xml.sax.SAXException;

public class ArchiveMetadataReader implements StepExecutionListener {
	private Logger logger = LoggerFactory.getLogger(ArchiveMetadataReader.class);

	private StepExecution stepExecution;

	private String sourceName;

	private OrganisationService organisationService;

	private Validator validator;

	@Autowired
	public final void setSourceService(final OrganisationService sourceService) {
		this.organisationService = sourceService;
	}

	@Autowired
	public final void setValidator(Validator validator) {
		this.validator = validator;
	}

	/**
	 *
	 * @param archiveDirectoryName
	 *            The directory where the DwC Archive has been unpacked to
	 * @param sourceName Set the name of the source
	 * @param failOnError Whether to fail if there is an error
	 * @return An exit status indicating whether the step has been completed or
	 *         failed
	 */
	public final ExitStatus readMetadata(final String archiveDirectoryName, final String sourceName, String metaErrorsFail) {
		this.sourceName = sourceName;
		boolean failOnError = "false".equalsIgnoreCase(metaErrorsFail) ? false : true;
		try {
			File archiveDirectory = new File(archiveDirectoryName);
			File metaDir = getMetaDirectory(archiveDirectory);
			if(metaDir == null) {
				logger.error("Could not find metadata directory in " +  archiveDirectoryName);
				return ExitStatus.FAILED;
			}
			Archive archive = ArchiveFactory.openArchive(metaDir);

			ArchiveFile core = archive.getCore();

			if (archive.getMetadataLocation() != null) {
				String metadataFileName = metaDir.getCanonicalPath() + File.separator  + archive.getMetadataLocation();
				try {
					Eml eml = EmlFactory.build(new FileInputStream(metadataFileName));
					updateSourceMetadata(eml);
				} catch (SAXException e) {
					logger.error(e.getMessage(), e);
				}
			}
			getMetadata(core, "core", DwcTerm.taxonID, failOnError);

			if (archive.getExtension(GbifTerm.Description) != null) {
				getMetadata(archive.getExtension(GbifTerm.Description), "description", DwcTerm.taxonID, failOnError);
			}

			if (archive.getExtension(GbifTerm.Distribution) != null) {
				getMetadata(archive.getExtension(GbifTerm.Distribution), "distribution", DwcTerm.taxonID, failOnError);
			}

			if (archive.getExtension(ExtendedAcTerm.Multimedia) != null) {
				getMetadata(archive.getExtension(ExtendedAcTerm.Multimedia), "image", DwcTerm.taxonID, failOnError);
			}

			if (archive.getExtension(GbifTerm.Reference) != null) {
				getMetadata(archive.getExtension(GbifTerm.Reference), "reference", DwcTerm.taxonID, failOnError);
			}

			if (archive.getExtension(GbifTerm.Identifier) != null) {
				getMetadata(archive.getExtension(GbifTerm.Identifier), "identifier", DwcTerm.taxonID, failOnError);
			}

			if (archive.getExtension(DwcTerm.Identification) != null) {
				getMetadata(archive.getExtension(DwcTerm.Identification), "identification", DwcTerm.taxonID, failOnError);
			}

			if (archive.getExtension(DwcTerm.MeasurementOrFact) != null) {
				getMetadata(archive.getExtension(DwcTerm.MeasurementOrFact), "measurementOrFact", DwcTerm.taxonID, failOnError);
			}

			if (archive.getExtension(GbifTerm.VernacularName) != null) {
				getMetadata(archive.getExtension(GbifTerm.VernacularName), "vernacularName", DwcTerm.taxonID, failOnError);
			}

			if (archive.getExtension(GbifTerm.TypesAndSpecimen) != null) {
				getMetadata(archive.getExtension(GbifTerm.TypesAndSpecimen), "typeAndSpecimen", DwcTerm.taxonID, failOnError);
			}

			if (archive.getExtension(SkosTerm.Concept) != null) {
				getMetadata(archive.getExtension(SkosTerm.Concept), "term", DwcTerm.taxonID, failOnError);
			}

			if (archive.getExtension(GbifTerm.Multimedia) != null) {
				getMetadata(archive.getExtension(GbifTerm.Multimedia), "multimedia", DwcTerm.taxonID, failOnError);
			}
		} catch (UnsupportedArchiveException uae) {
			logger.error("Unsupported Archive Exception reading "
					+ archiveDirectoryName + " " + uae.getLocalizedMessage());
			return ExitStatus.FAILED;
		} catch (IOException ioe) {
			logger.error("Input Output Exception reading " + archiveDirectoryName
					+ " " + ioe.getLocalizedMessage());
			return ExitStatus.FAILED;
		}

		return ExitStatus.COMPLETED;
	}

	/**
	 * Recurses over a directory system and returns the first enclosing
	 * directory which contains a file with the suffix *.xml
	 *
	 * @param archiveDirectory
	 * @return a file or null if no meta.xml file can be found
	 */
	private File getMetaDirectory(File archiveDirectory) {
		for(File f : archiveDirectory.listFiles()) {
			if(f.getName().endsWith(".xml")) {
				return archiveDirectory;
			} else {
				if(f.isDirectory()) {
					File dir = getMetaDirectory(f);
					if(dir != null) {
						return dir;
					}
				}
			}
		}
		return null;
	}

	private void updateSourceMetadata(final BasicMetadata basicMetadata) {
		boolean update = false;
		Organisation source = organisationService.find(sourceName);
		if (!nullSafeEquals(source.getBibliographicCitation(), basicMetadata.getCitationString())) {
			source.setBibliographicCitation(basicMetadata.getCitationString());
			update = true;
		}
		if (!nullSafeEquals(source.getCreator(), basicMetadata.getCreatorName())) {
			source.setCreator(basicMetadata.getCreatorName());
			update = true;
		}
		if (!nullSafeEquals(source.getDescription(), basicMetadata.getDescription())) {
			source.setDescription(basicMetadata.getDescription());
			update = true;
		}
		if (!nullSafeEquals(source.getPublisherEmail(), basicMetadata.getPublisherEmail())) {
			source.setPublisherEmail(basicMetadata.getPublisherEmail());
			update = true;
		}
		if (!nullSafeEquals(source.getPublisherName(), basicMetadata.getPublisherName())) {
			source.setPublisherName(basicMetadata.getPublisherName());
			update = true;
		}
		if (!nullSafeEquals(source.getSubject(), basicMetadata.getSubject())) {
			source.setSubject(basicMetadata.getSubject());
			update = true;
		}

		if (update) {
			Set<ConstraintViolation<Organisation>> violations = validator.validate(source);
			if (violations.isEmpty()) {
				logger.info("Updating metadata for source " + sourceName);
						organisationService.saveOrUpdate(source);
			} else {
				for (ConstraintViolation<Organisation> violation : violations) {
					logger.error(violation.getMessage());
				}
			}
		}
	}

	/**
	 *
	 * @param string1 Set the first string
	 * @param string2 Set the second string
	 * @return true if the strings are equal, false otherwise
	 */
	private boolean nullSafeEquals(final String string1, final String string2) {
		if (string1 == null) {
			return string1 == string2;
		} else {
			return string1.equals(string2);
		}
	}

	/**
	 *
	 * @param archiveFile
	 *            The archive file to examine
	 * @param prefix
	 *            the prefix to append to the properties
	 * @param identifierTerm
	 *            the name of the identifier property
	 * @param failOnError Whether to fail if there is an error
	 * @throws IOException
	 */
	private void getMetadata(final ArchiveFile archiveFile,
			final String prefix, final Term identifierTerm, boolean failOnError) throws IOException {
		logger.debug("Processing " + archiveFile.getRowType());

		ExecutionContext executionContext = this.stepExecution.getJobExecution().getExecutionContext();


		executionContext.put("dwca." + prefix + ".file", archiveFile.getLocationFile().getAbsolutePath());
		executionContext.put("dwca." + prefix + ".fieldsTerminatedBy", archiveFile.getFieldsTerminatedBy());
		executionContext.put("dwca." + prefix + ".linesTerminatedBy", archiveFile.getLinesTerminatedBy());
		if(archiveFile.getFieldsEnclosedBy() != null) {
			executionContext.put("dwca." + prefix + ".fieldsEnclosedBy", archiveFile.getFieldsEnclosedBy());
		} else {
			executionContext.put("dwca." + prefix + ".fieldsEnclosedBy", '\u0000');
		}
		executionContext.put("dwca." + prefix + ".encoding", archiveFile.getEncoding());

		Integer headerLinesToSkip = 0;
		if (archiveFile.getIgnoreHeaderLines() != null) {
			headerLinesToSkip = archiveFile.getIgnoreHeaderLines();
		}

		executionContext.put("dwca." + prefix + ".ignoreHeaderLines", headerLinesToSkip);
		ArchiveField idField = archiveFile.getId();
		idField.setTerm(identifierTerm);

		List<ArchiveField> fields = archiveFile.getFieldsSorted();
		/**
		 * Its not clear if you should include the id field twice but if it is
		 * present twice, ignore it.
		 */
		boolean idListed = false;
		for (ArchiveField field : fields) {
			if (field.getIndex() != null
					&& field.getIndex().equals(idField.getIndex())) {
				idListed = true;
				break;
			}
		}
		if (!idListed) {
			fields.add(idField.getIndex(), idField);
		}

		File file = archiveFile.getLocationFile();
		FileInputStream fileInputStream = new FileInputStream(file);
		LineNumberReader lineNumberReader = new LineNumberReader(new InputStreamReader(fileInputStream,Charset.forName(archiveFile.getEncoding())));

		String firstDataLine = null;
		String line = null;
		while((line = lineNumberReader.readLine()) != null) {
			if(lineNumberReader.getLineNumber() == (headerLinesToSkip + 1)) {
				firstDataLine = line;
			}
		}

		Integer totalColumns = 0;
		Integer maxIndex = 0;
		for(ArchiveField field : fields) {
			if(field.getIndex() != null && field.getIndex() > maxIndex) {
				maxIndex = field.getIndex();
			}
		}
		if(firstDataLine != null) {
			// Note java.lang.String.split does not include trailing empty string
			totalColumns = firstDataLine.split(archiveFile.getFieldsTerminatedBy(), -1).length;
		} else {
			totalColumns = maxIndex + 1;
		}

		if((maxIndex + 1) > totalColumns) {
			lineNumberReader.close();
			if(failOnError) {
				throw new RuntimeException("Metadata for " + archiveFile.getRowType()
						+ " indicates that there should be at least "+ (maxIndex + 1)
						+ " columns but the first data line in the file has only " + totalColumns + " values");
			} else {
				logger.error("Error reading metadata", new RuntimeException("Metadata for " + archiveFile.getRowType() +
						" indicates that there should be at least " + (maxIndex + 1) +
						" columns but the first data line in the file has only "+ totalColumns + " values"));

				executionContext.put(prefix + ".processing.mode", "SKIP_WITH_ERROR");
				return;
			}
		}

		executionContext.put("dwca." + prefix + ".totalColumns", totalColumns);
		executionContext.put("dwca." + prefix + ".totalRecords", lineNumberReader.getLineNumber() - headerLinesToSkip);
		executionContext.put("dwca." + prefix + ".fieldNames", toFieldNames(fields, totalColumns));
		executionContext.put("dwca." + prefix + ".defaultValues",  getDefaultValues(fields));
		lineNumberReader.close();
	}

	/**
	 * Method which extracts the default fields from the archive and exposes
	 * them.
	 *
	 * @param fields
	 *            the list of fields
	 * @return a map of property names -> default values
	 */
	private Map<String, String> getDefaultValues(final List<ArchiveField> fields) {
		Map<String, String> defaultValues = new HashMap<String, String>();
		for (ArchiveField field : fields) {
			if (field.getDefaultValue() != null && field.getIndex() == null) {
				defaultValues.put(field.getTerm().qualifiedName(),
						field.getDefaultValue());
			}
		}
		return defaultValues;
	}

	/**
	 * Method which converts from archive fields to string field names. To be
	 * used by fieldSet mapper
	 *
	 * @param fields
	 *            the DwC/A fields
	 * @param totalColumns TODO
	 * @return an array of string names
	 */
	private String[] toFieldNames(final List<ArchiveField> fields, Integer totalColumns) {

		List<String> names = new ArrayList<String>(totalColumns);
		for(int i = 0; i < totalColumns; i++) {
			names.add("");
		}

		for (ArchiveField field : fields) {
			logger.debug("Archive contains field " + field.getTerm().qualifiedName());
			if(field.getIndex() != null) {
				if(field.getDefaultValue() != null) {
					names.set(field.getIndex(), field.getTerm().qualifiedName() + " " + field.getDefaultValue());
				} else {
					names.set(field.getIndex(), field.getTerm().qualifiedName());
				}
			}
		}
		logger.info("Archive contains field names " + names);
		return names.toArray(new String[names.size()]);
	}

	public final ExitStatus afterStep(final StepExecution newStepExecution) {
		return null;
	}

	public final void beforeStep(final StepExecution newStepExecution) {
		this.stepExecution = newStepExecution;
	}

}
