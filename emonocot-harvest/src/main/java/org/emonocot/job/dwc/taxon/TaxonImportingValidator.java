package org.emonocot.job.dwc.taxon;

import org.emonocot.harvest.common.TaxonRelationshipResolver;
import org.emonocot.job.dwc.DarwinCoreValidator;
import org.emonocot.model.common.Annotation;
import org.emonocot.model.common.AnnotationCode;
import org.emonocot.model.common.AnnotationType;
import org.emonocot.model.common.RecordType;
import org.emonocot.model.taxon.Taxon;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;

/**
 *
 * @author ben
 *
 */
public class TaxonImportingValidator extends DarwinCoreValidator<Taxon> {
    /**
     *
     */
    private Logger logger = LoggerFactory
            .getLogger(TaxonImportingValidator.class);

    /**
     *
     */
    private TaxonRelationshipResolver taxonRelationshipResolver;

    /**
     * @param resolver set the taxon relationship resolver
     */
    @Autowired
    public final void setTaxonRelationshipResolver(
            final TaxonRelationshipResolver resolver) {
        this.taxonRelationshipResolver = resolver;
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
        Taxon persistedTaxon = getTaxonService().find(taxon.getIdentifier());
        if (persistedTaxon == null) {
            taxonRelationshipResolver.bind(taxon);
            Annotation annotation = createAnnotation(taxon,
                    RecordType.Taxon, AnnotationCode.Create,
                    AnnotationType.Info);
            taxon.getAnnotations().add(annotation);
            logger.info("Adding taxon " + taxon.getIdentifier());
            return taxon;
        } else {
            taxonRelationshipResolver.bind(persistedTaxon);
            taxonRelationshipResolver.updateTaxon(persistedTaxon);
            if ((persistedTaxon.getModified() != null
                    && taxon.getModified() != null)
                    && !persistedTaxon.getModified().isBefore(
                            taxon.getModified())) {
            } else {
                taxonRelationshipResolver.bind(persistedTaxon);
                persistedTaxon.setModified(taxon.getModified());
                persistedTaxon.setCreated(taxon.getCreated());
                persistedTaxon.setSource(taxon.getSource());
                persistedTaxon.setNameIdentifier(taxon.getNameIdentifier());
                persistedTaxon.setName(taxon.getName());
                persistedTaxon.setAuthorship(taxon.getAuthorship());
                persistedTaxon.setRank(taxon.getRank());
                persistedTaxon.setGenus(taxon.getGenus());
                persistedTaxon.setInfraGenericEpithet(taxon
                        .getInfraGenericEpithet());
                persistedTaxon.setSpecificEpithet(taxon.getSpecificEpithet());
                persistedTaxon.setInfraSpecificEpithet(taxon
                        .getInfraSpecificEpithet());
                persistedTaxon.setStatus(taxon.getStatus());
                persistedTaxon.setAccordingTo(taxon.getAccordingTo());
                persistedTaxon.setKingdom(taxon.getKingdom());
                persistedTaxon.setPhylum(taxon.getPhylum());
                persistedTaxon.setClazz(taxon.getClazz());
                persistedTaxon.setOrder(taxon.getOrder());
                persistedTaxon.setFamily(taxon.getFamily());
                persistedTaxon.setNomenclaturalCode(taxon
                        .getNomenclaturalCode());
                persistedTaxon.setProtologue(taxon.getProtologue());
                persistedTaxon.setProtologueMicroReference(taxon
                        .getProtologueMicroReference());
            }
            Annotation annotation = createAnnotation(persistedTaxon,
                    RecordType.Taxon, AnnotationCode.Update,
                    AnnotationType.Info);
            persistedTaxon.getAnnotations().add(annotation);
            logger.info("Overwriting taxon " + persistedTaxon.getIdentifier());
            return persistedTaxon;
        }
    }
}
