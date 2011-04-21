package org.emonocot.job.scratchpads;

import static org.junit.Assert.assertNotNull;

import java.io.File;
import java.util.HashMap;
import java.util.Map;

import org.joda.time.DateTime;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.batch.core.Job;
import org.springframework.batch.core.JobParameter;
import org.springframework.batch.core.JobParameters;
import org.springframework.batch.core.configuration.JobLocator;
import org.springframework.batch.core.launch.JobLauncher;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;


@RunWith(SpringJUnit4ClassRunner.class)
@ContextConfiguration({"/applicationContext.xml","/applicationContext-test.xml"})
public class HarvestingJobIntegrationTest {
	
	@Autowired
	JobLocator jobLocator;
	
	@Autowired
	JobLauncher jobLauncher;
	
	@Test
	public void testNotModifiedResponse() throws Exception {
		Map<String,JobParameter> parameters = new HashMap<String,JobParameter>();
		parameters.put("authority.name", new JobParameter("http://scratchpad.cate-araceae.org"));
		parameters.put("authority.uri", new JobParameter("http://129.67.24.160/test/test.xml"));
		parameters.put("authority.last.harvested", new JobParameter(new DateTime(2010, 11, 1, 9, 0, 0, 0).toDate()));
		parameters.put("temporary.file.name", new JobParameter(File.createTempFile("test", ".xml").getAbsolutePath()));
		JobParameters jobParameters = new JobParameters(parameters);
		
		
		Job speciesPageHarvestingJob = jobLocator.getJob("SpeciesPageHarvestingJob");	
		assertNotNull("SpeciesPageHarvestingJob must not be null",speciesPageHarvestingJob);
		jobLauncher.run(speciesPageHarvestingJob, jobParameters);
	}
}
