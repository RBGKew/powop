/*
 * This is eMonocot, a global online biodiversity information resource.
 *
 * Copyright © 2011–2015 The Board of Trustees of the Royal Botanic Gardens, Kew and The University of Oxford
 *
 * eMonocot is free software: you can redistribute it and/or modify it under the terms of the
 * GNU Affero General Public License as published by the Free Software Foundation, either version 3
 * of the License, or (at your option) any later version.
 *
 * eMonocot is distributed in the hope that it will be useful, but WITHOUT ANY WARRANTY; without even
 * the implied warranty of MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 * GNU Affero General Public License for more details.
 *
 * The complete text of the GNU Affero General Public License is in the source repository as the file
 * ‘COPYING’.  It is also available from <http://www.gnu.org/licenses/>.
 */
package org.emonocot.job.dwc.taxon;

import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;

import org.emonocot.api.ReferenceService;
import org.emonocot.job.dwc.exception.NoIdentifierException;
import org.emonocot.job.dwc.read.DarwinCoreProcessor;
import org.emonocot.model.Annotation;
import org.emonocot.model.Reference;
import org.emonocot.model.Taxon;
import org.emonocot.model.constants.AnnotationCode;
import org.emonocot.model.constants.AnnotationType;
import org.emonocot.model.constants.RecordType;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.batch.core.ChunkListener;
import org.springframework.batch.core.ItemWriteListener;
import org.springframework.beans.factory.annotation.Autowired;

/**
 * 
 * @author ben
 * 
 */
public class Processor extends DarwinCoreProcessor<Taxon> implements ChunkListener, ItemWriteListener<Taxon> {
	
	private Set<TaxonRelationship> taxonRelationships = new HashSet<TaxonRelationship>();
	
	private Map<String, Reference> boundReferences = new HashMap<String, Reference>();
	
	private Map<String, Taxon> boundTaxa = new HashMap<String,Taxon>();

	private Logger logger = LoggerFactory.getLogger(Processor.class);
	
	private ReferenceService referenceService;
	
	@Autowired
	public void setReferenceService(ReferenceService referenceService) {
	    this.referenceService = referenceService;
	}

	/**
	 * @param t
	 *            a taxon object
	 * @throws Exception
	 *             if something goes wrong
	 * @return Taxon a taxon object
	 */
	public Taxon doProcess(Taxon t) throws Exception {
		logger.info("Processing " + t.getIdentifier());
		
		if (t.getIdentifier() == null) {
			throw new NoIdentifierException(t);
		}
		
		/**
		 * replace references with persisted objects or new ones
		 */
		t.setNameAccordingTo(resolveReference(t.getNameAccordingTo()));
		t.setNamePublishedIn(resolveReference(t.getNamePublishedIn()));
		
		Taxon persisted = getTaxonService().find(t.getIdentifier());
		
		if (persisted == null) {
			// Taxon is new
			bindRelationships(t,t);
			validate(t);
			Annotation annotation = createAnnotation(t, RecordType.Taxon, AnnotationCode.Create, AnnotationType.Info);
			t.getAnnotations().add(annotation);
			t.setAuthority(getSource());
			logger.info("Adding taxon " + t);
			return t;
		} else if(boundTaxa.containsKey(t.getIdentifier())) { 
		    logger.error(t.getIdentifier() + " was found earlier in this archive");
		    createAnnotation(boundTaxa.get(t.getIdentifier()), RecordType.Taxon, AnnotationCode.AlreadyProcessed, AnnotationType.Warn);
		    return null;
		} else {
			checkAuthority(RecordType.Taxon, t, persisted.getAuthority());
			if (skipUnmodified
					&& ((persisted.getModified() != null && t.getModified() != null) && !persisted
							.getModified().isBefore(t.getModified()))) {
				bindTaxon(persisted);
				replaceAnnotation(persisted, AnnotationType.Info,
						AnnotationCode.Skipped);
			} else {
				bindRelationships(t, persisted);
				persisted.setAccessRights(t.getAccessRights());
				persisted.setCreated(t.getCreated());
				persisted.setLicense(t.getLicense());
				persisted.setModified(t.getModified());
				persisted.setRights(t.getRights());
				persisted.setRightsHolder(t.getRightsHolder());
				persisted
						.setBibliographicCitation(t.getBibliographicCitation());
				persisted.setClazz(t.getClazz());
				persisted.setFamily(t.getFamily());
				persisted.setGenus(t.getGenus());
				persisted.setInfraspecificEpithet(t.getInfraspecificEpithet());
				persisted.setKingdom(t.getKingdom());
				persisted.setNameAccordingTo(t.getNameAccordingTo());
				persisted.setNamePublishedIn(t.getNamePublishedIn());
				persisted
						.setNamePublishedInString(t.getNamePublishedInString());
				persisted.setNamePublishedInYear(t.getNamePublishedInYear());
				persisted.setNomenclaturalCode(t.getNomenclaturalCode());
				persisted.setNomenclaturalStatus(t.getNomenclaturalStatus());
				persisted.setOrder(t.getOrder());
				persisted.setPhylum(t.getPhylum());
				persisted.setScientificName(t.getScientificName());
				persisted.setScientificNameAuthorship(t
						.getScientificNameAuthorship());
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
				persisted.setUri(t.getUri());
				validate(t);

				replaceAnnotation(persisted, AnnotationType.Info,
						AnnotationCode.Update);
			}
			logger.info("Overwriting taxon " + persisted);
			return persisted;

		}
	}

