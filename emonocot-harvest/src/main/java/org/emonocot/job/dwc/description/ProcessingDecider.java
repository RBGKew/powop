package org.emonocot.job.dwc.description;

import org.springframework.batch.core.JobExecution;
import org.springframework.batch.core.StepExecution;
import org.springframework.batch.core.job.flow.FlowExecutionStatus;
import org.springframework.batch.core.job.flow.JobExecutionDecider;

/**
 *
 * @author ben
 *
 */
public class ProcessingDecider implements JobExecutionDecider {

    /**
     * @param jobExecution set the job execution
     * @param stepExecution set the step execution
     * @return FlowExecutionStatus a status
     */
    public final FlowExecutionStatus decide(final JobExecution jobExecution,
            final StepExecution stepExecution) {
        if (jobExecution.getExecutionContext()
                .containsKey("dwca.description.file")) {
            return new FlowExecutionStatus("DESCRIPTION_PRESENT");
        } else {
            return new FlowExecutionStatus("DESCRIPTION_ABSENT");
        }
    }

}
