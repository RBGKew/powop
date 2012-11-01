package org.emonocot.job.dwc.taxon;

import org.emonocot.harvest.common.TaxonRelationshipResolver;
import org.emonocot.job.dwc.DarwinCoreProcessor;
import org.emonocot.model.Annotation;
import org.emonocot.model.Taxon;
import org.emonocot.model.constants.AnnotationCode;
import org.emonocot.model.constants.AnnotationType;
import org.emonocot.model.constants.RecordType;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;

/**
 * 
 * @author ben
 * 
 */
public class ImportingValidator extends DarwinCoreProcessor<Taxon> {
	/**
     *
     */
	private Logger logger = LoggerFactory.getLogger(ImportingValidator.class);

	/**
     *
     */
	private TaxonRelationshipResolver taxonRelationshipResolver;

	/**
	 * @param resolver
	 *            set the taxon relationship resolver
	 */
	@Autowired
	public final void setTaxonRelationshipResolver(
			final TaxonRelationshipResolver resolver) {
		this.taxonRelationshipResolver = resolver;
	}

	/**
	 * @param taxon
	 *            a taxon object
	 * @throws Exception
	 *             if something goes wrong
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
			Annotation annotation = createAnnotation(taxon, RecordType.Taxon,
					AnnotationCode.Create, AnnotationType.Info);
			taxon.getAnnotations().add(annotation);
			taxon.setAuthority(getSource());
			logger.info("Adding taxon " + taxon.getIdentifier());
			return taxon;
		} else {
			if (!persistedTaxon.getAuthority().getIdentifier()
					.equals(this.getSource().getIdentifier())) {
				// Skip taxa which do not belong to this authority
				return null;
			} else {
				taxonRelationshipResolver.bind(persistedTaxon);
				taxonRelationshipResolver.updateTaxon(persistedTaxon);
				if ((persistedTaxon.getModified() != null && taxon
						.getModified() != null)
						&& !persistedTaxon.getModified().isBefore(
								taxon.getModified())) {
				} else {
					taxonRelationshipResolver.bind(persistedTaxon);
					persistedTaxon.setModified(taxon.getModified());
					persistedTaxon.setCreated(taxon.getCreated());
					persistedTaxon.setSource(taxon.getSource());
					persistedTaxon.setScientificNameID(taxon.getScientificNameID());
					persistedTaxon.setScientificName(taxon.getScientificName());
					persistedTaxon.setScientificNameAuthorship(taxon.getScientificNameAuthorship());
					persistedTaxon.setTaxonRank(taxon.getTaxonRank());
					persistedTaxon.setGenus(taxon.getGenus());
					persistedTaxon.setSubgenus(taxon.getSubgenus());
					persistedTaxon.setSpecificEpithet(taxon.getSpecificEpithet());
					persistedTaxon.setInfraspecificEpithet(taxon.getInfraspecificEpithet());
					persistedTaxon.setTaxonomicStatus(taxon.getTaxonomicStatus());
					persistedTaxon.setNameAccordingTo(taxon.getNameAccordingTo());
					persistedTaxon.setKingdom(taxon.getKingdom());
					persistedTaxon.setPhylum(taxon.getPhylum());
					persistedTaxon.setClazz(taxon.getClazz());
					persistedTaxon.setOrder(taxon.getOrder());
					persistedTaxon.setFamily(taxon.getFamily());
					persistedTaxon.setSubfamily(taxon.getSubfamily());
					persistedTaxon.setTribe(taxon.getTribe());
					persistedTaxon.setSubtribe(taxon.getSubtribe());
					persistedTaxon.setNomenclaturalCode(taxon
							.getNomenclaturalCode());
					persistedTaxon.setNamePublishedIn(taxon.getNamePublishedIn());
					
					// Allow the relationships to either be re-asserted or dropped
					persistedTaxon.setAcceptedNameUsage(null);
					persistedTaxon.setParentNameUsage(null);
				}
				Annotation annotation = createAnnotation(persistedTaxon,
						RecordType.Taxon, AnnotationCode.Update,
						AnnotationType.Info);
				persistedTaxon.getAnnotations().add(annotation);
				logger.info("Overwriting taxon "
						+ persistedTaxon.getIdentifier());
				return persistedTaxon;
			}
		}
	}
}
