package org.emonocot.job.iucn;

import java.io.File;
import java.util.UUID;

import org.springframework.batch.core.StepContribution;
import org.springframework.batch.core.scope.context.ChunkContext;
import org.springframework.batch.core.step.tasklet.Tasklet;
import org.springframework.batch.item.ExecutionContext;
import org.springframework.batch.repeat.RepeatStatus;

/**
 *
 * @author ben
 *
 */
public class SetTemporaryFilenamesTasklet implements Tasklet {

    /**
     *
     */
    private String harvesterSpoolDirectory;

    /**
     * @param newHarvesterSpoolDirectory the harvesterSpoolDirectory to set
     */
    public final void setHarvesterSpoolDirectory(
            final String newHarvesterSpoolDirectory) {
        this.harvesterSpoolDirectory = newHarvesterSpoolDirectory;
    }

    /**
     * @param contribution Set the step contribution
     * @param chunkContext Set the chunk context
     * @return the repeat status
     * @throws Exception if there is a problem deleting the resources
     */
    public final RepeatStatus execute(final StepContribution contribution,
            final ChunkContext chunkContext)
            throws Exception {
        UUID uuid1 = UUID.randomUUID();
        String splitFileName = harvesterSpoolDirectory + File.separator
                + uuid1.toString() + ".json";
        UUID uuid2 = UUID.randomUUID();
        String temporaryFileName = harvesterSpoolDirectory + File.separator
                + uuid2.toString() + ".json";
        
        File temporaryFile = new File(temporaryFileName);
        File splitFile = new File(splitFileName);
        ExecutionContext executionContext = chunkContext.getStepContext()
                .getStepExecution().getJobExecution().getExecutionContext();
        executionContext.put("temporary.file.name", temporaryFile.getAbsolutePath());
        executionContext.put("split.file.name", splitFile.getAbsolutePath());
        executionContext.putLong("job.execution.id", chunkContext.getStepContext().getStepExecution().getJobExecutionId());
        return RepeatStatus.FINISHED;
    }
}
