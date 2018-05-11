package org.powo.harvest.controller;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.content;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

import org.joda.time.DateTime;
import org.joda.time.DateTimeZone;
import org.joda.time.format.DateTimeFormatter;
import org.junit.After;
import org.junit.Before;
import org.junit.Ignore;
import org.junit.Test;
import org.powo.factories.JobConfigurationFactory;
import org.powo.model.JobConfiguration;
import org.powo.model.JobList;
import org.powo.model.constants.SchedulingPeriod;
import org.powo.model.marshall.json.JobSchedule;
import org.powo.model.registry.Organisation;
import org.powo.model.registry.Resource;
import org.springframework.http.MediaType;

public class JobControllerIntegrationTest extends AbstractControllerTest {

	private Organisation org;
	private Resource resource1;
	private Resource resource2;

	private JobConfiguration names;
	private JobConfiguration taxa;
	private JobConfiguration reindex;

	@Before
	public void setup() throws Exception {
		org = createSource("test", "http://test.org", "Test");
		resource1 = createResource(org, "blarg", "http://blarg.org/blerg.zip");
		resource2 = createResource(org, "blarg", "http://blarg.org/blerg.zip");

		doSetUp();

		names = JobConfigurationFactory.harvestNames(resource1);
		taxa = JobConfigurationFactory.harvestTaxonomy(resource2);
		reindex = JobConfigurationFactory.reIndexTaxa();

		jobConfigurationDao.save(names);
		jobConfigurationDao.save(taxa);
		jobConfigurationDao.save(reindex);

		resource1.setJobConfiguration(names);
		resource2.setJobConfiguration(taxa);

		resourceDao.save(resource1);
		resourceDao.save(resource2);
	}

	@After
	public void teardown() {
		doTearDown();
	}

	@Test
	public void listJobs() throws Exception {
		mvc.perform(get("/api/1/job/configuration").accept(MediaType.APPLICATION_JSON_UTF8))
		.andExpect(status().isOk())
		.andExpect(content().contentType(MediaType.APPLICATION_JSON_UTF8))
		.andExpect(jsonPath("$.totalResults").value(3));
	}

	@Test
	public void runJob() throws Exception {
		mvc.perform(post("/api/1/job/configuration/" + reindex.getId() + "/run"))
		.andExpect(status().isOk())
		.andExpect(jsonPath("$.jobStatus").value("STARTING"));
	}

	@Test
	public void createList() throws Exception {
		// It's complicated to serialize a JobList object in a way that the api expects
		// (array of job configuration ids), so just make a json string manually
		String request = "{\"description\": \"Test job list\"," +
				"\"jobConfigurations\": [" + names.getId() + "," + taxa.getId() + "]}";
		mvc.perform(post("/api/1/job/list")
				.contentType(MediaType.APPLICATION_JSON_UTF8)
				.content(request))
		.andExpect(status().isCreated());
	}

	@Test
	public void listLists() throws Exception {
		JobList list1 = JobList.builder()
				.description("Test job list")
				.jobConfiguration(names)
				.jobConfiguration(taxa)
				.jobConfiguration(reindex)
				.build();
		JobList list2 = JobList.builder()
				.description("Test job list 2")
				.jobConfiguration(taxa)
				.jobConfiguration(names)
				.jobConfiguration(reindex)
				.build();

		jobListDao.save(list1);
		jobListDao.save(list2);

		mvc.perform(get("/api/1/job/list"))
		.andExpect(status().isOk())
		.andExpect(jsonPath("$.totalResults").value(2));
	}

	@Test
	public void showList() throws Exception {
		JobList list = JobList.builder()
				.description("Test job list")
				.jobConfiguration(names)
				.jobConfiguration(taxa)
				.jobConfiguration(reindex)
				.build();
		jobListDao.save(list);

		mvc.perform(get("/api/1/job/list/" + list.getId()))
		.andExpect(status().isOk())
		.andExpect(jsonPath("$.description").value("Test job list"))
		.andExpect(jsonPath("$.currentJob").value(0))
		.andExpect(jsonPath("$.jobConfigurations[0].id").value(names.getId()))
		.andExpect(jsonPath("$.jobConfigurations[0].description").value(names.getDescription()))
		.andExpect(jsonPath("$.jobConfigurations[1].id").value(taxa.getId()))
		.andExpect(jsonPath("$.jobConfigurations[1].description").value(taxa.getDescription()))
		.andExpect(jsonPath("$.jobConfigurations[2].id").value(reindex.getId()))
		.andExpect(jsonPath("$.jobConfigurations[2].description").value(reindex.getDescription()));
	}

	@Test
	public void scheduleList() throws Exception {
		JobList list = JobList.builder()
				.description("Test job list")
				.jobConfiguration(names)
				.jobConfiguration(taxa)
				.jobConfiguration(reindex)
				.build();
		jobListDao.save(list);

		DateTime runAt = DateTime.now();
		JobSchedule schedule = JobSchedule.builder()
				.nextRun(runAt)
				.schedulingPeriod(SchedulingPeriod.MONTHLY)
				.build();

		mvc.perform(post("/api/1/job/list/" + list.getId() + "/schedule")
				.contentType(MediaType.APPLICATION_JSON)
				.content(objectToJsonString(schedule)))
		.andExpect(status().isOk())
		.andExpect(jsonPath("$.schedulingPeriod").value("MONTHLY"))
		.andExpect(jsonPath("$.nextRun").value(runAt.toString()));
	}
}
