package org.emonocot.job.dwc;

import org.emonocot.harvest.common.AbstractRecordAnnotator;
import org.emonocot.job.dwc.exception.CannotFindRecordException;
import org.emonocot.job.dwc.exception.DarwinCoreProcessingException;
import org.emonocot.model.Annotation;
import org.emonocot.model.Base;
import org.emonocot.model.constants.AnnotationCode;
import org.emonocot.model.constants.AnnotationType;
import org.emonocot.model.constants.RecordType;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.batch.core.ExitStatus;
import org.springframework.batch.core.ItemProcessListener;
import org.springframework.batch.core.ItemReadListener;
import org.springframework.batch.core.StepExecution;
import org.springframework.batch.core.StepExecutionListener;
import org.springframework.batch.item.file.FlatFileParseException;
import org.springframework.validation.BindException;

/**
 *
 * @author ben
 *
 */
public class DwCProcessingExceptionProcessListener extends
        AbstractRecordAnnotator implements ItemProcessListener<Base, Base>,
        ItemReadListener<Base>, StepExecutionListener {
	
    private Logger logger = LoggerFactory.getLogger(DwCProcessingExceptionProcessListener.class);

    private StepExecution stepExecution;

    private static final String PROCESS_CORE_FILE = "processCoreFile";

    private static final String PROCESS_DESCRIPTION_FILE = "processDescriptionFile";

    private static final String PROCESS_IMAGE_FILE = "processImageFile";

    private static final String PROCESS_REFERENCE_FILE = "processReferenceFile";
    
    private static final String PROCESS_DISTRIBUTION_FILE = "processDistributionFile";
    
    private static final String PROCESS_MEASUREMENT_OR_FACT_FILE = "processMeasurementOrFactFile";
    
    private static final String PROCESS_VERNACULAR_NAME_FILE = "processVernacularNameFile";
    
    private static final String PROCESS_TYPE_AND_SPECIMEN_FILE = "processTypeAndSpecimenFile";

    /**
     * @param item
     *            the item to be processed
     */
    public void beforeProcess(final Base item) {

    }

    /**
     * @param item
     *            the item to be processed
     * @param result
     *            the resulting object
     */
    public void afterProcess(final Base item, final Base result) {

    }

    /**
     * @param item
     *            the item to be processed
     * @param e
     *            the exception thrown
     */
    public final void onProcessError(final Base item, final Exception e) {
        logger.debug("Process Error " + e.getMessage());
        if (e instanceof DarwinCoreProcessingException) {
            DarwinCoreProcessingException dwcpe = (DarwinCoreProcessingException) e;
            logger.debug(dwcpe.getCode() + " | " + dwcpe.getMessage());
            super.annotate(dwcpe.getRecordType(),
                    stepExecution.getJobExecutionId(), dwcpe.getCode(),
                    dwcpe.getValue(), dwcpe.getType(), dwcpe.getMessage());
        }
    }

    /**
     *
     * @return the record type we are currently processing
     */
    private RecordType getRecordType() {
        String stepName = stepExecution.getStepName();
        if (stepName.equals(PROCESS_CORE_FILE)) {
            return RecordType.Taxon;
        } else if (stepName.equals(PROCESS_DESCRIPTION_FILE)) {
            return RecordType.Description;
        } else if (stepName.equals(PROCESS_IMAGE_FILE)) {
            return RecordType.Description;
        } else if (stepName.equals(PROCESS_REFERENCE_FILE)) {
            return RecordType.Reference;
        } else if (stepName.equals(PROCESS_DISTRIBUTION_FILE)) {
            return RecordType.Distribution;
        } else if (stepName.equals(PROCESS_MEASUREMENT_OR_FACT_FILE)) {
            return RecordType.MeasurementOrFact;
        } else if (stepName.equals(PROCESS_VERNACULAR_NAME_FILE)) {
            return RecordType.VernacularName;
        } else if (stepName.equals(PROCESS_TYPE_AND_SPECIMEN_FILE)) {
            return RecordType.TypeAndSpecimen;
        }
        return null;
    }

    /**
     * @param newStepExecution
     *            Set the step execution
     */
    public final void beforeStep(final StepExecution newStepExecution) {
        this.stepExecution = newStepExecution;
    }

    /**
     * @param newStepExecution
     *            Set the step execution
     * @return the exit status
     */
    public final ExitStatus afterStep(final StepExecution newStepExecution) {
        return null;
    }

    /**
     * @param base
     *            the object read
     */
    public void afterRead(final Base base) {

    }

    /**
     *
     */
    public void beforeRead() {

    }

    /**
     * @param e
     *            the exception
     */
    public final void onReadError(final Exception e) {
        if (e instanceof FlatFileParseException) {
            FlatFileParseException ffpe = (FlatFileParseException) e;
            StringBuffer message = new StringBuffer();
            message.append(ffpe.getMessage());
            final Annotation annotation = new Annotation();
            if (ffpe.getCause() != null) {
                message.append(" " + ffpe.getCause().getMessage());
                logger.debug("FlatFileParseException | " + ffpe.getMessage()
                        + " Cause " + ffpe.getCause().getMessage());
                if (ffpe.getCause().getClass()
                        .equals(CannotFindRecordException.class)) {
                    annotation.setCode(AnnotationCode.BadIdentifier);
                    CannotFindRecordException cfre = (CannotFindRecordException) ffpe
                            .getCause();
                    annotation.setValue(cfre.getValue());
                } else if (ffpe.getCause().getClass()
                        .equals(BindException.class)) {
                    annotation.setCode(AnnotationCode.BadField);
                    BindException be = (BindException) ffpe.getCause();
                    annotation.setValue(be.getFieldError().getField());
                } else {
                    annotation.setCode(AnnotationCode.BadRecord);
                }
            } else {
                logger.debug("FlatFileParseException | " + ffpe.getMessage());
            }

            annotation.setJobId(stepExecution.getJobExecutionId());
            annotation.setRecordType(getRecordType());
            annotation.setType(AnnotationType.Error);
            annotation.setText(message.toString());
            super.annotate(annotation);
        }
    }

}
