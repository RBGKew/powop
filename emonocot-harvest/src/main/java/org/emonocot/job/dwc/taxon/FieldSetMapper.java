package org.emonocot.job.dwc.taxon;

import java.util.HashMap;

import org.emonocot.api.ReferenceService;
import org.emonocot.api.job.EmonocotTerm;
import org.emonocot.job.dwc.read.BaseDataFieldSetMapper;
import org.emonocot.model.Annotation;
import org.emonocot.model.Reference;
import org.emonocot.model.Taxon;
import org.emonocot.model.constants.AnnotationCode;
import org.emonocot.model.constants.AnnotationType;
import org.emonocot.model.constants.RecordType;
import org.gbif.dwc.terms.ConceptTerm;
import org.gbif.dwc.terms.DcTerm;
import org.gbif.dwc.terms.DwcTerm;
import org.gbif.ecat.voc.NomenclaturalCode;
import org.gbif.ecat.voc.NomenclaturalStatus;
import org.gbif.ecat.voc.Rank;
import org.gbif.ecat.voc.TaxonomicStatus;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.batch.core.ChunkListener;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.convert.ConversionException;
import org.springframework.validation.BindException;

/**
 *
 * @author ben
 *
 */
public class FieldSetMapper extends BaseDataFieldSetMapper<Taxon> implements ChunkListener {

   /**
    *
    */
    private Logger logger
        = LoggerFactory.getLogger(FieldSetMapper.class);

    /**
     *
     */
    private HashMap<String, Reference> boundReferences;

    /**
     *
     */
    private ReferenceService referenceService;

    /**
     *
     */
    public FieldSetMapper() {
        super(Taxon.class);
    }

    /**
     * @param newReferenceService Set the reference service
     */
    @Autowired
    public final void setReferenceService(final ReferenceService newReferenceService) {
        this.referenceService = newReferenceService;
    }

    /**
     *
     * @param object the object to map onto
     * @param fieldName the name of the field
     * @param value the value to map
     * @throws BindException if there is a problem mapping
     *         the value to the object
     */
    @Override
    public final void mapField(final Taxon object, final String fieldName,
            final String value) throws BindException {
    	super.mapField(object, fieldName, value);
    	
        ConceptTerm term = getTermFactory().findTerm(fieldName);
        logger.info("Mapping " + fieldName + " " + " " + value + " to "
                + object);
        if (term instanceof DcTerm) {
            DcTerm dcTerm = (DcTerm) term;
            switch (dcTerm) {
            case bibliographicCitation:
            	object.setBibliographicCitation(value);
            	break;
            case source:
                object.setSource(value);
            default:
                break;
            }
        }
        // DwcTerms
        if (term instanceof DwcTerm) {
            DwcTerm dwcTerm = (DwcTerm) term;
            switch (dwcTerm) {
            case acceptedNameUsageID:
                if (value != null && value.trim().length() != 0) {
                    Taxon taxon = new Taxon();
                    taxon.setIdentifier(value);
                    object.setAcceptedNameUsage(taxon);
                }
                break;
            case classs:
                object.setClazz(value);
                break;
            case family:
                object.setFamily(value);
                break;
            case genus:
                object.setGenus(value);
                break;
            case infraspecificEpithet:
                object.setInfraspecificEpithet(value);
                break;
            case kingdom:
                object.setKingdom(value);
                break; 
            case nameAccordingToID:
                object.setNameAccordingTo(handleReference(value));
                break;  
            case namePublishedInID:
            	object.setNamePublishedIn(handleReference(value));
                break;
            case namePublishedIn:
            	object.setNamePublishedInString(value);
            	break;
            case namePublishedInYear:
            	object.setNamePublishedInYear(conversionService.convert(value, Integer.class));
            	break;
            case nomenclaturalCode:
                object.setNomenclaturalCode(conversionService.convert(value, NomenclaturalCode.class));
                break;
            case nomenclaturalStatus:
            	object.setNomenclaturalStatus(conversionService.convert(value, NomenclaturalStatus.class));
                break;
            case order:
                object.setOrder(value);
                break;
            case originalNameUsageID:
            	if (value != null && value.trim().length() != 0) {
            		Taxon taxon = new Taxon();
                    taxon.setIdentifier(value);
                    object.setOriginalNameUsage(taxon);
                }
            	break;
            case parentNameUsageID:
                if (value != null && value.trim().length() != 0) {
                	Taxon taxon = new Taxon();
                    taxon.setIdentifier(value);
                    object.setParentNameUsage(taxon);
                }
                break;
            case phylum:
                object.setPhylum(value);
                break;
            case scientificName:
                object.setScientificName(value);
                break;
            case scientificNameAuthorship:
                object.setScientificNameAuthorship(value);
                break;
            case scientificNameID:
                object.setScientificNameID(value);
                break;
            case specificEpithet:
                object.setSpecificEpithet(value);
                break;
            case subgenus:
                object.setSubgenus(value);
                break;
            case taxonID:
                object.setIdentifier(value);
                break;
            case taxonomicStatus:
                try {
                    object.setTaxonomicStatus(conversionService.convert(value, TaxonomicStatus.class));
                } catch (ConversionException ce) {
                    logger.error(ce.getMessage());
                }
                break;
            case taxonRank:
                try {                    
                    object.setTaxonRank(conversionService.convert(value, Rank.class));
                } catch (ConversionException ce) {
                    logger.error(ce.getMessage());
                }
                break;
            case taxonRemarks:
            	object.setTaxonRemarks(value);
            	break;
            case verbatimTaxonRank:
            	object.setVerbatimTaxonRank(value);
            	break;
            default:
                break;
            }
        }
        // eMonocot Terms
        if (term instanceof EmonocotTerm) {
            EmonocotTerm eMonocotTerm = (EmonocotTerm) term;
            switch(eMonocotTerm) {
            case subfamily:
            	object.setSubfamily(value);
            	break;
            case subtribe:
            	object.setSubtribe(value);
            	break;
            case tribe:
            	object.setTribe(value);
            	break;
            default:
            	break;
            }            
        }
    }

    private Reference handleReference(String value) {
		if (value != null && value.trim().length() > 0) {
			Reference reference = null;
			if (boundReferences.containsKey(value)) {
				return boundReferences.get(value);
			} else {
				reference = referenceService.find(value);
				if (reference == null) {
					reference = new Reference();
					reference.setIdentifier(value);
					Annotation annotation = createAnnotation(reference,
							RecordType.Reference, AnnotationCode.Create,
							AnnotationType.Info);
					reference.getAnnotations().add(annotation);
				}
				boundReferences.put(reference.getIdentifier(), reference);
				return reference;
			}
		} else {
			return null;
		}
	}

	/**
    *
    */
   public final void afterChunk() {
       logger.info("After Chunk");
   }

   /**
    *
    */
   public final void beforeChunk() {
       logger.info("Before Chunk");       

       boundReferences = new HashMap<String, Reference>();
   }
}
