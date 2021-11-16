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

/**
 * These tests act as a convenient way to run jobs and debug how they work, starting from a fresh database.
 */
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
		resource.setUri("https://storage.googleapis.com/powop-content/test-data/20211022_colfungi_names_1.zip");
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

	@Test
	public void importNamesAndDistributions() throws Exception {
		var org = new Organisation();
		org.setIdentifier("CatalogodeHongosUtilesdeColombia");
		org.setAbbreviation("Kew-Names-and-Taxonomic-Backbone");
		org.setTitle("Kew Backbone");
		org = organisations.save(org);

		var namesResource = new Resource();
		namesResource.setOrganisation(org);
		namesResource.setUri("https://storage.googleapis.com/powop-content/test-data/20211022_colfungi_names_1.zip");
		namesResource.setTitle("ColFungi-Names");
		namesResource.setIdentifier("ColFungi-Names");
		namesResource.setResourceType(ResourceType.DwC_Archive);
		namesResource = resources.save(namesResource);

		var params = new JobParametersBuilder()
			.addJobParameters(jobLauncher.getUniqueJobParameters())
			.addString("authority.name", org.getIdentifier())
			.addString("authority.uri", namesResource.getUri())
			.addString("resource.identifier", namesResource.getIdentifier())
			.addString("skip.indexing", "true")
			.addString("taxon.processing.mode", "IMPORT_NAMES")
			.toJobParameters();
		jobLauncher.launchJob(params);
		
		var distributionsResource = new Resource();
		distributionsResource.setOrganisation(org);
		distributionsResource.setUri("https://storage.googleapis.com/powop-content/test-data/colfungi_distribution_safe_1.zip");
		distributionsResource.setTitle("ColFungi-Distributions");
		distributionsResource.setIdentifier("ColFungi-Distributions");
		distributionsResource.setResourceType(ResourceType.DwC_Archive);
		distributionsResource = resources.save(distributionsResource);

		params = new JobParametersBuilder()
			.addJobParameters(jobLauncher.getUniqueJobParameters())
			.addString("authority.name", org.getIdentifier())
			.addString("authority.uri", distributionsResource.getUri())
			.addString("resource.identifier", distributionsResource.getIdentifier())
			.addString("skip.indexing", "true")
			.toJobParameters();
		jobLauncher.launchJob(params);
	}

	@Test
	public void importNamesAndImages() throws Exception {
		var org = new Organisation();
		org.setIdentifier("CatalogodeHongosUtilesdeColombia");
		org.setAbbreviation("Kew-Names-and-Taxonomic-Backbone");
		org.setTitle("Kew Backbone");
		org = organisations.save(org);

		var namesResource = new Resource();
		namesResource.setOrganisation(org);
		namesResource.setUri("https://storage.googleapis.com/powop-content/test-data/20211022_colfungi_names_1.zip");
		namesResource.setTitle("ColFungi-Names");
		namesResource.setIdentifier("ColFungi-Names");
		namesResource.setResourceType(ResourceType.DwC_Archive);
		namesResource = resources.save(namesResource);

		var params = new JobParametersBuilder()
			.addJobParameters(jobLauncher.getUniqueJobParameters())
			.addString("authority.name", org.getIdentifier())
			.addString("authority.uri", namesResource.getUri())
			.addString("resource.identifier", namesResource.getIdentifier())
			.addString("skip.indexing", "true")
			.addString("taxon.processing.mode", "IMPORT_NAMES")
			.toJobParameters();
		jobLauncher.launchJob(params);
		
		var imagesResource = new Resource();
		imagesResource.setOrganisation(org);
		imagesResource.setUri("https://storage.googleapis.com/powop-content/test-data/colfungi_images_safe_1.zip");
		imagesResource.setTitle("ColFungi-Images");
		imagesResource.setIdentifier("ColFungi-Images");
		imagesResource.setResourceType(ResourceType.DwC_Archive);
		imagesResource = resources.save(imagesResource);

		params = new JobParametersBuilder()
			.addJobParameters(jobLauncher.getUniqueJobParameters())
			.addString("authority.name", org.getIdentifier())
			.addString("authority.uri", imagesResource.getUri())
			.addString("resource.identifier", imagesResource.getIdentifier())
			.addString("skip.indexing", "true")
			.toJobParameters();
		jobLauncher.launchJob(params);
	}
}
