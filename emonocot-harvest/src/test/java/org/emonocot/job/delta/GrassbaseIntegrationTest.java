package org.emonocot.job.delta;

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
    "/META-INF/spring/batch/jobs/grassbase.xml",
    "/META-INF/spring/applicationContext-test.xml" })
@DirtiesContext(classMode = ClassMode.AFTER_CLASS)
public class GrassbaseIntegrationTest {
	
	private Logger logger = LoggerFactory.getLogger(GrassbaseIntegrationTest.class);
	
	private ClassPathResource generaSpecsFile = new ClassPathResource("org/kew/grassbase/TONAT");
	
	private ClassPathResource generaItemsFile = new ClassPathResource("org/kew/grassbase/geitems");
	
	private ClassPathResource generaTaxonFile = new ClassPathResource("org/kew/grassbase/genera.txt");
	
	private ClassPathResource speciesSpecsFile = new ClassPathResource("org/kew/grassbase/TONAT");
	
	private ClassPathResource speciesItemsFile = new ClassPathResource("org/kew/grassbase/ITEMS");
	
	private ClassPathResource speciesTaxonFile = new ClassPathResource("org/kew/grassbase/species.txt");
	
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
	    parameters.put("species.specs.file", new JobParameter(speciesSpecsFile.getFile().getAbsolutePath()));
		parameters.put("species.items.file", new JobParameter(speciesItemsFile.getFile().getAbsolutePath()));
		parameters.put("species.taxon.file", new JobParameter(speciesTaxonFile.getFile().getAbsolutePath()));
	    parameters.put("genera.specs.file", new JobParameter(generaSpecsFile.getFile().getAbsolutePath()));
	    parameters.put("genera.items.file", new JobParameter(generaItemsFile.getFile().getAbsolutePath()));
	    parameters.put("genera.taxon.file", new JobParameter(generaTaxonFile.getFile().getAbsolutePath()));
	    parameters.put("fields.terminated.by", new JobParameter("\t"));
	    parameters.put("fields.enclosed.by", new JobParameter("\""));
	    parameters.put("output.fields",new JobParameter("taxonID,description,language,license"));
	    parameters.put("taxon.file.skip.lines", new JobParameter("0"));
	    parameters.put("taxon.file.field.names", new JobParameter("taxonID,scientificName,scientificNameAuthorship"));
	    parameters.put("description.file.field.names", new JobParameter("taxonID,description,license,language"));
	    parameters.put("description.default.values", new JobParameter("license=http://creativecommons.org/licenses/by-nc-sa/3.0,language=EN"));
	    parameters.put("archive.file", new JobParameter(UUID.randomUUID().toString()));
	    
	    JobParameters jobParameters = new JobParameters(parameters);

	    Job deltaToDwC = jobLocator.getJob("Grassbase");
	    assertNotNull("Grassbase must not be null",  deltaToDwC);
	    JobExecution jobExecution = jobLauncher.run(deltaToDwC, jobParameters);
	    for (StepExecution stepExecution : jobExecution.getStepExecutions()) {
	        logger.info(stepExecution.getStepName() + " "
	                    + stepExecution.getReadCount() + " "
	                    + stepExecution.getFilterCount() + " "
	                    + stepExecution.getWriteCount() + " " + stepExecution.getCommitCount());
	   }
		
	}

}
