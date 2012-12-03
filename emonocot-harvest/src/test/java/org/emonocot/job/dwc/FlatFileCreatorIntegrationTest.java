/**
 * 
 */
package org.emonocot.job.dwc;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;

import java.io.File;
import java.io.FileInputStream;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.zip.ZipEntry;
import java.util.zip.ZipInputStream;

import org.emonocot.model.Taxon;
import org.emonocot.persistence.hibernate.SolrIndexingListener;
import org.hibernate.Session;
import org.hibernate.SessionFactory;
import org.hibernate.Transaction;
import org.junit.After;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.batch.core.ExitStatus;
import org.springframework.batch.core.Job;
import org.springframework.batch.core.JobExecution;
import org.springframework.batch.core.JobParameter;
import org.springframework.batch.core.JobParameters;
import org.springframework.batch.core.configuration.JobLocator;
import org.springframework.batch.core.launch.JobLauncher;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.test.annotation.DirtiesContext;
import org.springframework.test.annotation.DirtiesContext.ClassMode;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;

/**
 * @author jk00kg
 * 
 */
@RunWith(SpringJUnit4ClassRunner.class)
@ContextConfiguration({
    "/META-INF/spring/batch/jobs/flatFileCreator.xml",
    "/META-INF/spring/applicationContext-integration.xml",
    "/META-INF/spring/applicationContext-test.xml" })
@DirtiesContext(classMode = ClassMode.AFTER_CLASS)
public class FlatFileCreatorIntegrationTest {

    /**
     *
     */
    @Autowired
    private JobLocator jobLocator;

    /**
     *
     */
    @Autowired
    private JobLauncher jobLauncher;

    /**
     * 
     */
    @Autowired
    private SessionFactory sessionFactory;
    
    @Autowired SolrIndexingListener solrIndexingListener;

	/**
	 * @throws java.lang.Exception
	 */
	@Before
	public void setUp() throws Exception {
        Session session = sessionFactory.openSession();
        
        Transaction tx = session.beginTransaction();

        List<Taxon> taxa = session.createQuery("from Taxon as taxon").list();
        solrIndexingListener.indexObjects(taxa);
        tx.commit();
	}

	/**
	 * @throws java.lang.Exception
	 */
	@After
	public void tearDown() throws Exception {
	}

	/**
	 * @throws Exception
	 */
	@Test
	public void testWriteTaxonFile() throws Exception {
		Map<String, JobParameter> parameters = new HashMap<String, JobParameter>();
		parameters.put("query", new JobParameter(""));
		parameters.put("selected.facets", new JobParameter("taxon.family_s=Araceae"));
		parameters.put("extension", new JobParameter("Taxon"));
		parameters.put("columns", new JobParameter("taxonID,scientificName,scientificNameAuthorship,taxonRank"));
		parameters.put("output.file", new JobParameter(File.createTempFile("output", ".txt").getAbsolutePath()));
		System.out.println(parameters.get("output.file"));
		JobParameters jobParameters = new JobParameters(parameters);
		Job archiveCreatorJob = jobLocator.getJob("FlatFileCreation");
		assertNotNull("flatFileCreatorJob must exist", archiveCreatorJob);
		JobExecution jobExecution = jobLauncher.run(archiveCreatorJob,
		        jobParameters);
		
		assertEquals("The Job should be sucessful", ExitStatus.COMPLETED, jobExecution.getExitStatus());        
	}
	
	/**
	 * @throws Exception
	 */
	@Test
	public void testWriteDistributionFile() throws Exception {
		Map<String, JobParameter> parameters = new HashMap<String, JobParameter>();
		parameters.put("output.file", new JobParameter(File.createTempFile("output", ".txt").getAbsolutePath()));
		System.out.println(parameters.get("output.file"));
		parameters.put("query", new JobParameter(""));
		parameters.put("extension", new JobParameter("Distribution"));		
		parameters.put("columns", new JobParameter("taxonID,locationID"));
		
		JobParameters jobParameters = new JobParameters(parameters);
		Job archiveCreatorJob = jobLocator.getJob("FlatFileCreation");
		assertNotNull("flatFileJob must exist", archiveCreatorJob);
		JobExecution jobExecution = jobLauncher.run(archiveCreatorJob,
		        jobParameters);
		
		assertEquals("The Job should be sucessful", ExitStatus.COMPLETED, jobExecution.getExitStatus());        
	}

	/**
	 * @throws Exception
	 */
	@Test
	public void testWriteImageFile() throws Exception {
		Map<String, JobParameter> parameters = new HashMap<String, JobParameter>();
		parameters.put("output.file", new JobParameter(File.createTempFile("output", ".txt").getAbsolutePath()));
		System.out.println(parameters.get("output.file"));
		parameters.put("query", new JobParameter(""));
		parameters.put("extension", new JobParameter("Image"));		
		parameters.put("columns", new JobParameter("taxonID,identifier"));
		
		JobParameters jobParameters = new JobParameters(parameters);
		Job archiveCreatorJob = jobLocator.getJob("FlatFileCreation");
		assertNotNull("flatFileJob must exist", archiveCreatorJob);
		JobExecution jobExecution = jobLauncher.run(archiveCreatorJob,
		        jobParameters);
		
		assertEquals("The Job should be sucessful", ExitStatus.COMPLETED, jobExecution.getExitStatus());        
	}
}
