package org.emonocot.job.dwc;

import org.springframework.batch.core.JobExecution;
import org.springframework.batch.core.StepExecution;
import org.springframework.batch.core.job.flow.FlowExecutionStatus;
import org.springframework.batch.core.job.flow.JobExecutionDecider;

/**
 *
 * @author ben
 *
 */
public class ConfigurableProcessingModeDecider implements JobExecutionDecider {
	
	private String processingModeKey = null;
	
	private String defaultProcessingMode = null;	

    /**
	 * @param processingModeKey the processingModeKey to set
	 */
	public void setProcessingModeKey(String processingModeKey) {
		this.processingModeKey = processingModeKey;
	}

	/**
	 * @param defaultProcessingMode the defaultProcessingMode to set
	 */
	public void setDefaultProcessingMode(String defaultProcessingMode) {
		this.defaultProcessingMode = defaultProcessingMode;
	}

	/**
     * @param jobExecution set the job execution
     * @param stepExecution set the step execution
     * @return FlowExecutionStatus a status
     */
    public final FlowExecutionStatus decide(final JobExecution jobExecution,
            final StepExecution stepExecution) {
        if (jobExecution.getExecutionContext().containsKey(processingModeKey)) {
            return new FlowExecutionStatus(jobExecution.getExecutionContext().getString(processingModeKey));
        } else if(jobExecution.getJobInstance().getJobParameters().getParameters().containsKey(processingModeKey)) {
        	return new FlowExecutionStatus(jobExecution.getJobInstance().getJobParameters().getString(processingModeKey));
        }else {
            return new FlowExecutionStatus(defaultProcessingMode);
        }
    }

}
