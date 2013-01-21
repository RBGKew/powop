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

import org.apache.solr.client.solrj.SolrServer;
import org.apache.solr.client.solrj.response.QueryResponse;
import org.apache.solr.common.SolrDocumentList;
import org.apache.solr.common.params.ModifiableSolrParams;
import org.emonocot.model.Taxon;
import org.emonocot.persistence.hibernate.SolrIndexingListener;
import org.hibernate.Session;
import org.hibernate.SessionFactory;
import org.hibernate.Transaction;
import org.junit.After;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.batch.core.ExitStatus;
import org.springframework.batch.core.Job;
import org.springframework.batch.core.JobExecution;
import org.springframework.batch.core.JobParameter;
import org.springframework.batch.core.JobParameters;
import org.springframework.batch.core.configuration.JobLocator;
import org.springframework.batch.core.launch.JobLauncher;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
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
    "/META-INF/spring/batch/jobs/darwinCoreArchiveCreator.xml",
    "/META-INF/spring/applicationContext-integration.xml",
    "/META-INF/spring/applicationContext-test.xml" })
@DirtiesContext(classMode = ClassMode.AFTER_CLASS)
public class DwcaCreationIntegrationTest {
	
	Logger logger = LoggerFactory.getLogger(DwcaCreationIntegrationTest.class);

    @Autowired
    private JobLocator jobLocator;

    @Autowired
	@Qualifier("jobLauncher")
    private JobLauncher jobLauncher;

    @Autowired
    private SessionFactory sessionFactory;
    
    @Autowired SolrServer solrServer;
    
    @Autowired SolrIndexingListener solrIndexingListener;

	/**
	 * @throws java.lang.Exception
	 */
	@Before
	public void setUp() throws Exception {
		ModifiableSolrParams params = new ModifiableSolrParams();
    	params.add("q","*:*");
    	params.add("rows",new Integer(Integer.MAX_VALUE).toString());
    	params.add("df","id");
    	QueryResponse queryResponse = solrServer.query(params);
    	SolrDocumentList solrDocumentList = queryResponse.getResults();
    	List<String> documentsToDelete = new ArrayList<String>();
    	for(int i = 0; i < solrDocumentList.size(); i++) {
    		documentsToDelete.add(solrDocumentList.get(i).getFirstValue("id").toString());
    	}
    	if(!documentsToDelete.isEmpty()) {
    		logger.info("Deleting " + documentsToDelete.size());
    	    solrServer.deleteById(documentsToDelete);
    	    solrServer.commit();
    	}
		
		
        Session session = sessionFactory.openSession();
        
        Transaction tx = session.beginTransaction();

        List<Taxon> taxa = session.createQuery("from Taxon as taxon").list();
        solrIndexingListener.indexObjects(taxa);
        logger.info("Indexing " + taxa.size());
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
	public void testWriteSubsetArchive() throws Exception {
		Map<String, JobParameter> parameters = new HashMap<String, JobParameter>();
		parameters.put("query", new JobParameter(""));
		parameters.put("selected.facets", new JobParameter("taxon.family_s=Araceae,base.class_s=org.emonocot.model.Taxon"));
		parameters.put("extensions", new JobParameter("Description,Distribution,Reference"));
		parameters.put("taxon.columns", new JobParameter("scientificName,scientificNameAuthorship"));
		parameters.put("description.columns", new JobParameter("taxonID,type,description"));
		parameters.put("distribution.columns", new JobParameter("taxonID,locationID"));
		parameters.put("reference.columns", new JobParameter("taxonID,bibliographicCitation"));
		parameters.put("output.file",
		        new JobParameter(File.createTempFile("output", ".zip").getAbsolutePath()));
		
		JobParameters jobParameters = new JobParameters(parameters);
		Job archiveCreatorJob = jobLocator.getJob("DarwinCoreArchiveCreation");
		assertNotNull("archiveCreatorJob must exist", archiveCreatorJob);
		JobExecution jobExecution = jobLauncher.run(archiveCreatorJob,
		        jobParameters);
		
		assertEquals("The Job should be sucessful", ExitStatus.COMPLETED, jobExecution.getExitStatus());
		
		File outputFile = new File(jobParameters.getParameters()
				.get("output.file").getValue().toString());
        ZipInputStream zipStream = new ZipInputStream(new FileInputStream(outputFile));
        assertNotNull("There should be an output file", outputFile);
		System.out.println("Zip file created at " + outputFile.getAbsolutePath());
		
        List<ZipEntry> entries = new ArrayList<ZipEntry>();
        ZipEntry e;
        while((e = zipStream.getNextEntry()) != null){
            entries.add(e);
        }
        assertEquals("There should be 5 files", 5, entries.size());
	}
	
	/**
	 * @throws Exception
	 */
	@Test
	public void testWriteAllArchive() throws Exception {
		Map<String, JobParameter> parameters = new HashMap<String, JobParameter>();
		parameters.put("output.file",
		        new JobParameter(File.createTempFile("output", ".zip").getAbsolutePath()));
		
		parameters.put("query", new JobParameter(""));
		parameters.put("selected.facets", new JobParameter("base.class_s=org.emonocot.model.Taxon"));
		parameters.put("extensions", new JobParameter("Description,Distribution,Reference"));
		parameters.put("taxon.columns", new JobParameter("scientificName,scientificNameAuthorship"));
		parameters.put("description.columns", new JobParameter("taxonID,type,description"));
		parameters.put("distribution.columns", new JobParameter("taxonID,locationID"));
		parameters.put("reference.columns", new JobParameter("taxonID,bibliographicCitation"));
		
		JobParameters jobParameters = new JobParameters(parameters);
		Job archiveCreatorJob = jobLocator.getJob("DarwinCoreArchiveCreation");
		assertNotNull("archiveCreatorJob must exist", archiveCreatorJob);
		JobExecution jobExecution = jobLauncher.run(archiveCreatorJob,
		        jobParameters);
		
		assertEquals("The Job should be sucessful", ExitStatus.COMPLETED, jobExecution.getExitStatus());

        File outputFile = new File(jobParameters.getParameters()
                .get("output.file").getValue().toString());
        ZipInputStream zipStream = new ZipInputStream(new FileInputStream(outputFile));
        assertNotNull("There should be an output file", outputFile);
        System.out.println("Zip file created at " + outputFile.getAbsolutePath());
        
        List<ZipEntry> entries = new ArrayList<ZipEntry>();
        ZipEntry e;
        while((e = zipStream.getNextEntry()) != null){
            entries.add(e);
        }
        assertEquals("There should be 6 files", 6, entries.size());
	}

}
