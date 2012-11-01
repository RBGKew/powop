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
public class Validator extends DarwinCoreProcessor<Taxon> {
    /**
     *
     */
    private Logger logger = LoggerFactory.getLogger(Validator.class);

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
        Taxon persistedTaxon = getTaxonService().find(taxon.getIdentifier());
        if (persistedTaxon == null) {
            throw new CannotFindRecordException(taxon.getIdentifier());
        }

        boolean anAnnotationPresent = false;
        for (Annotation annotation : persistedTaxon.getAnnotations()) {
            if (annotation.getJobId().equals(
                    getStepExecution().getJobExecutionId())) {
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
            throw new UnexpectedTaxonException(taxon);
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
}
