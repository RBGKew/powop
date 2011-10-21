package org.emonocot.job.dwc;

import org.emonocot.api.SourceService;
import org.emonocot.api.TaxonService;
import org.emonocot.model.common.Annotation;
import org.emonocot.model.common.AnnotationType;
import org.emonocot.model.common.BaseData;
import org.emonocot.model.source.Source;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.batch.core.ExitStatus;
import org.springframework.batch.core.StepExecution;
import org.springframework.batch.core.StepExecutionListener;
import org.springframework.batch.item.ItemProcessor;
import org.springframework.beans.factory.annotation.Autowired;

/**
 *
 * @author ben
 * @param <T> the type of object validated
 */
public abstract class DarwinCoreValidator<T extends BaseData> implements
        ItemProcessor<T, T>, StepExecutionListener {
    /**
     *
     */
    private Logger logger = LoggerFactory.getLogger(DarwinCoreValidator.class);

    /**
     *
     */
    private TaxonService taxonService;

    /**
     *
     */
    private StepExecution stepExecution;

    /**
     *
     */
    private Source source;

    /**
     *
     */
    private String sourceName;

    /**
     *
     */
    private SourceService sourceService;

    /**
     *
     * @return the step execution
     */
    public final StepExecution getStepExecution() {
        return stepExecution;
    }

    /**
    *
    * @param type The type of object
    * @param code the code of the annotation
    * @param annotationType the type of annotation
    * @return an annotation
    */
   protected final Annotation createAnnotation(final String type,
           final String code, final AnnotationType annotationType) {
       Annotation annotation = new Annotation();
       annotation.setAnnotatedObjType(type);
       annotation.setJobId(getStepExecution().getJobExecutionId());
       annotation.setCode(code);
       annotation.setType(annotationType);
       annotation.setSource(getSource());
       return annotation;
   }

   /**
    *
    * @param sourceName Set the name of the source
    */
    public final void setSourceName(final String sourceName) {
      this.sourceName = sourceName;
    }

    /**
     * @param taxonService set the taxon service
     */
    @Autowired
    public final void setTaxonService(TaxonService taxonService) {
        this.taxonService = taxonService;
    }

    /**
     *
     * @return the taxon service set
     */
    public final TaxonService getTaxonService() {
        return taxonService;
    }

    /**
    *
    */
   @Autowired
   public final void setSourceService(SourceService sourceService) {
       this.sourceService = sourceService;
   }

    /**
     * @param t an object
     * @throws Exception if something goes wrong
     * @return an object of class T
     */
    public abstract T process(final T t) throws Exception;

    /**
     *
     * @return the source
     */
   public final Source getSource() {
        if (source == null) {
            this.source = sourceService.load(sourceName);
        }
        return source;
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
