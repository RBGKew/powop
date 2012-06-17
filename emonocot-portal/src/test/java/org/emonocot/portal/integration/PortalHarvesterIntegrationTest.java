package org.emonocot.portal.integration;

import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.fail;

import java.util.HashMap;
import java.util.Map;

import org.emonocot.api.job.JobLaunchRequest;
import org.emonocot.api.job.JobLauncher;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.batch.core.JobExecutionException;
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
        } catch (Exception jobExecutionException) {
            System.out.println(jobExecutionException.getMessage());
            fail("No exception expected here");
        }
    }

    /**
     * @throws JobExecutionException
     *             if there is a problem launching the job
     *
     */
    /*@Test
    public final void testLaunchJobUnsuccessfully()
            throws JobExecutionException {
        assertNotNull("jobLaunchRequestHandler should not be null", jobLauncher);
        Job job = new SimpleJob("UnsuccessfulJob");
        mockJobLauncher.setResponse(new JobExecutionException("Exception"));
        JobLaunchRequest jobLaunchRequest = new JobLaunchRequest(job,
                jobParameters);
        boolean exceptionThrown = false;

        try {
            Object jobExecutionInfo = jobLauncher
                    .launch(jobLaunchRequest);
            fail("Exception should have been thrown");
        } catch (JobExecutionException jobExecutionException) {
            System.out.println(jobExecutionException.getMessage());
            exceptionThrown = true;
        }
        assertTrue("Expected an exception to be thrown", exceptionThrown);
    }*/
}
