package org.emonocot.job.dwc;

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import org.gbif.dwc.text.Archive;
import org.gbif.dwc.text.ArchiveFactory;
import org.gbif.dwc.text.ArchiveField;
import org.gbif.dwc.text.ArchiveFile;
import org.gbif.dwc.text.UnsupportedArchiveException;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.batch.core.ExitStatus;
import org.springframework.batch.core.StepExecution;
import org.springframework.batch.core.StepExecutionListener;
import org.springframework.batch.item.ExecutionContext;

/**
 *
 * @author ben
 *
 */
public class ArchiveMetadataReader implements StepExecutionListener {
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
    public final void setArchiveFactory(
            final ArchiveFactory newArchiveFactory) {
        this.archiveFactory = newArchiveFactory;
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
                = this.stepExecution.getJobExecution().getExecutionContext();
            executionContext.put("dwca.core.file", core.getLocationFile()
                    .getAbsolutePath());
            executionContext.put("dwca.core.fieldsTerminatedBy",
                    core.getFieldsTerminatedBy());
            executionContext.put("dwca.core.encoding", core.getEncoding());

            if (core.getIgnoreHeaderLines() != null) {
              executionContext.put("dwca.core.ignoreHeaderLines",
                      core.getIgnoreHeaderLines());
            } else {
              executionContext.put("dwca.core.ignoreHeaderLines", 0);
            }

            List<ArchiveField> fields = core.getFieldsSorted();
            executionContext.put("dwca.core.fieldNames",
                    toFieldNames(fields));
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

    /**
     * Method which converts from archive fields to string field names.
     *  To be used by fieldSet mapper
     *
     * @param fields the DwC/A fields
     * @return an array of string names
     */
    private String[] toFieldNames(final List<ArchiveField> fields) {

        List<String> names = new ArrayList<String>();
        for (ArchiveField field : fields) {
            if (field.getIndex() != null) {
                names.add(field.getTerm().qualifiedName());
            }
        }
        logger.info("Archive contains field names " + names);
        return names.toArray(new String[names.size()]);
    }

    @Override
    public final ExitStatus afterStep(final StepExecution newStepExecution) {
        return null;
    }

    @Override
    public final void beforeStep(final StepExecution newStepExecution) {
        this.stepExecution = newStepExecution;
    }

}
