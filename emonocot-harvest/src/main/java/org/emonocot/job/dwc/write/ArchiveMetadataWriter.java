package org.emonocot.job.dwc.write;

import java.io.File;
import java.io.IOException;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import org.emonocot.api.job.TermFactory;
import org.gbif.dwc.terms.ConceptTerm;
import org.gbif.dwc.terms.DwcTerm;
import org.gbif.dwc.terms.GbifTerm;

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
import org.springframework.batch.core.StepContribution;
import org.springframework.batch.core.scope.context.ChunkContext;
import org.springframework.batch.core.step.tasklet.Tasklet;
import org.springframework.batch.repeat.RepeatStatus;
import org.springframework.core.io.FileSystemResource;

import freemarker.template.TemplateException;


public class ArchiveMetadataWriter implements Tasklet {
	
	private DateTimeFormatter dateTimeFormatter = DateTimeFormat.forPattern("dd/MM/YYYY");
	
	private TermFactory termFactory = new TermFactory();
	
	private Pattern defaultValuesPattern = Pattern.compile("((?:[^\\\\,]|\\\\.)*)(?:,|$)");
	
	private String archiveFile;	

	private String[] taxonFields;
	
	private Map<String,String> taxonDefaultValues = new HashMap<String,String>();

	private String[] descriptionFields;
	
	private Map<String,String> descriptionDefaultValues = new HashMap<String,String>();

	private String[] distributionFields;
	
	private Map<String,String> distributionDefaultValues = new HashMap<String,String>();

	private String[] referenceFields;
	
	private Map<String,String> referenceDefaultValues = new HashMap<String,String>();

	private String[] imageFields;
	
	private Map<String,String> imageDefaultValues = new HashMap<String,String>();

	private String[] typeAndSpecimenFields;
	
	private Map<String,String> typeAndSpecimenDefaultValues = new HashMap<String,String>();

	private String[] measurementOrFactFields;
	
	private Map<String,String> measurementOrFactDefaultValues = new HashMap<String,String>();

	private String[] vernacularNameFields;
	
	private Map<String,String> vernacularNameDefaultValues = new HashMap<String,String>();

	private String[] identifierFields;
	
	private Map<String,String> identifierDefaultValues = new HashMap<String,String>();
	
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
	
	public void setCitationString(String citationString) {
		this.citationString = citationString;
	}

	public void setCreatorEmail(String creatorEmail) {
		this.creatorEmail = creatorEmail;
	}

	public void setCreatorName(String creatorName) {
		this.creatorName = creatorName;
	}

	public void setDescription(String description) {
		this.description = description;
	}

	public void setHomepageUrl(String homepageUrl) {
		this.homepageUrl = homepageUrl;
	}

	public void setIdentifier(String identifier) {
		this.identifier = identifier;
	}

	public void setLogoUrl(String logoUrl) {
		this.logoUrl = logoUrl;
	}

	public void setPublisherEmail(String publisherEmail) {
		this.publisherEmail = publisherEmail;
	}

	public void setPublisherName(String publisherName) {
		this.publisherName = publisherName;
	}

	public void setRights(String rights) {
		this.rights = rights;
	}

	public void setSubject(String subject) {
		this.subject = subject;
	}

	public void setTitle(String title) {
		this.title = title;
	}

	public void setQuoteCharacter(Character quoteCharacter) {
		this.quoteCharacter = quoteCharacter;
	}

	public void setDelimiter(String delimiter) {
		this.delimiter = delimiter;
	}

	public void setArchiveFile(String archiveFile) {
		this.archiveFile = archiveFile;
	}
	
	public void setTaxonDefaultValues(String taxonDefaultValues) {
		this.taxonDefaultValues = toDefaultValues(taxonDefaultValues);
	}

	public void setDescriptionDefaultValues(String descriptionDefaultValues) {
		this.descriptionDefaultValues = toDefaultValues(descriptionDefaultValues);
	}

	public void setDistributionDefaultValues(String distributionDefaultValues) {
		this.distributionDefaultValues = toDefaultValues(distributionDefaultValues);
	}

	public void setReferenceDefaultValuesFields(String referenceDefaultValues) {
		this.referenceDefaultValues = toDefaultValues(referenceDefaultValues);
	}

	public void setImageDefaultValues(String imageDefaultValues) {
		this.imageDefaultValues = toDefaultValues(imageDefaultValues);
	}

	public void setTypeAndSpecimenDefaultValues(String typeAndSpecimenDefaultValues) {
		this.typeAndSpecimenDefaultValues = toDefaultValues(typeAndSpecimenDefaultValues);
	}

	public void setMeasurementOrFactDefaultValues(String measurementOrFactDefaultValues) {
		this.measurementOrFactDefaultValues = toDefaultValues(measurementOrFactDefaultValues);
	}

	public void setVernacularNameDefaultValues(String vernacularNameDefaultValues) {
		this.vernacularNameDefaultValues = toDefaultValues(vernacularNameDefaultValues);
	}

	public void setIdentifierDefaultValues(String identifierDefaultValues) {
		this.identifierDefaultValues = toDefaultValues(identifierDefaultValues);
	}
	
	public void setReferenceDefaultValues(String referenceDefaultValues) {
		this.referenceDefaultValues = toDefaultValues(referenceDefaultValues);
	}
	
	public void setTaxonFields(String[] taxonFields) {
		this.taxonFields = taxonFields;
	}

	public void setDescriptionFields(String[] descriptionFields) {
		this.descriptionFields = descriptionFields;
	}

