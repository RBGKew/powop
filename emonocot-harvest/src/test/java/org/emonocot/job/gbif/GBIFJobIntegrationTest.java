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
package org.emonocot.job.gbif;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;

import java.io.File;
import java.io.IOException;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Properties;

import org.emonocot.model.Taxon;
import org.emonocot.persistence.hibernate.SolrIndexingListener;
import org.hibernate.Session;
import org.hibernate.SessionFactory;
import org.hibernate.Transaction;
import org.joda.time.DateTime;
import org.joda.time.base.BaseDateTime;
import org.junit.Before;
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
import org.springframework.core.io.Resource;
import org.springframework.test.annotation.DirtiesContext;
import org.springframework.test.annotation.DirtiesContext.ClassMode;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;

/**
 *
 * @author ben
 *
 */
@RunWith(SpringJUnit4ClassRunner.class)
@ContextConfiguration({
    "/META-INF/spring/batch/jobs/gbifImport.xml",
    "/META-INF/spring/applicationContext-integration.xml",
    "/META-INF/spring/applicationContext-test.xml" })
@DirtiesContext(
		classMode = ClassMode.AFTER_CLASS)
public class GBIFJobIntegrationTest {

    private Logger logger = LoggerFactory.getLogger(GBIFJobIntegrationTest.class);

    private Resource inputFile = new ClassPathResource("/org/emonocot/job/gbif/list.xml");

    @Autowired
    private JobLocator jobLocator;

    @Autowired
	@Qualifier("readWriteJobLauncher")
    private JobLauncher jobLauncher;
    
    @Autowired
    private SessionFactory sessionFactory;
    
    @Autowired
    private SolrIndexingListener solrIndexingListener;
    
    private Properties properties;

    /**
     * 1288569600 in unix time.
     */
    private static final BaseDateTime PAST_DATETIME
    = new DateTime(2010, 11, 1, 9, 0, 0, 0);
    
    @Before
    public void setUp() throws Exception {
    	Resource propertiesFile = new ClassPathResource("META-INF/spring/application.properties");
    	properties = new Properties();
    	properties.load(propertiesFile.getInputStream());
    	File spoolDirectory = new File("./target/spool");
        spoolDirectory.mkdirs();
        spoolDirectory.deleteOnExit();
    }

    /**
     *
     * @throws IOException
     *             if a temporary file cannot be created.
     * @throws NoSuchJobException
     *             if SpeciesPageHarvestingJob cannot be located
     * @throws JobParametersInvalidException
     *             if the job parameters are invalid
     * @throws JobInstanceAlreadyCompleteException
     *             if the job has already completed
     * @throws JobRestartException
     *             if the job cannot be restarted
     * @throws JobExecutionAlreadyRunningException
     *             if the job is already running
     */
    @Test
    public final void testNotModifiedResponse() throws IOException,
            NoSuchJobException, JobExecutionAlreadyRunningException,
            JobRestartException, JobInstanceAlreadyCompleteException,
            JobParametersInvalidException {
    	Session session = sessionFactory.openSession();        
        Transaction tx = session.beginTransaction();

        List<Taxon> taxa = session.createQuery("from Taxon as taxon").list();
        solrIndexingListener.indexObjects(taxa);
        tx.commit();
        
        Map<String, JobParameter> parameters =
            new HashMap<String, JobParameter>();
        parameters.put("family", new JobParameter("Araceae"));
        parameters.put("authority.name", new JobParameter("test"));
        String repository = properties.getProperty("test.resource.baseUrl",
                "http://build.e-monocot.org/git/?p=emonocot.git;a=blob_plain;f=emonocot-harvest/src/test/resources/org/emonocot/job/common/");
        parameters.put("authority.uri", new JobParameter(repository + "list.xml"));
        
        //parameters.put("authority.uri", new JobParameter("http://data.gbif.org/ws/rest/occurrence/list?taxonconceptkey=6979&maxresults=1000&typesonly=true&format=darwin&mode=raw&startindex="));
        parameters.put("authority.last.harvested",
        new JobParameter(Long.toString((GBIFJobIntegrationTest.PAST_DATETIME.getMillis()))));

       
        JobParameters jobParameters = new JobParameters(parameters);

        Job job = jobLocator.getJob("GBIFImport");
        assertNotNull("GBIFImport must not be null", job);
        JobExecution jobExecution = jobLauncher.run(job, jobParameters);
        assertEquals("The job should complete successfully",jobExecution.getExitStatus().getExitCode(),"COMPLETED");
        for (StepExecution stepExecution : jobExecution.getStepExecutions()) {
            logger.info(stepExecution.getStepName() + " "
                    + stepExecution.getReadCount() + " "
                    + stepExecution.getFilterCount() + " "
                    + stepExecution.getWriteCount() + " " + stepExecution.getCommitCount());
        }
    }
}
