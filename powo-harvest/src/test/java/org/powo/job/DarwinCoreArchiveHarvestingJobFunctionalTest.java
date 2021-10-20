package org.powo.job;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.powo.model.constants.ResourceType;
import org.powo.model.registry.Organisation;
import org.powo.model.registry.Resource;
import org.powo.persistence.dao.OrganisationDao;
import org.powo.persistence.dao.ResourceDao;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.batch.core.JobParametersBuilder;
import org.springframework.batch.test.JobLauncherTestUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;

@RunWith(SpringJUnit4ClassRunner.class)
@ContextConfiguration({ 
	"/META-INF/spring/applicationContext-batch-test.xml",
	"/META-INF/spring/batch/jobs/darwinCoreArchiveHarvesting.xml" 
})
public class DarwinCoreArchiveHarvestingJobFunctionalTest extends AbstractDatabaseTest {
	private static final Logger log = LoggerFactory.getLogger(DarwinCoreArchiveHarvestingJobFunctionalTest.class);

	@Autowired
	private JobLauncherTestUtils jobLauncher;

	@Autowired
	private OrganisationDao organisations;

	@Autowired
	private ResourceDao resources;

	@Test
	public void importNames() throws Exception {
		var org = new Organisation();
		org.setIdentifier("Kew-Names-and-Taxonomic-Backbone");
		org.setAbbreviation("Kew-Names-and-Taxonomic-Backbone");
		org.setTitle("Kew Backbone");
		org = organisations.save(org);

		var resource = new Resource();
		resource.setOrganisation(org);
		resource.setUri("http://storage.googleapis.com/powop-content/backbone/if_species_name.zip");
		resource.setTitle("IF-Species-Names");
		resource.setIdentifier("IF-Species-Names");
		resource.setResourceType(ResourceType.DwC_Archive);
		resource = resources.save(resource);
		
		var params = new JobParametersBuilder()
			.addJobParameters(jobLauncher.getUniqueJobParameters())
			.addString("authority.name", org.getIdentifier())
			.addString("authority.uri", resource.getUri())
			.addString("resource.identifier", resource.getIdentifier())
			.addString("skip.indexing", "true")
			.addString("taxon.processing.mode", "IMPORT_NAMES")
			.toJobParameters();
		jobLauncher.launchJob(params);
	}

	
	@Test
	public void importNamesAndTaxonomy() throws Exception {
		var org = new Organisation();
		org.setIdentifier("CatalogodeHongosUtilesdeColombia");
		org.setAbbreviation("Kew-Names-and-Taxonomic-Backbone");
		org.setTitle("Kew Backbone");
		org = organisations.save(org);

		var resource = new Resource();
		resource.setOrganisation(org);
		resource.setUri("https://storage.googleapis.com/powop-content/test-data/20211008_colfungi_names.zip");
		resource.setTitle("ColFungi-Taxonomy");
		resource.setIdentifier("ColFungi-Taxonomy");
		resource.setResourceType(ResourceType.DwC_Archive);
		resource = resources.save(resource);
		
		var params = new JobParametersBuilder()
			.addJobParameters(jobLauncher.getUniqueJobParameters())
			.addString("authority.name", org.getIdentifier())
			.addString("authority.uri", resource.getUri())
			.addString("resource.identifier", resource.getIdentifier())
			.addString("skip.indexing", "true")
			.addString("taxon.processing.mode", "IMPORT_NAMES")
			.toJobParameters();
		jobLauncher.launchJob(params);
		
		params = new JobParametersBuilder()
			.addJobParameters(jobLauncher.getUniqueJobParameters())
			.addString("authority.name", org.getIdentifier())
			.addString("authority.uri", resource.getUri())
			.addString("resource.identifier", resource.getIdentifier())
			.addString("skip.indexing", "true")
			.addString("taxon.processing.mode", "IMPORT_TAXONOMY")
			.toJobParameters();
		jobLauncher.launchJob(params);
	}
}
