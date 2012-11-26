/**
 * 
 */
package org.emonocot.job.dwc;

import static org.junit.Assert.*;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileReader;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.zip.ZipEntry;
import java.util.zip.ZipInputStream;

import org.emonocot.model.Taxon;
import org.emonocot.persistence.hibernate.SolrIndexingListener;
import org.gbif.dwc.record.Record;
import org.gbif.dwc.terms.ConceptTerm;
import org.gbif.dwc.terms.DcTerm;
import org.gbif.dwc.terms.DwcTerm;
import org.gbif.dwc.terms.GbifTerm;
import org.gbif.dwc.terms.TermFactory;

//import org.gbif.dwc.text.ArchiveWriter; //from dwca-reader v1.7.6
import org.gbif.dwc.text.Archive;
import org.gbif.dwc.text.ArchiveField;
import org.gbif.dwc.text.ArchiveFile;
import org.gbif.dwc.text.DwcaWriter;
import org.gbif.dwc.text.StarRecord;
import org.gbif.metadata.eml.EmlFactory;
import org.gbif.utils.file.FileUtils;
import org.hibernate.Session;
import org.hibernate.SessionFactory;
import org.hibernate.Transaction;
import org.joda.time.DateTime;
import org.joda.time.base.BaseDateTime;
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
import org.springframework.core.io.ClassPathResource;
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
	public void testWriteSubsetArchive() throws Exception {
		Map<String, JobParameter> parameters = new HashMap<String, JobParameter>();
		parameters.put("query", new JobParameter("Orchidaceae"));
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
        assertEquals("There should be 4 files", 4, entries.size());
	}
	
	/**
	 * @throws Exception
	 */
	@Test
	public void testWriteAllArchive() throws Exception {
		Map<String, JobParameter> parameters = new HashMap<String, JobParameter>();
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
        assertEquals("There should be 4 files", 4, entries.size());
	}

}
