package org.emonocot.harvest.common;

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
public class SetTemporaryFilenameTasklet implements Tasklet {

    /**
     *
     */
    private String harvesterSpoolDirectory;
    
    private String extension = "xml";

    public void setHarvesterSpoolDirectory(String harvesterSpoolDirectory) {
        this.harvesterSpoolDirectory = harvesterSpoolDirectory;
    }
    
    public void setExtension(String extension) {
    	 this.extension = extension;
    }
    

    /**
     * @param contribution Set the step contribution
     * @param chunkContext Set the chunk context
     * @return the repeat status
     * @throws Exception if there is a problem deleting the resources
     */
    public  RepeatStatus execute( StepContribution contribution, ChunkContext chunkContext)
            throws Exception {
        UUID uuid = UUID.randomUUID();
        String temporaryFileName = harvesterSpoolDirectory + File.separator
                + uuid.toString() + "." + extension;

        File temporaryFile = new File(temporaryFileName);
        ExecutionContext executionContext = chunkContext.getStepContext()
                .getStepExecution().getJobExecution().getExecutionContext();
        executionContext.put("temporary.file.name",
                temporaryFile.getAbsolutePath());
        return RepeatStatus.FINISHED;
    }
}