	private void bindTaxon(Taxon persisted) {
		boundTaxa.put(persisted.getIdentifier(), persisted);
	}

	private void bindRelationships(Taxon t, Taxon u) {
		bindTaxon(u);
		if(t.getParentNameUsage() != null) {
			this.taxonRelationships.add(new TaxonRelationship(u, TaxonRelationshipType.parent, t.getParentNameUsage().getIdentifier(), t.getParentNameUsage().getScientificName()));
		}
		if(t.getAcceptedNameUsage() != null) {
			this.taxonRelationships.add(new TaxonRelationship(u, TaxonRelationshipType.accepted, t.getAcceptedNameUsage().getIdentifier(), t.getAcceptedNameUsage().getScientificName()));
		}
		if(t.getOriginalNameUsage() != null) {
			this.taxonRelationships.add(new TaxonRelationship(u, TaxonRelationshipType.original, t.getOriginalNameUsage().getIdentifier(), t.getOriginalNameUsage().getScientificName()));
		}
		
		u.setParentNameUsage(null);
		u.setAcceptedNameUsage(null);
		u.setOriginalNameUsage(null);
	}

	@Override
	public void beforeChunk() {
		boundTaxa = new HashMap<String,Taxon>();
		taxonRelationships = new HashSet<TaxonRelationship>();
		boundReferences = new HashMap<String, Reference>();
	}

	@Override
	public void afterChunk() {}

	private Taxon resolveTaxon(String identifier, String scientificName) {
        if (boundTaxa.containsKey(identifier)) {
            logger.info("Found taxon " + scientificName + " with identifier " + identifier + " from cache returning taxon with id " + boundTaxa.get(identifier).getId());
            return boundTaxa.get(identifier);
        } else {
            Taxon taxon = getTaxonService().find(identifier);

            if (taxon == null) {
                taxon = new Taxon();
                Annotation annotation = createAnnotation(taxon, RecordType.Taxon,
    					AnnotationCode.Create, AnnotationType.Info);
    			taxon.getAnnotations().add(annotation);
                taxon.setAuthority(getSource());
                taxon.setIdentifier(identifier);
                taxon.setScientificName(scientificName);
                logger.info("Didn't find taxon " + scientificName + " with identifier " + identifier + " from service returning new taxon");
                bindTaxon(taxon);
              } else {
                  logger.info("Found taxon " + scientificName + "with identifier " + identifier + " from service returning taxon with id " + taxon.getId());
                  bindTaxon(taxon);
              }
              return taxon;
        }
    }
	
    /**
    *
    * @param object
    *            Set the text content object
    * @param value
    *            the source of the reference to resolve
    */
   private Reference resolveReference(Reference reference) {
		if (reference == null) {
			return null;
		} else {
			String identifier = reference.getIdentifier();
			if (identifier == null || identifier.trim().length() == 0) {
				// there is not citation identifier
				return null;
			} else {
				if (boundReferences.containsKey(identifier)) {
					return boundReferences.get(identifier);
				} else {
					Reference r = referenceService.find(identifier);
					if (r == null) {
						r = new Reference();
						r.setIdentifier(identifier);
						Annotation annotation = super.createAnnotation(r,
								RecordType.Reference, AnnotationCode.Create,
								AnnotationType.Info);
						r.getAnnotations().add(annotation);
						r.setAuthority(getSource());
					}
					boundReferences.put(identifier, r);
					return r;
				}
			}
		}
   }

	@Override
	public void beforeWrite(List<? extends Taxon> items) {
		logger.info("Before Write");
        for (TaxonRelationship taxonRelationship : taxonRelationships) {
        	Taxon to = resolveTaxon(taxonRelationship.getToIdentifier(), taxonRelationship.getToScientificName());
        	Taxon from = taxonRelationship.getFrom();
            switch(taxonRelationship.getTerm()) {
            case original:
            	from.setOriginalNameUsage(to);
            	break;
            case accepted:
            	from.setAcceptedNameUsage(to);
            	break;
            case parent:
            	from.setParentNameUsage(to);
            	break;
            }
        }
	}

	@Override
	public void afterWrite(List<? extends Taxon> items) {
		
	}

	@Override
	public void onWriteError(Exception exception, List<? extends Taxon> items) {
		
	}
}
