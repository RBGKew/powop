package org.emonocot.job.dwc.identifier;

import org.springframework.batch.core.JobExecution;
import org.springframework.batch.core.StepExecution;
import org.springframework.batch.core.job.flow.FlowExecutionStatus;
import org.springframework.batch.core.job.flow.JobExecutionDecider;

/**
 *
 * @author ben
 *
 */
public class ProcessIdentifierDecider implements JobExecutionDecider {

    /**
     * @param jobExecution set the job execution
     * @param stepExecution set the step execution
     * @return FlowExecutionStatus a status
     */
    public final FlowExecutionStatus decide(final JobExecution jobExecution,
            final StepExecution stepExecution) {
        if (jobExecution.getExecutionContext()
                .containsKey("dwca.identifier.file")) {
            return new FlowExecutionStatus("IDENTIFIER_PRESENT");
        } else {
            return new FlowExecutionStatus("IDENTIFIER_ABSENT");
        }
    }

}
