package org.emonocot.job.dwc;

import org.emonocot.factories.JobConfigurationFactory;
import org.emonocot.model.registry.Organisation;
import org.emonocot.model.registry.Resource;
import org.emonocot.persistence.AbstractPersistenceTest;
import org.junit.After;
import org.junit.Before;
import org.junit.Test;

public class JobConfigurationIntegrationTest extends AbstractPersistenceTest {

	private Resource testResource;

	@Before
	public void setUp() throws Exception {
		Organisation org = createSource("test", "http://test.org", "test", null);
		testResource = createResource(org, "names", "/sample-names.zip");
		doSetUp();
	}

	@After
	public void tearDown() {
		doTearDown();
	}

	@Test
	public void testJobConfigurationSave() {
		// Make sure job configurations persist without exceptions
		jobConfigurationDao.save(JobConfigurationFactory.harvest(testResource));
		jobConfigurationDao.save(JobConfigurationFactory.harvestNames(testResource));
		jobConfigurationDao.save(JobConfigurationFactory.harvestTaxonomy(testResource));
		jobConfigurationDao.save(JobConfigurationFactory.harvestImages(testResource, ""));
		jobConfigurationDao.save(JobConfigurationFactory.reIndexTaxa());
	}
}