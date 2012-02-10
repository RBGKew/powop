package org.emonocot.job.dwc.taxon;

import org.springframework.batch.core.JobExecution;
import org.springframework.batch.core.StepExecution;
import org.springframework.batch.core.job.flow.FlowExecutionStatus;
import org.springframework.batch.core.job.flow.JobExecutionDecider;

/**
 *
 * @author ben
 *
 */
public class TaxonProcessingModeDecider implements JobExecutionDecider {

    /**
     * @param jobExecution Set the job execution
     * @param stepExecution Set the step execution
     * @return the flow execution status
     */
    public final FlowExecutionStatus decide(final JobExecution jobExecution,
            final StepExecution stepExecution) {
        if (jobExecution.getExecutionContext().containsKey(
                "taxon.processing.mode")) {

            String taxonProcessingMode = jobExecution.getExecutionContext()
                    .getString("taxon.processing.mode");
            return new FlowExecutionStatus(taxonProcessingMode);
        } else {
            return new FlowExecutionStatus("CHECK_TAXA");
        }
    }

}
