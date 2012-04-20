package org.emonocot.job.key;

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
        ExecutionContext executionContext = chunkContext.getStepContext()
        .getStepExecution().getJobExecution().getExecutionContext();
        UUID uuid = UUID.randomUUID();
        String taxonFileName = harvesterSpoolDirectory + File.separator
        + uuid.toString() +  "-taxon.xml";
        String imageFileName = harvesterSpoolDirectory + File.separator
        + uuid.toString() +  "-image.xml";
        String outputFileName = harvesterSpoolDirectory + File.separator
                + uuid.toString() +  "-output.json";
        String inputFileName = harvesterSpoolDirectory + File.separator
                + uuid.toString() + "-input.xml";

        File taxonFile = new File(taxonFileName);
        executionContext.put("taxon.file.name",
                taxonFile.getAbsolutePath());
        File imageFile = new File(imageFileName);
        executionContext.put("image.file.name",
                imageFile.getAbsolutePath());
        File inputFile = new File(inputFileName);
        executionContext.put("input.file.name",
                inputFile.getAbsolutePath());
        File outputFile = new File(outputFileName);
        executionContext.put("output.file.name",
                outputFile.getAbsolutePath());

        return RepeatStatus.FINISHED;
    }
}
