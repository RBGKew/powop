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
package org.emonocot.portal.integration;

import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.fail;

import java.util.HashMap;
import java.util.Map;

import org.emonocot.api.job.JobExecutionException;
import org.emonocot.api.job.JobLaunchRequest;
import org.emonocot.api.job.JobLauncher;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;

/**
 *
 * @author ben
 *
 */
@RunWith(SpringJUnit4ClassRunner.class)
@ContextConfiguration({"classpath:META-INF/spring/applicationContext-integrationTest.xml",
                       "classpath:META-INF/spring/applicationContext-integration.xml"})
public class PortalHarvesterIntegrationTest {
	
	private static Logger logger = LoggerFactory.getLogger(PortalHarvesterIntegrationTest.class);

    /**
     *
     */
    @Autowired
    @Qualifier("messageBasedReadWriteJobLauncher")
    private JobLauncher jobLauncher;

    /**
     * @throws JobExecutionException
     *             if there is a problem launching the job
     *
     */
    @Test
    public final void testLaunchJobSuccessfully() throws Exception {
        assertNotNull("jobLaunchRequestHandler should not be null", jobLauncher);
        JobLaunchRequest jobLaunchRequest = new JobLaunchRequest();
        jobLaunchRequest.setJob("SuccessfulJob");
        Map<String, String> jobParameterMap = new HashMap<String, String>();
        jobParameterMap.put("query.string", "from Source");
        jobParameterMap.put("attempt","10");
        jobLaunchRequest.setParameters(jobParameterMap);

        try {
            jobLauncher.launch(jobLaunchRequest);            
        } catch (JobExecutionException jobExecutionException) {
            logger.error(jobExecutionException.getMessage());
            fail("No exception expected here");
        }
    }
}
