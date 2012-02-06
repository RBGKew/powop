package org.emonocot.job.dwc.taxon;

import org.emonocot.job.dwc.DarwinCoreValidator;
import org.emonocot.model.common.Annotation;
import org.emonocot.model.common.AnnotationCode;
import org.emonocot.model.common.AnnotationType;
import org.emonocot.model.source.Source;
import org.emonocot.model.taxon.Taxon;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 *
 * @author ben
 *
 */
public class TaxonImportingValidator extends DarwinCoreValidator<Taxon> {
    /**
     *
     */
    private Logger logger = LoggerFactory.getLogger(TaxonImportingValidator.class);

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