	public void setDistributionFields(String[] distributionFields) {
		this.distributionFields = distributionFields;
	}

	public void setReferenceFields(String[] referenceFields) {
		this.referenceFields = referenceFields;
	}

	public void setImageFields(String[] imageFields) {
		this.imageFields = imageFields;
	}

	public void setTypeAndSpecimenFields(String[] typeAndSpecimenFields) {
		this.typeAndSpecimenFields = typeAndSpecimenFields;
	}

	public void setMeasurementOrFactFields(String[] measurementOrFactFields) {
		this.measurementOrFactFields = measurementOrFactFields;
	}

	public void setVernacularNameFields(String[] vernacularNameFields) {
		this.vernacularNameFields = vernacularNameFields;
	}

	public void setIdentifierFields(String[] identifierFields) {
		this.identifierFields = identifierFields;
	}
	
	public void setOutputDirectory(FileSystemResource outputDirectory) {
		this.outputDirectory = outputDirectory;
	}
	
	Map<String,String> toDefaultValues(String defaultValueList) {
		
		Map<String,String> defaultValues = new HashMap<String,String>();
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
		
		archive.setCore(buildArchiveFile(taxonFields,taxonDefaultValues,DwcTerm.Taxon, DwcTerm.taxonID,"taxon.txt",0,"UTF-8",quoteCharacter,delimiter));
		
		if(descriptionFields != null) {
			archive.addExtension(buildArchiveFile(descriptionFields,descriptionDefaultValues,GbifTerm.Description, DwcTerm.taxonID,"description.txt",0,"UTF-8",quoteCharacter,delimiter));
		}
		if(distributionFields != null) {
			archive.addExtension(buildArchiveFile(distributionFields,distributionDefaultValues,GbifTerm.Distribution, DwcTerm.taxonID,"distribution.txt",0,"UTF-8",quoteCharacter,delimiter));
		}
		if(referenceFields != null) {
			archive.addExtension(buildArchiveFile(referenceFields,referenceDefaultValues,GbifTerm.Reference, DwcTerm.taxonID,"reference.txt",0,"UTF-8",quoteCharacter,delimiter));
		}
		if(imageFields != null) {
			archive.addExtension(buildArchiveFile(imageFields,imageDefaultValues,GbifTerm.Image, DwcTerm.taxonID,"image.txt",0,"UTF-8",quoteCharacter,delimiter));
		}
		if(typeAndSpecimenFields != null) {
			archive.addExtension(buildArchiveFile(typeAndSpecimenFields,typeAndSpecimenDefaultValues,GbifTerm.TypesAndSpecimen, DwcTerm.taxonID,"typeAndSpecimen.txt",0,"UTF-8",quoteCharacter,delimiter));
		}
		if(measurementOrFactFields != null) {
			archive.addExtension(buildArchiveFile(measurementOrFactFields,measurementOrFactDefaultValues,DwcTerm.MeasurementOrFact, DwcTerm.taxonID,"measurementOrFact.txt",0,"UTF-8",quoteCharacter,delimiter));
		}
		if(vernacularNameFields != null) {
			archive.addExtension(buildArchiveFile(vernacularNameFields,vernacularNameDefaultValues,GbifTerm.VernacularName, DwcTerm.taxonID,"vernacularName.txt",0,"UTF-8",quoteCharacter,delimiter));
		}
		if(identifierFields != null) {
			archive.addExtension(buildArchiveFile(identifierFields,identifierDefaultValues,GbifTerm.Identifier, DwcTerm.taxonID,"identifier.txt",0,"UTF-8",quoteCharacter,delimiter));
		}
		
		archive.setMetadataLocation("eml.xml");
		File workDirectory = new File(outputDirectory.getFile(),archiveFile);
		if(!workDirectory.exists()) {
			workDirectory.mkdir();
		}
		File metaFile = new File(workDirectory,"meta.xml");
		try {
		    MetaDescriptorWriter.writeMetaFile(metaFile, archive);
		} catch (TemplateException te) {
			throw new IOException("Exception writing meta.xml", te);
		}
		
		File emlFile = new File(workDirectory,"eml.xml");
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
		if(citationString != null) {
		    DateTime now = new DateTime();
		    Integer year = new Integer(now.getYear());
		    citationString = citationString.replace("{0}", year.toString()).replace("{1}", dateTimeFormatter.print(now));
		}
		eml.setCitation(citationString,identifier);
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

	private ArchiveFile buildArchiveFile(String[] fieldNames, Map<String,String> defaultValues, ConceptTerm rowType, ConceptTerm idTerm, String location,
	    Integer ignoreHeaderLines, String encoding, Character fieldsEnclosedBy, String fieldsTerminatedBy) {
		ArchiveFile archiveFile = new ArchiveFile();
		ArchiveField idField = new ArchiveField();
		idField.setIndex(0);
		idField.setTerm(idTerm);
		archiveFile.setId(idField);
		
		for(int i = 1; i < fieldNames.length; i++) {
			ConceptTerm term = termFactory.findTerm(fieldNames[i]);
			ArchiveField archiveField = new ArchiveField();
			archiveField.setTerm(term);
			archiveField.setIndex(i);
			if(defaultValues.containsKey(fieldNames[i])) {
				archiveField.setDefaultValue(defaultValues.get(fieldNames[i]));
				defaultValues.remove(fieldNames[i]);
			}
			archiveFile.addField(archiveField);
		}
		for(String fieldName : defaultValues.keySet()) {
			ConceptTerm term = termFactory.findTerm(fieldName);
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
