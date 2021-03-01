package org.powo.harvest.controller;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.content;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

import org.junit.After;
import org.junit.Before;
import org.junit.Test;
import org.powo.factories.JobConfigurationFactory;
import org.powo.model.JobConfiguration;
import org.powo.model.constants.ResourceType;
import org.powo.model.registry.Organisation;
import org.powo.model.registry.Resource;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.MediaType;

public class ResourceControllerIntegrationTest extends AbstractControllerTest {

	Logger log = LoggerFactory.getLogger(ResourceControllerIntegrationTest.class);

	private Resource resource;

	private Organisation org;

	@Before
	public void setUp() throws Exception {
		org = createSource("test", "http://test.org", "Test");
		resource = createResource(org, "blarg1", "http://blarg.com/1");
		createResource(org, "blarg2", "http://blarg.com");
		createResource(org, "blargedy", "http://blargedy.com");

		doSetUp();
	}

	@After
	public void tearDown() {
		doTearDown();
	}

	@Test
	public void list() throws Exception {
		mvc.perform(get("/api/1/resource").accept(MediaType.APPLICATION_JSON_UTF8))
		.andExpect(status().isOk())
		.andExpect(content().contentType(MediaType.APPLICATION_JSON_UTF8))
		.andExpect(jsonPath("$.totalResults").value(3));
	}

	@Test
	public void getResource() throws Exception {
		mvc.perform(get("/api/1/resource/" + resource.getId()).accept(MediaType.APPLICATION_JSON_UTF8))
		.andExpect(status().isOk())
		.andExpect(jsonPath("$.identifier").value(resource.getIdentifier()))
		.andExpect(jsonPath("$.resourceType").value(resource.getResourceType().toString()))
		.andExpect(jsonPath("$.uri").value(resource.getUri()))
		.andExpect(jsonPath("$.title").value(resource.getTitle()))
		.andExpect(jsonPath("$.organisation").value(org.getIdentifier()));
	}

	@Test
	public void getResourceWithJob() throws Exception {
		JobConfiguration jc = JobConfigurationFactory.harvest(resource);
		jobConfigurationDao.save(jc);

		resource.setJobConfiguration(jc);
		resourceDao.saveOrUpdate(resource);

		mvc.perform(get("/api/1/resource/" + resource.getId()).accept(MediaType.APPLICATION_JSON_UTF8))
		.andExpect(status().isOk())
		.andExpect(jsonPath("$.jobConfiguration.jobName").value("DarwinCoreArchiveHarvesting"))
		.andExpect(jsonPath("$.jobConfiguration.description").value("Harvest " + resource.getIdentifier()))
		.andExpect(jsonPath("$.jobConfiguration.parameters['authority.name']").value(org.getIdentifier()))
		.andExpect(jsonPath("$.jobConfiguration.parameters['authority.uri']").value(resource.getUri()))
		.andExpect(jsonPath("$.jobConfiguration.parameters['resource.id']").value(resource.getId()));
	}

	@Test
	public void missingResource() throws Exception {
		mvc.perform(get("/api/1/resource/1337").accept(MediaType.APPLICATION_JSON_UTF8))
		.andExpect(status().isNotFound())
		.andExpect(jsonPath("$.error").value("NotFound"))
		.andExpect(jsonPath("$.message").value("Resource[1337] not found"));
	}

	@Test
	public void createResource() throws Exception {
		Resource resource = Resource.builder()
				.identifier("creation-test")
				.resourceType(ResourceType.DwC_Archive)
				.uri("http://blargedy.org/test.zip")
				.title("Creation Test")
				.organisation(org)
				.build();

		mvc.perform(post("/api/1/resource")
				.contentType(MediaType.APPLICATION_JSON_UTF8)
				.content(objectToJsonString(resource)))
		.andExpect(status().isCreated())
		.andExpect(jsonPath("$.uri").value(resource.getUri()))
		.andExpect(jsonPath("$.jobConfiguration.parameters['authority.uri']").value(resource.getUri()));
	}

	@Test
	public void invalidUri() throws Exception {
		Resource resource = Resource.builder()
				.identifier("creation-test")
				.resourceType(ResourceType.DwC_Archive)
				.title("Creation Test")
				.organisation(org)
				.build();

		// No URI
		mvc.perform(post("/api/1/resource")
				.contentType(MediaType.APPLICATION_JSON_UTF8)
				.content(objectToJsonString(resource)))
		.andExpect(status().isBadRequest())
		.andExpect(jsonPath("$.validationErrors['resource.uri']").value("may not be empty"));

		// missing organisation identifier
		resource.setUri("http://blarg.org");
		resource.setOrganisation(null);

		mvc.perform(post("/api/1/resource")
				.contentType(MediaType.APPLICATION_JSON_UTF8)
				.content(objectToJsonString(resource)))
		.andExpect(status().isBadRequest())
		.andExpect(jsonPath("$.validationErrors['resource.organisation']").value("may not be null"));

		// Unknown organisation identifier
		Organisation missing = createSource("missing", "http://missing.org", "Missing title");
		resource.setOrganisation(missing);
		mvc.perform(post("/api/1/resource")
				.contentType(MediaType.APPLICATION_JSON_UTF8)
				.content(objectToJsonString(resource)))
		.andExpect(status().isBadRequest())
		.andExpect(jsonPath("$.validationErrors['resource.organisation']").value("Organisation[" + missing.getIdentifier() + "] not found"));
	}

	@Test
	public void updatingResourceUpdatesAssociatedJobConfiguration() throws Exception {
		JobConfiguration jc = JobConfigurationFactory.harvest(resource);
		jobConfigurationDao.save(jc);

		resource.setJobConfiguration(jc);
		resourceDao.saveOrUpdate(resource);

		mvc.perform(get("/api/1/resource/" + resource.getId()).accept(MediaType.APPLICATION_JSON_UTF8))
		.andExpect(status().isOk())
		.andExpect(jsonPath("$.jobConfiguration.parameters['authority.uri']").value(resource.getUri()));

		resource.setUri("http://foogleboogle.org/blargedy.zip");

		mvc.perform(post("/api/1/resource/" + resource.getId())
				.contentType(MediaType.APPLICATION_JSON_UTF8)
				.content(objectToJsonString(resource)))
		.andExpect(status().isOk())
		.andExpect(jsonPath("$.uri").value(resource.getUri()))
		.andExpect(jsonPath("$.jobConfiguration.id").value(jc.getId()))
		.andExpect(jsonPath("$.jobConfiguration.parameters['authority.uri']").value(resource.getUri()));
	}
}
