package org.emonocot.job.delta;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;

import java.io.IOException;
import java.util.HashMap;
import java.util.Map;
 
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
    "/org/emonocot/job/grassbase/grassbase.xml",
    "/META-INF/spring/applicationContext-integration.xml",
    "/META-INF/spring/applicationContext-test.xml" })
@DirtiesContext(classMode = ClassMode.AFTER_CLASS)
public class GrassbaseIntegrationTest {
	
	private Logger logger = LoggerFactory.getLogger(GrassbaseIntegrationTest.class);
	
	private ClassPathResource generaSpecsFile = new ClassPathResource("org/kew/grassbase/GTONATH");
	
	private ClassPathResource generaItemsFile = new ClassPathResource("org/kew/grassbase/geitems");
	
	private ClassPathResource generaTaxonFile = new ClassPathResource("org/kew/grassbase/genera.txt");
	
	private ClassPathResource speciesSpecsFile = new ClassPathResource("org/kew/grassbase/TONAT");
	
	private ClassPathResource speciesItemsFile = new ClassPathResource("org/kew/grassbase/ITEMS");
	
	private ClassPathResource speciesTaxonFile = new ClassPathResource("org/kew/grassbase/species.txt");
	
	private ClassPathResource imagesFile = new ClassPathResource("org/kew/grassbase/images.txt");
	
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
	    parameters.put("images.file", new JobParameter(imagesFile.getFile().getAbsolutePath()));

	    JobParameters jobParameters = new JobParameters(parameters);

	    Job deltaToDwC = jobLocator.getJob("Grassbase");
	    assertNotNull("Grassbase must not be null",  deltaToDwC);
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
