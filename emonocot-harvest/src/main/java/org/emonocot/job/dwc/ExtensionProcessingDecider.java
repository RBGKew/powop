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
public class ExtensionProcessingDecider implements JobExecutionDecider {
	
	private String processingModeKey = null;
	
	

    /**
	 * @param processingModeKey the processingModeKey to set
	 */
	public void setProcessingModeKey(String processingModeKey) {
		this.processingModeKey = processingModeKey;
	}



	/**
     * @param jobExecution set the job execution
     * @param stepExecution set the step execution
     * @return FlowExecutionStatus a status
     */
    public final FlowExecutionStatus decide(final JobExecution jobExecution,
            final StepExecution stepExecution) {
        if (jobExecution.getExecutionContext().containsKey(processingModeKey)) {
            return new FlowExecutionStatus("true");
        } else {
            return new FlowExecutionStatus("false");
        }
    }

}
