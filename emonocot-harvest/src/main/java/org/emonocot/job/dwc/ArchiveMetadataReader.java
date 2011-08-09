package org.emonocot.job.dwc;

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.gbif.dwc.terms.ConceptTerm;
import org.gbif.dwc.terms.DwcTerm;
import org.gbif.dwc.terms.GbifTerm;
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
            getMetadata(core, "core", DwcTerm.taxonID);

            if (archive.getExtension(GbifTerm.Description) != null) {
                getMetadata(archive.getExtension(GbifTerm.Description),
                        "description", DwcTerm.taxonID);
            }

            if (archive.getExtension(GbifTerm.Distribution) != null) {
                getMetadata(archive.getExtension(GbifTerm.Distribution),
                        "distribution", DwcTerm.taxonID);
            }

            if (archive.getExtension(GbifTerm.Image) != null) {
                getMetadata(archive.getExtension(GbifTerm.Image),
                        "image", DwcTerm.taxonID);
            }

            if (archive.getExtension(GbifTerm.Reference) != null) {
                getMetadata(archive.getExtension(GbifTerm.Reference),
                        "reference", DwcTerm.taxonID);
            }
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
     *
     * @param archiveFile The archive file to examine
     * @param prefix the prefix to append to the properties
     * @param identifierTerm the name of the identifier property
     */
    private void getMetadata(final ArchiveFile archiveFile,
            final String prefix, final ConceptTerm identifierTerm) {
        ExecutionContext executionContext = this.stepExecution
                .getJobExecution().getExecutionContext();
        executionContext.put("dwca." + prefix + ".file", archiveFile
                .getLocationFile().getAbsolutePath());
        executionContext.put("dwca." + prefix + ".fieldsTerminatedBy",
                archiveFile.getFieldsTerminatedBy());
        executionContext.put("dwca." + prefix + ".encoding",
                archiveFile.getEncoding());

        if (archiveFile.getIgnoreHeaderLines() != null) {
            executionContext.put("dwca." + prefix + ".ignoreHeaderLines",
                    archiveFile.getIgnoreHeaderLines());
        } else {
            executionContext.put("dwca." + prefix + ".ignoreHeaderLines", 0);
        }
        ArchiveField idField = archiveFile.getId();
        idField.setTerm(identifierTerm);

        List<ArchiveField> fields = archiveFile.getFieldsSorted();
        fields.add(idField.getIndex(), idField);
        executionContext.put("dwca." + prefix + ".fieldNames",
                toFieldNames(fields));

        executionContext.put("dwca." + prefix + ".defaultValues",
                getDefaultValues(fields));
    }

    /**
     * Method which extracts the default fields from the
     * archive and exposes them.
     *
     * @param fields the list of fields
     * @return a map of property names -> default values
     */
    private Map<String, String> getDefaultValues(
            final List<ArchiveField> fields) {
        Map<String, String> defaultValues
            = new HashMap<String, String>();
        for (ArchiveField field : fields) {
            if (field.getIndex() == null) {
                defaultValues.put(field.getTerm().qualifiedName(),
                        field.getDefaultValue());
            }
        }
        return defaultValues;
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

    public final ExitStatus afterStep(final StepExecution newStepExecution) {
        return null;
    }

    public final void beforeStep(final StepExecution newStepExecution) {
        this.stepExecution = newStepExecution;
    }

}
