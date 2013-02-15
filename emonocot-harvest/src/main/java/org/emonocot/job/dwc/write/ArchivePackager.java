package org.emonocot.job.dwc.write;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.util.zip.ZipEntry;
import java.util.zip.ZipOutputStream;

import org.springframework.batch.core.StepContribution;
import org.springframework.batch.core.scope.context.ChunkContext;
import org.springframework.batch.core.step.tasklet.Tasklet;
import org.springframework.batch.repeat.RepeatStatus;
import org.springframework.core.io.FileSystemResource;


public class ArchivePackager implements Tasklet {
	
	private String archiveFile;
	
	private FileSystemResource outputDirectory;
	
	private String[] descriptionFields;
	
	private String[] distributionFields;
	
	private String[] measurementOrFactFields;
	
	private String[] vernacularNameFields;
	
	private String[] identifierFields;
	
	private String[] imageFields;
	
	private String[] referenceFields;
	
	private String[] typeAndSpecimenFields;
	
	public void setArchiveFile(String archiveFile) {
		this.archiveFile = archiveFile;
	}
	
	public void setOutputDirectory(FileSystemResource outputDirectory) {
		this.outputDirectory = outputDirectory;
	}
	
	public void setDescriptionFields(String[] descriptionFields) {
		this.descriptionFields = descriptionFields;
	}
	
	public void setDistributionFields(String[] distributionFields) {
		this.distributionFields = distributionFields;
	}
	
	public void setMeasurementOrFactFields(String[] measurementOrFactFields) {
		this.measurementOrFactFields = measurementOrFactFields;
	}
	
	public void setVernacularNameFields(String[] vernacularNameFields) {
		this.vernacularNameFields = vernacularNameFields;
	}
	
	public void setImageFields(String[] imageFields) {
		this.imageFields = imageFields;
	}
	
	public void setIdentifierFields(String[] identifierFields) {
		this.identifierFields = identifierFields;
	}
	
	public void setReferenceFields(String[] referenceFields) {
		this.referenceFields = referenceFields;
	}
	
	public void setTypeAndSpecimenFields(String[] typeAndSpecimenFields) {
		this.typeAndSpecimenFields = typeAndSpecimenFields;
	}
	
	public RepeatStatus execute(StepContribution stepContribution, final ChunkContext chunkContext) throws Exception {
		File archive = new File(outputDirectory.getFile(),archiveFile  + ".zip");
		ZipOutputStream zipOutputStream = new ZipOutputStream(new FileOutputStream(archive));
		File workDirectory = new File(outputDirectory.getFile(),archiveFile);
		if(!workDirectory.exists()) {
			workDirectory.mkdir();
		}
		
		addFile(workDirectory, "taxon.txt", zipOutputStream);
		addFile(workDirectory, "eml.xml", zipOutputStream);
		addFile(workDirectory, "meta.xml", zipOutputStream);
		
		if(descriptionFields != null) {
			addFile(workDirectory, "description.txt", zipOutputStream);
		}
		if(distributionFields != null) {
			addFile(workDirectory, "distribution.txt", zipOutputStream);
		}
		if(referenceFields != null) {
			addFile(workDirectory, "reference.txt", zipOutputStream);
		}
		if(imageFields != null) {
			addFile(workDirectory, "image.txt", zipOutputStream);
		}
		if(identifierFields != null) {
			addFile(workDirectory, "identifier.txt", zipOutputStream);
		}
		if(vernacularNameFields != null) {
			addFile(workDirectory, "vernacularName.txt", zipOutputStream);
		}
		if(typeAndSpecimenFields != null) {
			addFile(workDirectory, "typeAndSpecimen.txt", zipOutputStream);
		}
		if(measurementOrFactFields != null) {
			addFile(workDirectory, "measurementOrFact.txt", zipOutputStream);
		}
		
		zipOutputStream.close();
		return RepeatStatus.FINISHED;
	}
	
	private void addFile(File workDirectory, String fileName, ZipOutputStream zipOutputStream) throws Exception {
	    byte[] buffer = new byte[1024];
	    File file = new File(workDirectory, fileName);
        FileInputStream fileInputStream = new FileInputStream(file);
        zipOutputStream.putNextEntry(new ZipEntry(fileName));
        int length;
        while ((length = fileInputStream.read(buffer)) > 0) {
            zipOutputStream.write(buffer, 0, length);
	    }
        zipOutputStream.closeEntry();
        fileInputStream.close();
	}
	
}
