package org.emonocot.portal.ws.ncbi;

import static org.junit.Assert.assertNotNull;

import org.emonocot.portal.controller.form.NcbiDto;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;

@RunWith(SpringJUnit4ClassRunner.class)
@ContextConfiguration({"classpath:META-INF/spring/applicationContext-integrationTest.xml",
                       "classpath:META-INF/spring/applicationContext-integration.xml"})
public class NcbiServiceIntegrationTest {
	
	private static Logger logger = LoggerFactory.getLogger(NcbiServiceIntegrationTest.class);
	
	@Autowired
	private NcbiService ncbiService;
	
	@Before
	public void setUp() throws Exception {
		if(System.getProperty("http.proxyPort") != null && System.getProperty("http.proxyPort").equals("")) {
      	  System.clearProperty("http.proxyPort");
        }
		if(System.getProperty("http.proxyHost") != null && System.getProperty("http.proxyHost").equals("")) {
	      System.clearProperty("http.proxyHost");
	    }
	}
	
	@Test
	public void testNcbiService() throws Exception {

		NcbiDto ncbiDto = ncbiService.issueRequest("Arum");
		
		assertNotNull("NcbiDto should not be null",ncbiDto);
		logger.info("Nucleotide Entries: " + ncbiDto.getNucleotideEntries());
		logger.info("Protien Entries: " + ncbiDto.getProtienEntries());
		logger.info("Pubmed Entries: " + ncbiDto.getPubMedEntries());
	}

}
