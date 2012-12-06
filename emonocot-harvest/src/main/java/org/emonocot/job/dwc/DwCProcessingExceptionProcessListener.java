package org.emonocot.job.dwc;

import java.util.List;

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
import org.springframework.batch.core.ItemWriteListener;
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
        ItemReadListener<Base>, ItemWriteListener<Base>, StepExecutionListener {
	
    private Logger logger = LoggerFactory.getLogger(DwCProcessingExceptionProcessListener.class);

    private StepExecution stepExecution;
    
    public void beforeProcess(final Base item) {

    }

    public void afterProcess(final Base item, final Base result) {

    }

    public final void onProcessError(final Base item, final Exception e) {
        logger.error("Process Error " + e.getMessage());
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
        switch(stepExecution.getStepName()) {
        case "processCoreFile":
            return RecordType.Taxon;
        case "processDescriptionFile":
            return RecordType.Description;
        case "processIdentifierFile":
            return RecordType.Identifier;
        case "processImageFile":
            return RecordType.Image;
        case "processReferenceFile":
            return RecordType.Reference;
        case "processDistributionFile":
            return RecordType.Distribution;
        case "processMeasurementOrFactFile":
            return RecordType.MeasurementOrFact;
        case "processVernacularNameFile":
            return RecordType.VernacularName;
        case "processTypeAndSpecimenFile":
            return RecordType.TypeAndSpecimen;
        default:
        	return null;
        }
    }

    public final void beforeStep(final StepExecution newStepExecution) {
        this.stepExecution = newStepExecution;
    }

    public final ExitStatus afterStep(final StepExecution newStepExecution) {
        return null;
    }

    public void afterRead(final Base base) {

    }

    public void beforeRead() {

    }

    public final void onReadError(final Exception e) {
    	logger.error("Read Error " + e.getMessage());
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

	@Override
	public void beforeWrite(List<? extends Base> items) {
		
	}

	@Override
	public void afterWrite(List<? extends Base> items) {
		
	}

	@Override
	public void onWriteError(Exception exception, List<? extends Base> items) {
		logger.error(exception.getLocalizedMessage());
	}

}
