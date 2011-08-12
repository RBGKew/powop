package org.emonocot.job.dwc.taxon;

import org.emonocot.model.common.Annotation;
import org.emonocot.model.taxon.Taxon;
import org.emonocot.service.TaxonService;
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
    @Autowired
    public final void setTaxonService(TaxonService taxonService) {
        this.taxonService = taxonService;
    }

    /**
     * @param taxon a taxon object
     * @throws Exception if something goes wrong
     * @return Taxon a taxon object
     */
    public final Taxon process(final Taxon taxon) throws Exception {
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
                annotation.setPresent(true);
                anAnnotationPresent = true;
                break;
            }
        }

        if (!anAnnotationPresent) {
            throw new UnexpectedTaxonException(taxon.getIdentifier());
        }

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
