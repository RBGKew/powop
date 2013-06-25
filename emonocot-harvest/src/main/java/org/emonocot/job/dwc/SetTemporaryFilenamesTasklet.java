package org.emonocot.job.dwc;

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
    public final RepeatStatus execute(final StepContribution contribution, final ChunkContext chunkContext)
            throws Exception {
        UUID uuid = UUID.randomUUID();
        String unpackDirectoryName = harvesterSpoolDirectory + File.separator
                + uuid.toString();
        String temporaryFileName = harvesterSpoolDirectory + File.separator
                + uuid.toString() + ".zip";

        File unpackDirectory = new File(unpackDirectoryName);
        unpackDirectory.mkdir();
        File temporaryFile = new File(temporaryFileName);
        ExecutionContext executionContext = chunkContext.getStepContext()
                .getStepExecution().getJobExecution().getExecutionContext();
        executionContext.put("temporary.file.name",temporaryFile.getAbsolutePath());
        executionContext.put("unpack.directory.name",unpackDirectory.getAbsolutePath());
        executionContext.putLong("job.execution.id", chunkContext.getStepContext().getStepExecution().getJobExecutionId());
        return RepeatStatus.FINISHED;
    }
}
