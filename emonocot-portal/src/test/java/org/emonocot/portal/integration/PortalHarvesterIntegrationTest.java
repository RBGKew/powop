package org.emonocot.portal.integration;

import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertNull;
import static org.junit.Assert.fail;

import java.util.HashMap;
import java.util.Map;

import org.emonocot.api.job.JobExecutionException;
import org.emonocot.api.job.JobLaunchRequest;
import org.emonocot.api.job.JobLauncher;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
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

    /**
     *
     */
    @Autowired
    private JobLauncher jobLauncher;

    /**
     *
     */
    @Autowired
    private MockJobLauncher mockJobLauncher;

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
            Object jobExecutionInfo = jobLauncher
                    .launch(jobLaunchRequest);
            assertNotNull("jobExecutionInfo should not be null",
                    jobExecutionInfo);
        } catch (JobExecutionException jobExecutionException) {
            System.out.println(jobExecutionException.getMessage());
            fail("No exception expected here");
        }
    }

    /**
     * @throws JobExecutionException
     *             if there is a problem launching the job
     *
     */
    @Test
    public final void testLaunchJobUnsuccessfully()
            throws JobExecutionException {
        assertNotNull("jobLaunchRequestHandler should not be null", jobLauncher);
        JobLaunchRequest jobLaunchRequest = new JobLaunchRequest();
        jobLaunchRequest.setJob("UnsuccessfulJob");
        mockJobLauncher.setException(new JobExecutionException("Exception"));
        mockJobLauncher.setExecution(null);

        jobLaunchRequest = jobLauncher.launch(jobLaunchRequest);
            
        assertNull("There should be no job execution", jobLaunchRequest.getExecution());
        assertNotNull("Expected an exception to be thrown", jobLaunchRequest.getException());
    }
}
