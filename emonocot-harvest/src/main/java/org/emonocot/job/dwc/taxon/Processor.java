package org.emonocot.job.dwc.taxon;

import org.emonocot.harvest.common.TaxonRelationshipResolver;
import org.emonocot.job.dwc.exception.NoIdentifierException;
import org.emonocot.job.dwc.read.DarwinCoreProcessor;
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
public class Processor extends DarwinCoreProcessor<Taxon> {
	/**
     *
     */
	private Logger logger = LoggerFactory.getLogger(Processor.class);

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
	 * @param t
	 *            a taxon object
	 * @throws Exception
	 *             if something goes wrong
	 * @return Taxon a taxon object
	 */
	public final Taxon process(final Taxon t) throws Exception {
		logger.info("Processing " + t.getIdentifier());
		
		if (t.getIdentifier() == null) {
			throw new NoIdentifierException(t);
		}
		
		Taxon persisted = getTaxonService().find(t.getIdentifier());
		
		if (persisted == null) {
			taxonRelationshipResolver.bind(t);
			Annotation annotation = createAnnotation(t, RecordType.Taxon,
					AnnotationCode.Create, AnnotationType.Info);
			t.getAnnotations().add(annotation);
			t.setAuthority(getSource());
			logger.info("Adding taxon " + t.getIdentifier());
			return t;
		} else {
			if (!persisted.getAuthority().getIdentifier()
					.equals(this.getSource().getIdentifier())) {
				// Skip taxa which do not belong to this authority
				logger.info("Taxon " + persisted.getIdentifier() + " belongs to " + this.getSource().getIdentifier() +  ", skipping");
				return null;
			} else {
				taxonRelationshipResolver.bind(persisted);
				taxonRelationshipResolver.updateTaxon(persisted);
				if ((persisted.getModified() != null && t
						.getModified() != null)
						&& !persisted.getModified().isBefore(
								t.getModified())) {
					replaceAnnotation(persisted, AnnotationType.Info, AnnotationCode.Skipped);
				} else {
					persisted.setAccessRights(t.getAccessRights());
	                persisted.setCreated(t.getCreated());
	                persisted.setLicense(t.getLicense());
	                persisted.setModified(t.getModified());
	                persisted.setRights(t.getRights());
	                persisted.setRightsHolder(t.getRightsHolder());
	                
	                persisted.setAcceptedNameUsage(null); // Re-established at end of chunk by taxonRelationshipResolver
					persisted.setBibliographicCitation(t.getBibliographicCitation());
					persisted.setClazz(t.getClazz());
					persisted.setFamily(t.getFamily());
					persisted.setGenus(t.getGenus());
					persisted.setInfraspecificEpithet(t.getInfraspecificEpithet());
					persisted.setKingdom(t.getKingdom());
					persisted.setNameAccordingTo(t.getNameAccordingTo());
					persisted.setNamePublishedIn(t.getNamePublishedIn());
					persisted.setNamePublishedInString(t.getNamePublishedInString());
					persisted.setNamePublishedInYear(t.getNamePublishedInYear());
					persisted.setNomenclaturalCode(t.getNomenclaturalCode());
					persisted.setNomenclaturalStatus(t.getNomenclaturalStatus());
					persisted.setOrder(t.getOrder());
					persisted.setOriginalNameUsage(null); // Re-established at end of chunk by taxonRelationshipResolver
					persisted.setParentNameUsage(null); // Re-established at end of chunk by taxonRelationshipResolver
					persisted.setPhylum(t.getPhylum());
					persisted.setScientificName(t.getScientificName());
					persisted.setScientificNameAuthorship(t.getScientificNameAuthorship());
					persisted.setScientificNameID(t.getScientificNameID());
					persisted.setSource(t.getSource());
					persisted.setSpecificEpithet(t.getSpecificEpithet());
					persisted.setSubfamily(t.getSubfamily());
					persisted.setSubgenus(t.getSubgenus());
					persisted.setSubtribe(t.getSubtribe());
					persisted.setTaxonomicStatus(t.getTaxonomicStatus());
					persisted.setTaxonRank(t.getTaxonRank());
					persisted.setTaxonRemarks(t.getTaxonRemarks());
					persisted.setTribe(t.getTribe());
					persisted.setTaxonRank(t.getTaxonRank());
					replaceAnnotation(persisted, AnnotationType.Info, AnnotationCode.Update);					
				}
				

				logger.info("Overwriting taxon "
						+ persisted.getIdentifier());
				return persisted;
			}
		}
	}
}
