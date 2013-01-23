package org.emonocot.job.dwc.write;

import java.io.File;
import java.io.IOException;
import java.util.Date;

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
import org.springframework.batch.core.StepContribution;
import org.springframework.batch.core.scope.context.ChunkContext;
import org.springframework.batch.core.step.tasklet.Tasklet;
import org.springframework.batch.repeat.RepeatStatus;
import org.springframework.core.io.FileSystemResource;

import freemarker.template.TemplateException;


public class ArchiveMetadataWriter implements Tasklet {
	
	private TermFactory termFactory = new TermFactory();
	
	private String downloadFile;	

	private String[] taxonFields;

	private String[] descriptionFields;

	private String[] distributionFields;

	private String[] referenceFields;

	private String[] imageFields;

	private String[] typeAndSpecimenFields;

	private String[] measurementOrFactFields;

	private String[] vernacularNameFields;

	private String[] identifierFields;
	
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

	public void setDownloadFile(String downloadFile) {
		this.downloadFile = downloadFile;
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

	public RepeatStatus execute(StepContribution stepContribution, final ChunkContext chunkContext) throws Exception {
		Archive archive = new Archive();		
		
		archive.setCore(buildArchiveFile(taxonFields,DwcTerm.Taxon, DwcTerm.taxonID,"taxon.txt",0,"UTF-8",quoteCharacter,delimiter));
		
		if(descriptionFields != null) {
			archive.addExtension(buildArchiveFile(descriptionFields,GbifTerm.Description, DwcTerm.taxonID,"description.txt",0,"UTF-8",quoteCharacter,delimiter));
		}
		if(distributionFields != null) {
			archive.addExtension(buildArchiveFile(distributionFields,GbifTerm.Distribution, DwcTerm.taxonID,"distribution.txt",0,"UTF-8",quoteCharacter,delimiter));
		}
		if(referenceFields != null) {
			archive.addExtension(buildArchiveFile(referenceFields,GbifTerm.Reference, DwcTerm.taxonID,"reference.txt",0,"UTF-8",quoteCharacter,delimiter));
		}
		if(imageFields != null) {
			archive.addExtension(buildArchiveFile(imageFields,GbifTerm.Image, DwcTerm.taxonID,"image.txt",0,"UTF-8",quoteCharacter,delimiter));
		}
		if(typeAndSpecimenFields != null) {
			archive.addExtension(buildArchiveFile(typeAndSpecimenFields,GbifTerm.TypesAndSpecimen, DwcTerm.taxonID,"typeAndSpecimen.txt",0,"UTF-8",quoteCharacter,delimiter));
		}
		if(measurementOrFactFields != null) {
			archive.addExtension(buildArchiveFile(measurementOrFactFields,DwcTerm.MeasurementOrFact, DwcTerm.taxonID,"measurementOrFact.txt",0,"UTF-8",quoteCharacter,delimiter));
		}
		if(vernacularNameFields != null) {
			archive.addExtension(buildArchiveFile(vernacularNameFields,GbifTerm.VernacularName, DwcTerm.taxonID,"vernacularName.txt",0,"UTF-8",quoteCharacter,delimiter));
		}
		if(identifierFields != null) {
			archive.addExtension(buildArchiveFile(identifierFields,GbifTerm.Identifier, DwcTerm.taxonID,"identifier.txt",0,"UTF-8",quoteCharacter,delimiter));
		}
		
		archive.setMetadataLocation("eml.xml");
		File workDirectory = new File(outputDirectory.getFile(),downloadFile);
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

	private ArchiveFile buildArchiveFile(String[] fieldNames, ConceptTerm rowType, ConceptTerm idTerm, String location,
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
