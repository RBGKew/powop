package org.powo.job.dwc;

import org.junit.After;
import org.junit.Before;
import org.junit.Test;
import org.powo.factories.JobConfigurationFactory;
import org.powo.model.registry.Organisation;
import org.powo.model.registry.Resource;
import org.powo.persistence.AbstractPersistenceTest;

public class JobConfigurationIntegrationTest extends AbstractPersistenceTest {

	private Resource testResource;

	@Before
	public void setUp() throws Exception {
		Organisation org = createSource("test", "http://test.org", "test");
		testResource = createResource(org, "names", "http://localhost/sample-names.zip");
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