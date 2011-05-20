package org.emonocot.job.dwc;

import java.io.File;
import java.io.IOException;

import org.gbif.dwc.text.Archive;
import org.gbif.dwc.text.ArchiveFactory;
import org.gbif.dwc.text.ArchiveFile;
import org.gbif.dwc.text.UnsupportedArchiveException;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.batch.core.ExitStatus;
import org.springframework.batch.core.JobExecution;
import org.springframework.batch.core.StepExecution;
import org.springframework.batch.core.annotation.BeforeStep;
import org.springframework.batch.item.ExecutionContext;
import org.springframework.beans.factory.annotation.Autowired;

/**
 *
 * @author ben
 *
 */
public class ArchiveMetadataReader {
    /**
    *
    */
    private Logger logger
        = LoggerFactory.getLogger(ArchiveMetadataReader.class);

    /**
     *
     */
    private StepExecution stepExecution;

    /**
     *
     */
    private ArchiveFactory archiveFactory;

    /**
     *
     * @param newArchiveFactory Set the Archive Factory
     */
    @Autowired
    public final void setArchiveFactory(
            final ArchiveFactory newArchiveFactory) {
        this.archiveFactory = newArchiveFactory;
    }

    /**
     *
     * @param newStepExection Set the step execution
     */
    @BeforeStep
    public final void saveStepExecution(final StepExecution newStepExection) {
        this.stepExecution = newStepExection;
    }
    /**
     *
     * @param archiveDirectory
     *            The directory where the DwC Archive has been unpacked to
     * @return An exit status indicating whether the step has been completed or
     *         failed
     */
    public final ExitStatus readMetadata(final String archiveDirectory) {
        try {
            Archive archive
                = archiveFactory.openArchive(new File(archiveDirectory));

            ArchiveFile core = archive.getCore();
            ExecutionContext executionContext
                = this.stepExecution.getExecutionContext();
            executionContext.put("dwca.core.file", core.getLocationFile().getAbsolutePath());
            executionContext.put("dwca.core.fieldsTerminatedBy", core.getFieldsTerminatedBy());
            executionContext.put("dwca.core.encoding", core.getEncoding());
            executionContext.put("dwca.core.ignoreHeaderLines", core.getIgnoreHeaderLines());
            executionContext.put("dwca.core.ignoreHeaderLines", core.getIgnoreHeaderLines());
        } catch (UnsupportedArchiveException uae) {
            logger.error("Unsupported Archive Exception reading "
                    + archiveDirectory + " " + uae.getLocalizedMessage());
            return ExitStatus.FAILED;
        } catch (IOException ioe) {
            logger.error("Input Output Exception reading "
                    + archiveDirectory + " " + ioe.getLocalizedMessage());
            return ExitStatus.FAILED;
        }
        return ExitStatus.COMPLETED;
    }

}
