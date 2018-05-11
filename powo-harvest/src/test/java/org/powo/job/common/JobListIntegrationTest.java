package org.powo.job.common;

import static com.github.tomakehurst.wiremock.client.WireMock.aResponse;
import static com.github.tomakehurst.wiremock.client.WireMock.get;
import static com.github.tomakehurst.wiremock.client.WireMock.stubFor;
import static com.github.tomakehurst.wiremock.client.WireMock.urlEqualTo;
import static org.junit.Assert.assertEquals;

import java.io.File;
import java.util.List;

import org.joda.time.DateTime;
import org.joda.time.DateTimeZone;
import org.joda.time.Period;
import org.junit.After;
import org.junit.Before;
import org.junit.Rule;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.powo.api.JobListService;
import org.powo.factories.JobConfigurationFactory;
import org.powo.model.JobConfiguration;
import org.powo.model.JobList;
import org.powo.model.constants.JobListStatus;
import org.powo.model.registry.Organisation;
import org.powo.model.registry.Resource;
import org.powo.persistence.AbstractPersistenceTest;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.test.annotation.DirtiesContext;
import org.springframework.test.annotation.DirtiesContext.ClassMode;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;

import com.github.tomakehurst.wiremock.junit.WireMockRule;

@RunWith(SpringJUnit4ClassRunner.class)
@ContextConfiguration({"/META-INF/spring/batch/jobs/darwinCoreArchiveHarvesting.xml", "/META-INF/spring/batch/jobs/reindex.xml" })
@DirtiesContext(classMode = ClassMode.AFTER_EACH_TEST_METHOD)
public class JobListIntegrationTest extends AbstractPersistenceTest {

	private final int mockHttpPort = 8088;
	private final String mockHttpUrl = "http://localhost:" + mockHttpPort;
	@Rule public WireMockRule wireMockRule = new WireMockRule(mockHttpPort);

	@Autowired
	private JobListService jobListService;

	private Resource names;
	private Resource taxa;

	@Before
	public void setUp() throws Exception {
		File spoolDirectory = new File("target/spool");
		DateTimeZone.setDefault(DateTimeZone.UTC);

		spoolDirectory.mkdirs();
		spoolDirectory.deleteOnExit();

		stubFor(get(urlEqualTo("/sample-names.zip"))
				.willReturn(aResponse()
						.withStatus(200)
						.withBodyFile("/sample-names.zip")));

		stubFor(get(urlEqualTo("/sample-taxonomy.zip"))
				.willReturn(aResponse()
						.withStatus(200)
						.withBodyFile("/sample-taxonomy.zip")));

		Organisation backbone = createSource("test", "http://test.org", "test");
		names = createResource(backbone, "names", mockHttpUrl + "/sample-names.zip");
		taxa = createResource(backbone, "taxa", mockHttpUrl + "/sample-taxonomy.zip");

		doSetUp();
	}

	@After
	public void tearDown() {
		doTearDown();
	}

	@Test
	public void testScheduling() {
		JobConfiguration j1 = JobConfigurationFactory.harvestNames(names);
		JobConfiguration j2 = JobConfigurationFactory.harvestTaxonomy(taxa);
		jobConfigurationDao.save(j1);
		jobConfigurationDao.save(j2);

		names.setJobConfiguration(j1);
		taxa.setJobConfiguration(j2);

		resourceDao.save(names);
		resourceDao.save(taxa);

		JobList toRun = JobList.builder()
				.jobConfiguration(j1)
				.jobConfiguration(j2)
				.description("Test job list")
				.nextRun(DateTime.now())
				.status(JobListStatus.Completed)
				.build();

		JobList scheduledLater = JobList.builder()
				.jobConfiguration(j2)
				.jobConfiguration(j1)
				.description("Test job list")
				.nextRun(DateTime.now().plusMinutes(1))
				.build();

		JobList alreadyRunning = JobList.builder()
				.jobConfiguration(j2)
				.jobConfiguration(j1)
				.description("Test job list")
				.nextRun(DateTime.now().minusMinutes(1))
				.status(JobListStatus.Running)
				.build();

		jobListDao.save(toRun);
		jobListDao.save(scheduledLater);
		jobListDao.save(alreadyRunning);

		List<JobList> allJobs = jobListDao.list();
		assertEquals(3, allJobs.size());

		// nothing is scheduled yet...
		assertEquals(0, jobListDao.scheduled().size());

		// but one is available to be scheduled
		assertEquals(1, jobListDao.schedulable().size());

		jobListService.scheduleAvailable();

		// now j1 should be scheduled to run
		assertEquals(1, jobListDao.scheduled().size());
		assertEquals(toRun.getId(), jobListDao.scheduled().get(0).getId());
	}

	@Test
	public void testJobSequencing() {
		JobConfiguration j1 = JobConfigurationFactory.harvestNames(names);
		JobConfiguration j2 = JobConfigurationFactory.harvestTaxonomy(taxa);
		JobConfiguration j3 = JobConfigurationFactory.reIndexTaxa();

		jobConfigurationDao.save(j2);
		jobConfigurationDao.save(j1);
		jobConfigurationDao.save(j3);

		JobList toRun = JobList.builder()
				.jobConfiguration(j1)
				.jobConfiguration(j2)
				.jobConfiguration(j3)
				.description("Test job list")
				.nextRun(DateTime.now())
				.build();

		jobListService.save(toRun);

		DateTime start = DateTime.now();

		// Simulate quartz job by running JobRunner every second
		while(new Period(start, DateTime.now()).getMinutes() < 2) {
			if(JobListStatus.Completed.equals(toRun.getStatus())) {
				break;
			}

			jobListService.runAvailable();

			try {
				Thread.sleep(5000);
			} catch (InterruptedException e) {
				e.printStackTrace();
			}

			jobListService.refresh(toRun);
		}

		assertEquals(JobListStatus.Completed, toRun.getStatus());
	}
}
