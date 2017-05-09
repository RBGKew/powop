/*
 * This is eMonocot, a global online biodiversity information resource.
 *
 * Copyright © 2011–2015 The Board of Trustees of the Royal Botanic Gardens, Kew and The University of Oxford
 *
 * eMonocot is free software: you can redistribute it and/or modify it under the terms of the
 * GNU Affero General Public License as published by the Free Software Foundation, either version 3
 * of the License, or (at your option) any later version.
 *
 * eMonocot is distributed in the hope that it will be useful, but WITHOUT ANY WARRANTY; without even
 * the implied warranty of MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 * GNU Affero General Public License for more details.
 *
 * The complete text of the GNU Affero General Public License is in the source repository as the file
 * ‘COPYING’.  It is also available from <http://www.gnu.org/licenses/>.
 */
package org.emonocot.job.dwc;

import static com.github.tomakehurst.wiremock.client.WireMock.*;
import static org.junit.Assert.assertTrue;

import java.io.File;

import org.emonocot.api.job.JobExecutionException;
import org.emonocot.api.job.JobLaunchRequest;
import org.emonocot.api.job.JobLauncher;
import org.emonocot.factories.JobConfigurationFactory;
import org.emonocot.model.JobConfiguration;
import org.emonocot.model.registry.Organisation;
import org.emonocot.model.registry.Resource;
import org.emonocot.persistence.AbstractPersistenceTest;
import org.emonocot.service.impl.JobConfigurationService;
import org.joda.time.DateTime;
import org.joda.time.Period;
import org.junit.After;
import org.junit.Before;
import org.junit.Rule;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.batch.core.BatchStatus;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.test.annotation.DirtiesContext;
import org.springframework.test.annotation.DirtiesContext.ClassMode;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;

import com.github.tomakehurst.wiremock.junit.WireMockRule;

@RunWith(SpringJUnit4ClassRunner.class)
@ContextConfiguration({"/META-INF/spring/batch/jobs/darwinCoreArchiveHarvesting.xml"})
@DirtiesContext(classMode = ClassMode.AFTER_CLASS)
public class DarwinCoreJobIntegrationTest extends AbstractPersistenceTest {

	private Logger logger = LoggerFactory.getLogger(DarwinCoreJobIntegrationTest.class);

	private final int mockHttpPort = 8088;
	private final String mockHttpUrl = "http://localhost:" + mockHttpPort;
	@Rule public WireMockRule wireMockRule = new WireMockRule(mockHttpPort);

	@Autowired
	private JobLauncher jobLauncher;

	@Autowired
	private JobConfigurationService jobConfigurationService;

	private Organisation backbone;
	private Organisation imgOrg;
	private Organisation descOrg;

	private Resource names;
	private Resource taxa;
	private Resource images;
	private Resource descriptions;

	private static final String[] sampleArchives = {
			"/sample-descriptions.zip",
			"/sample-images.zip",
			"/sample-names.zip",
			"/sample-taxonomy.zip"};

	@Before
	public void setUp() throws Exception {
		File spoolDirectory = new File("target/spool");
		spoolDirectory.mkdirs();
		spoolDirectory.deleteOnExit();

		for(String archive : sampleArchives) {
			stubFor(get(urlEqualTo(archive))
					.willReturn(aResponse()
							.withStatus(200)
							.withBodyFile(archive)));
		}

		backbone = createSource("test", "http://test.org", "test", null);
		imgOrg = createSource("img", "http://test.org", "img", null);
		descOrg = createSource("desc", "http://test.org", "desc", null);

		names = createResource(backbone, "names", mockHttpUrl + "/sample-names.zip");
		taxa = createResource(backbone, "taxa", mockHttpUrl + "/sample-taxonomy.zip");
		images = createResource(imgOrg, "images", mockHttpUrl + "/sample-images.zip");
		descriptions = createResource(descOrg, "descriptions", mockHttpUrl + "/sample-descriptions.zip");

		doSetUp();
	}

	@After
	public void tearDown() throws Exception {
		doTearDown();
	}

	@Test
	public final void testHarvesting() throws JobExecutionException, Exception {
		JobConfiguration namesConf = JobConfigurationFactory.harvestNames(names);
		JobConfiguration taxaConf = JobConfigurationFactory.harvestTaxonomy(taxa);
		JobConfiguration imagesConf = JobConfigurationFactory.harvestImages(images, "");
		JobConfiguration descriptionsConf = JobConfigurationFactory.harvest(descriptions);

		jobConfigurationService.save(namesConf);
		jobConfigurationService.save(taxaConf);
		jobConfigurationService.save(imagesConf);
		jobConfigurationService.save(descriptionsConf);

		// Harvest names first
		jobLauncher.launch(new JobLaunchRequest(namesConf));
		assertTrue("Names didn't harvest successfully", harvestSuccessful(namesConf));

		// ... then taxonomy. It has to be done in order
		jobLauncher.launch(new JobLaunchRequest(taxaConf));
		assertTrue("Taxa didn't harvest successfully", harvestSuccessful(taxaConf));

		jobLauncher.launch(new JobLaunchRequest(imagesConf));
		assertTrue("Images didn't harvest successfully", harvestSuccessful(imagesConf));

		jobLauncher.launch(new JobLaunchRequest(descriptionsConf));
		assertTrue("Descriptions didn't harvest successfully", harvestSuccessful(descriptionsConf));
	}

	private boolean harvestSuccessful(JobConfiguration job) {
		DateTime start = DateTime.now();

		while(new Period(start, DateTime.now()).getSeconds() < 20) {
			jobConfigurationService.refresh(job);
			if(BatchStatus.COMPLETED.equals(job.getJobStatus())) {
				logger.info("Succesfully completed {}", job.getDescription());
				return true;
			}

			try {
				Thread.sleep(1000);
			} catch (InterruptedException e) {
				e.printStackTrace();
			}
		}

		return false;
	}
}
