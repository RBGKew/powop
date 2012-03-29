package org.emonocot.portal.integration;

import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertTrue;
import static org.junit.Assert.fail;

import java.util.HashMap;
import java.util.Map;

import org.emonocot.api.job.JobExecutionInfo;
import org.emonocot.model.user.User;
import org.emonocot.test.TestAuthentication;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.batch.core.Job;
import org.springframework.batch.core.JobExecutionException;
import org.springframework.batch.core.JobParameter;
import org.springframework.batch.core.JobParameters;
import org.springframework.batch.core.job.SimpleJob;
import org.springframework.batch.integration.launch.JobLaunchRequest;
import org.springframework.batch.integration.launch.JobLaunchRequestHandler;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.integration.MessagingException;
import org.springframework.security.core.context.SecurityContext;
import org.springframework.security.core.context.SecurityContextHolder;
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
    private JobParameters jobParameters = null;

    /**
     *
     */
    @Before
    public final void setUp() {
        SecurityContext securityContext = SecurityContextHolder.getContext();
        User user = new User();
        user.setUsername("admin");
        user.setPassword("adm1n");
        securityContext
          .setAuthentication(
                  new TestAuthentication(user));

        Map<String, JobParameter> jobParameterMap = new HashMap<String, JobParameter>();
        jobParameterMap.put("query.string", new JobParameter("from Source"));
        jobParameterMap.put("attempt", new JobParameter("10"));
        jobParameters = new JobParameters(jobParameterMap);
    }

    /**
     * @throws JobExecutionException
     *             if there is a problem launching the job
     *
     */
    @Test
    public final void testLaunchJobSuccessfully() throws JobExecutionException {
        assertNotNull("jobLaunchRequestHandler should not be null", jobLauncher);
        Job job = new SimpleJob("SuccessfulJob");
        JobLaunchRequest jobLaunchRequest = new JobLaunchRequest(job,
                jobParameters);

        try {
            JobExecutionInfo jobExecutionInfo = jobLauncher
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
        Job job = new SimpleJob("UnsuccessfulJob");
        JobLaunchRequest jobLaunchRequest = new JobLaunchRequest(job,
                jobParameters);
        boolean exceptionThrown = false;

        try {
            JobExecutionInfo jobExecutionInfo = jobLauncher
                    .launch(jobLaunchRequest);
            fail("Exception should have been thrown");
        } catch (JobExecutionException jobExecutionException) {
            System.out.println(jobExecutionException.getMessage());
            exceptionThrown = true;
        }
        assertTrue("Expected an exception to be thrown", exceptionThrown);
    }
}
