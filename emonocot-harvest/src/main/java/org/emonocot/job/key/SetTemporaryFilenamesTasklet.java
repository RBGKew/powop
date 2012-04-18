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
        String xsltFileName = harvesterSpoolDirectory + File.separator
        + uuid.toString() +  "-transform.xslt";
        String outputFileName = harvesterSpoolDirectory + File.separator
                + uuid.toString() +  "-output.json";
        String inputFileName = harvesterSpoolDirectory + File.separator
                + uuid.toString() + "-input.xml";

        File xsltFile = new File(xsltFileName);
        executionContext.put("transform.file.name",
                xsltFile.getAbsolutePath());
        File inputFile = new File(inputFileName);
        executionContext.put("input.file.name",
                inputFile.getAbsolutePath());
        File outputFile = new File(outputFileName);
        executionContext.put("output.file.name",
                outputFile.getAbsolutePath());

        return RepeatStatus.FINISHED;
    }
}
