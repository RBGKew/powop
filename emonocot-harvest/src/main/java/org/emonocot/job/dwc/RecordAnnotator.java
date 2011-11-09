package org.emonocot.job.dwc;

import org.emonocot.api.SourceService;
import org.emonocot.model.common.Annotation;
import org.emonocot.model.common.AnnotationCode;
import org.emonocot.model.common.AnnotationType;
import org.emonocot.model.source.Source;
import org.emonocot.model.taxon.Taxon;
import org.joda.time.DateTime;
import org.springframework.batch.core.ExitStatus;
import org.springframework.batch.core.StepExecution;
import org.springframework.batch.core.StepExecutionListener;
import org.springframework.batch.item.ItemProcessor;

/**
 *
 * @author ben
 *
 */
public class RecordAnnotator implements ItemProcessor<Taxon, Taxon>,
        StepExecutionListener {

    /**
     *
     */
    private String sourceName;

    /**
     *
     */
    private Source source;

    /**
     *
     */
    private SourceService sourceService;

    /**
     *
     */
    private StepExecution stepExecution;

    /**
     *
     * @param newSourceName Set the source name
     */
    public final void setSourceName(final String newSourceName) {
        this.sourceName = newSourceName;
    }

    /**
     * @return the source
     */
    private Source getSource() {
        if (source == null) {
            source = sourceService.load(sourceName);
        }
        return source;
    }

    /**
     *
     * @param newSourceService Set the source service
     */
    public final void setSourceService(final SourceService newSourceService) {
        this.sourceService = newSourceService;
    }

    /**
     * @param taxon Set the taxon
     * @return an annotated taxon
     */
    public final Taxon process(final Taxon taxon) {
        Annotation annotation = new Annotation();
        annotation.setAnnotatedObj(taxon);
        annotation.setDateTime(new DateTime());
        annotation.setJobId(stepExecution.getJobExecutionId());
        annotation.setSource(getSource());
        annotation.setCode(AnnotationCode.Absent);
        annotation.setType(AnnotationType.Warn);
        taxon.getAnnotations().add(annotation);
        return taxon;
    }

    /**
     * @param newStepExecution Set the step execution
     */
    public final void beforeStep(final StepExecution newStepExecution) {
        this.stepExecution = newStepExecution;
    }

    /**
     * @param newStepExecution Set the step execution
     * @return the exit status
     */
    public final ExitStatus afterStep(final StepExecution newStepExecution) {
        return null;
    }
}
