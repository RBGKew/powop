package org.emonocot.portal.integration;

import org.springframework.batch.integration.launch.JobLaunchRequest;


/**
 *
 * @author ben
 *
 */
public class JobLaunchRequestToStringAdapter {

    /**
     *
     * @param jobLaunchRequest Set the job launch request
     * @return the job launch request as a string
     */
    public final String adapt(final JobLaunchRequest jobLaunchRequest) {
        StringBuffer stringBuffer = new StringBuffer();
        stringBuffer.append(jobLaunchRequest.getJob().getName());
        stringBuffer.append("[");
        for (String parameterName : jobLaunchRequest.getJobParameters()
                .getParameters().keySet()) {
            stringBuffer.append(parameterName
                    + "="
                    + jobLaunchRequest.getJobParameters().getParameters()
                            .get(parameterName).getValue());
            stringBuffer.append(", ");
        }
        stringBuffer.append("]");
        return stringBuffer.toString();
    }
}
