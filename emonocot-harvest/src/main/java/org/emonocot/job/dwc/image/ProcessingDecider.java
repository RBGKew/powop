package org.emonocot.job.dwc.image;

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
                .containsKey("dwca.image.file")) {
            return new FlowExecutionStatus("IMAGE_PRESENT");
        } else {
            return new FlowExecutionStatus("IMAGE_ABSENT");
        }
    }

}
