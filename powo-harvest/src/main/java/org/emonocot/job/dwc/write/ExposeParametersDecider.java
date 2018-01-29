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
package org.emonocot.job.dwc.write;

import java.io.File;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.batch.core.JobExecution;
import org.springframework.batch.core.JobParameters;
import org.springframework.batch.core.StepExecution;
import org.springframework.batch.core.job.flow.FlowExecutionStatus;
import org.springframework.batch.core.job.flow.JobExecutionDecider;
import org.springframework.batch.item.ExecutionContext;
import org.springframework.core.io.FileSystemResource;

public class ExposeParametersDecider implements JobExecutionDecider {

	private Logger logger = LoggerFactory.getLogger(ExposeParametersDecider.class);

	private String jobParameterName = null;

	private FileSystemResource outputDirectory;

	public void setJobParameterName(String jobParameterName) {
		this.jobParameterName = jobParameterName;
	}

	public void setOutputDirectory(FileSystemResource outputDirectory) {
		this.outputDirectory = outputDirectory;
	}

	@Override
	public FlowExecutionStatus decide(JobExecution jobExecution, StepExecution stepExecution) {
		if (jobExecution.getJobParameters().getString(jobParameterName) != null) {
			ExecutionContext executionContext = jobExecution.getExecutionContext();
			JobParameters jobParameters = jobExecution.getJobParameters();
			if(jobParameterName.equals("download.taxon")) {
				setExecutionContext(executionContext,jobParameters,"taxon.txt","org.emonocot.model.Taxon", "http://rs.tdwg.org/dwc/terms/Taxon");
			} else if(jobParameterName.equals("download.description")) {
				setExecutionContext(executionContext,jobParameters,"description.txt","org.emonocot.model.Description", "http://rs.gbif.org/terms/1.0/Description");
			} else if(jobParameterName.equals("download.distribution")) {
				setExecutionContext(executionContext,jobParameters,"distribution.txt","org.emonocot.model.Distribution", "http://rs.gbif.org/terms/1.0/Distribution");
			} else if(jobParameterName.equals("download.image")) {
				setExecutionContext(executionContext,jobParameters,"image.txt","org.emonocot.model.Image", "http://rs.gbif.org/terms/1.0/Image");
			} else if(jobParameterName.equals("download.reference")) {
				setExecutionContext(executionContext,jobParameters,"reference.txt","org.emonocot.model.Reference", "http://rs.gbif.org/terms/1.0/Reference");
			} else if(jobParameterName.equals("download.typeAndSpecimen")) {
				setExecutionContext(executionContext,jobParameters,"typeAndSpecimen.txt","org.emonocot.model.TypeAndSpecimen", "http://rs.gbif.org/terms/1.0/TypesAndSpecimen");
			} else if(jobParameterName.equals("download.measurementOrFact")) {
				setExecutionContext(executionContext,jobParameters,"measurementOrFact.txt","org.emonocot.model.MeasurementOrFact", "http://rs.tdwg.org/dwc/terms/MeasurementOrFact");
			} else if(jobParameterName.equals("download.vernacularName")) {
				setExecutionContext(executionContext,jobParameters,"vernacularName.txt","org.emonocot.model.VernacularName","http://rs.gbif.org/terms/1.0/VernacularName");
			} else if(jobParameterName.equals("download.identifier")) {
				setExecutionContext(executionContext,jobParameters,"identifier.txt","org.emonocot.model.Identifier", "http://rs.gbif.org/terms/1.0/Identifier");
			}
			return new FlowExecutionStatus("true");
		} else {
			return new FlowExecutionStatus("false");
		}
	}

	private void setExecutionContext(ExecutionContext executionContext, JobParameters jobParameters, String fileName,String downloadType, String extension) {
		logger.debug(jobParameterName + " Setting download.fields to " + jobParameters.getString(jobParameterName));
		executionContext.put("download.fields", jobParameters.getString(jobParameterName));
		File workDirectory = new File(outputDirectory.getFile(),jobParameters.getString("download.file"));
		if(!workDirectory.exists()) {
			workDirectory.mkdir();
			executionContext.put("working.directory", workDirectory.getAbsolutePath());
		}
		File downloadFile =  new File(workDirectory,fileName);
		executionContext.put("download.file", downloadFile.getAbsolutePath());
		executionContext.put("download.type", downloadType);
		executionContext.put("download.extension", extension);
	}
}
