    package org.emonocot.job.dwc.taxon;

import org.emonocot.job.dwc.DarwinCoreProcessor;
import org.emonocot.model.Annotation;
import org.emonocot.model.Source;
import org.emonocot.model.Taxon;
import org.emonocot.model.constants.AnnotationCode;
import org.emonocot.model.constants.AnnotationType;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 *
 * @author ben
 *
 */
public class CheckingValidator extends DarwinCoreProcessor<Taxon> {
    /**
     *
     */
    private Logger logger = LoggerFactory.getLogger(CheckingValidator.class);

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
        Taxon persistedTaxon = getTaxonService().find(taxon.getIdentifier(), "taxon-with-annotations");
        if (persistedTaxon == null) {
            throw new CannotFindRecordException(taxon.getIdentifier());
        }

        boolean anAnnotationPresent = false;
        for (Annotation annotation : persistedTaxon.getAnnotations()) {
        	logger.info("Comparing " + annotation.getJobId() + " with " + getStepExecution().getJobExecutionId());
            if (getStepExecution().getJobExecutionId().equals(
            		annotation.getJobId())) {
                if (annotation.getCode().equals(AnnotationCode.Present)) {
                    throw new TaxonAlreadyProcessedException(taxon);
                }
                annotation.setType(AnnotationType.Info);
                annotation.setCode(AnnotationCode.Present);
                anAnnotationPresent = true;
                break;
            }
        }

        if (!anAnnotationPresent) {
        	logger.warn(taxon.getIdentifier() + " was not expected");
            throw new UnexpectedTaxonException(taxon);
        } else {
        	logger.info(taxon.getIdentifier() + " was expected");
            /**
             * Using java.util.Collection.contains() does not work on lazy
             * collections.
             */
            boolean contains = false;
            for (Source auth : persistedTaxon.getSources()) {
                logger.debug("Comparing " + auth.getIdentifier() + " with " + getSource().getIdentifier());
                if (auth.equals(getSource())) {
                    contains = true;
                    break;
                }
            }
            if (!contains) {
                logger.debug("Adding " + getSource());
                persistedTaxon.getSources().add(getSource());
            } else {
                logger.debug(persistedTaxon + " already contains " + getSource());
            }
        }
        return persistedTaxon;
    }
}
