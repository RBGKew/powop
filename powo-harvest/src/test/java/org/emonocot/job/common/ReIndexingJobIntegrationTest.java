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
package org.emonocot.job.common;

import org.emonocot.api.job.JobExecutionException;
import org.emonocot.api.job.JobLaunchRequest;
import org.emonocot.api.job.JobLauncher;
import org.emonocot.factories.JobConfigurationFactory;
import org.emonocot.model.JobConfiguration;
import org.emonocot.model.constants.Location;
import org.emonocot.model.registry.Organisation;
import org.emonocot.persistence.AbstractPersistenceTest;
import org.emonocot.api.JobConfigurationService;
import org.gbif.ecat.voc.Rank;
import org.gbif.ecat.voc.TaxonomicStatus;
import org.junit.After;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.test.annotation.DirtiesContext;
import org.springframework.test.annotation.DirtiesContext.ClassMode;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;

@RunWith(SpringJUnit4ClassRunner.class)
@ContextConfiguration({"/META-INF/spring/batch/jobs/reindex.xml"})
@DirtiesContext(classMode = ClassMode.AFTER_CLASS)
public class ReIndexingJobIntegrationTest extends AbstractPersistenceTest {

	@Autowired
	private JobLauncher jobLauncher;

	@Autowired
	private JobConfigurationService service;
	@Before
	public void setUp() throws Exception {
		Organisation source = createSource("test", "http://test.org", "test");
		createTaxon("Quercus", "1", null, null, "Fagaceae", "Quercus", null, null, Rank.GENUS, TaxonomicStatus.Accepted, source, new Location[] {}, null);
		createTaxon("Quercus alba", "2", null, null, "Fagaceae", "Quercus", null, null, Rank.GENUS, TaxonomicStatus.Accepted, source, new Location[] {}, null);
		doSetUp();
	}

	@After
	public void tearDown() {
		doTearDown();
	}

	@Test
	public final void testNotModifiedResponse() throws Exception, JobExecutionException {
		JobConfiguration conf = JobConfigurationFactory.reIndexTaxa();
		service.save(conf);
		jobLauncher.launch(new JobLaunchRequest(conf));
	}
}
