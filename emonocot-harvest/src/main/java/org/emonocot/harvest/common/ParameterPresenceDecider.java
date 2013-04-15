/**
 * 
 */
package org.emonocot.harvest.common;

import java.util.Map;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.batch.core.JobExecution;
import org.springframework.batch.core.JobParameter;
import org.springframework.batch.core.StepExecution;
import org.springframework.batch.core.job.flow.FlowExecutionStatus;
import org.springframework.batch.core.job.flow.JobExecutionDecider;
import org.springframework.batch.item.ExecutionContext;

/**
 * @author jk00kg
 * 
 * Utility class to inspect the execution for a provided parameter 
 */
public class ParameterPresenceDecider implements JobExecutionDecider {
    
    private Logger logger = LoggerFactory.getLogger(ParameterPresenceDecider.class);

    /**
     * 
     */
    private Object parameter;

    /**
     * @return the parameter
     */
    public Object getParameter() {
        return parameter;
    }

    /**
     * @param parameter the parameter to set
     */
    public void setParameter(Object parameter) {
        this.parameter = parameter;
    }

    /* (non-Javadoc)
     * @see org.springframework.batch.core.job.flow.JobExecutionDecider#decide(org.springframework.batch.core.JobExecution, org.springframework.batch.core.StepExecution)
     */
    @Override
    public FlowExecutionStatus decide(JobExecution jobExecution,
            StepExecution stepExecution) {
        logger.debug("Looking for parameter " + parameter);
        ExecutionContext context = jobExecution.getExecutionContext();
        if(parameter == null) { //There is nothing to look for
            return null;
        }
        //TODO Possible extension point to delegate to a callback/add option to run additional checks here
        //Look for the parameter in the ExecutionContext
        if(context.containsKey(parameter.toString())) {
            return new FlowExecutionStatus("true-key");
        }
        if(context.containsValue(parameter)) {
            return new FlowExecutionStatus("true-value");
        }
        //Look for the parameter in the jobParameterMap
        Map<String, JobParameter> jobParameterMap = jobExecution.getJobInstance().getJobParameters().getParameters();
        if(jobParameterMap.containsKey(parameter.toString())) {
            return new FlowExecutionStatus("true-key");
        }
        if(jobParameterMap.containsValue(parameter)) {
            return new FlowExecutionStatus("true-value");
        }
        
        //Assume false
        return new FlowExecutionStatus("false");
    }

}
