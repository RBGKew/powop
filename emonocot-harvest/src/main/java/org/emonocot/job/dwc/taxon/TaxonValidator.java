package org.emonocot.job.dwc.taxon;

import org.emonocot.model.source.Source;
import org.emonocot.model.common.Annotation;
import org.emonocot.model.common.AnnotationType;
import org.emonocot.model.taxon.Taxon;
import org.emonocot.api.SourceService;
import org.emonocot.api.TaxonService;
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
 *
 */
public class TaxonValidator implements ItemProcessor<Taxon, Taxon>,
        StepExecutionListener {
    /**
     *
     */
    private Logger logger = LoggerFactory.getLogger(TaxonValidator.class);

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
     */
    @Autowired
    public final void setTaxonService(TaxonService taxonService) {
        this.taxonService = taxonService;
    }

   /**
    *
    */
   @Autowired
   public final void setSourceService(SourceService sourceService) {
       this.sourceService = sourceService;
   }

    /**
    *
    * @param sourceName Set the name of the Source
    */
    public final void setSourceName(final String sourceName) {
      this.sourceName = sourceName;
    }

    /**
     * @param taxon a taxon object
     * @throws Exception if something goes wrong
     * @return Taxon a taxon object
     */
    public final Taxon process(final Taxon taxon) throws Exception {
        logger.info("Processing " + taxon.getIdentifier());
        if (taxon.getIdentifier() == null) {
            throw new NoIdentifierException(taxon);
        }
        Taxon persistedTaxon = taxonService.find(taxon.getIdentifier());
        if (persistedTaxon == null) {
            throw new CannotFindRecordException(taxon.getIdentifier());
        }

        boolean anAnnotationPresent = false;
        for (Annotation annotation : persistedTaxon.getAnnotations()) {
            if (annotation.getJobId().equals(
                    stepExecution.getJobExecutionId())) {
                if (annotation.getType().equals(AnnotationType.Present)) {
                    throw new TaxonAlreadyProcessedException("Taxon "
                            + taxon.getIdentifier()
                            + " already found once in this archive");
                }
                annotation.setType(AnnotationType.Present);
                anAnnotationPresent = true;
                break;
            }
        }

        if (!anAnnotationPresent) {
            throw new UnexpectedTaxonException(taxon.getIdentifier());
        } else {
            /**
             * Using java.util.Collection.contains() does not work on lazy
             * collections.
             */
            boolean contains = false;
            for (Source auth : persistedTaxon.getSources()) {
                if (auth.equals(getSource())) {
                    contains = true;
                    break;
                }
            }
            if (!contains) {
                persistedTaxon.getSources().add(getSource());
            }
        }
        return persistedTaxon;
    }

    /**
     *
     * @return the source
     */
    private Source getSource() {
        if (source == null) {
            source = sourceService.load(sourceName);
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
