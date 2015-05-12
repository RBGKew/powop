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
package org.emonocot.job.delta;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;

import java.io.IOException;
import java.util.HashMap;
import java.util.Map;
import java.util.UUID;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.batch.core.Job;
import org.springframework.batch.core.JobExecution;
import org.springframework.batch.core.JobParameter;
import org.springframework.batch.core.JobParameters;
import org.springframework.batch.core.JobParametersInvalidException;
import org.springframework.batch.core.StepExecution;
import org.springframework.batch.core.configuration.JobLocator;
import org.springframework.batch.core.launch.JobLauncher;
import org.springframework.batch.core.launch.NoSuchJobException;
import org.springframework.batch.core.repository.JobExecutionAlreadyRunningException;
import org.springframework.batch.core.repository.JobInstanceAlreadyCompleteException;
import org.springframework.batch.core.repository.JobRestartException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.core.io.ClassPathResource;
import org.springframework.test.annotation.DirtiesContext;
import org.springframework.test.annotation.DirtiesContext.ClassMode;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;

@RunWith(SpringJUnit4ClassRunner.class)
@ContextConfiguration({
    "/META-INF/spring/batch/jobs/deltaToDwc.xml",
    "/META-INF/spring/applicationContext-integration.xml",
    "/META-INF/spring/applicationContext-test.xml" })
@DirtiesContext(classMode = ClassMode.AFTER_CLASS)
public class DeltaNaturalLanguageDwcIntegrationTest {
	
	private Logger logger = LoggerFactory.getLogger(DeltaNaturalLanguageDwcIntegrationTest.class);
	
	private ClassPathResource specsFile = new ClassPathResource("org/emonocot/job/delta/tonat");
	
	private ClassPathResource itemsFile = new ClassPathResource("org/emonocot/job/delta/items");
	
	private ClassPathResource taxonFile = new ClassPathResource("org/emonocot/job/delta/taxon.txt");
	
	 @Autowired
	 private JobLocator jobLocator;

	 @Autowired
     @Qualifier("readWriteJobLauncher")
	 private JobLauncher jobLauncher;
	
	@Test
	public void testCreateGenericArchive() throws NoSuchJobException,
			JobExecutionAlreadyRunningException, JobRestartException,
			JobInstanceAlreadyCompleteException, JobParametersInvalidException, IOException {
		 Map<String, JobParameter> parameters =
	            new HashMap<String, JobParameter>();
	    parameters.put("specs.file", new JobParameter(specsFile.getFile().getAbsolutePath()));
	    parameters.put("items.file", new JobParameter(itemsFile.getFile().getAbsolutePath()));
	    parameters.put("taxon.file", new JobParameter(taxonFile.getFile().getAbsolutePath()));
	    parameters.put("fields.terminated.by", new JobParameter(","));
	    parameters.put("fields.enclosed.by", new JobParameter("\""));
	    parameters.put("output.fields",new JobParameter("taxonID,description,language,license"));
	    parameters.put("taxon.file.skip.lines", new JobParameter("1"));
	    parameters.put("taxon.file.field.names", new JobParameter("taxonID,subfamily,subtribe,tribe,accessRights,bibliographicCitation,created,license,modified,references,rights,rightsHolder,acceptedNameUsage,acceptedNameUsageID,class,datasetID,datasetName,family,genus,infraspecificEpithet,kingdom,nameAccordingTo,namePublishedIn,namePublishedInID,namePublishedInYear,nomenclaturalCode,nomenclaturalStatus,order,originalNameUsage,originalNameUsageID,parentNameUsage,parentNameUsageID,phylum,scientificName,scientificNameAuthorship,scientificNameID,specificEpithet,subgenus,taxonRank,taxonRemarks,taxonomicStatus,verbatimTaxonRank"));
	    parameters.put("taxon.file.delimiter", new JobParameter("\t"));
	    parameters.put("taxon.file.quote.character", new JobParameter("\""));
	    parameters.put("description.file.field.names", new JobParameter("taxonID,description,license,language,type,rights"));
	    parameters.put("description.default.values", new JobParameter("license=http://creativecommons.org/licenses/by-nc-sa/3.0,language=EN,type=general,rights=© Copyright The Board of Trustees\\, Royal Botanic Gardens\\, Kew."));
	    parameters.put("archive.file", new JobParameter(UUID.randomUUID().toString()));
	    
	    JobParameters jobParameters = new JobParameters(parameters);

	    Job deltaToDwC = jobLocator.getJob("DeltaToDwC");
	    assertNotNull("DeltaToDwC must not be null",  deltaToDwC);
	    JobExecution jobExecution = jobLauncher.run(deltaToDwC, jobParameters);
	    assertEquals("The job should complete successfully",jobExecution.getExitStatus().getExitCode(),"COMPLETED");
	    for (StepExecution stepExecution : jobExecution.getStepExecutions()) {
	        logger.info(stepExecution.getStepName() + " "
	                    + stepExecution.getReadCount() + " "
	                    + stepExecution.getFilterCount() + " "
	                    + stepExecution.getWriteCount() + " " + stepExecution.getCommitCount());
	   }
		
	}

}
