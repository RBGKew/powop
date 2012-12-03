package org.emonocot.job.dwc.typeandspecimen;

import org.emonocot.job.dwc.read.NonOwnedFieldSetMapper;
import org.emonocot.model.TypeAndSpecimen;
import org.emonocot.model.constants.TypeDesignationType;
import org.gbif.dwc.terms.ConceptTerm;
import org.gbif.dwc.terms.DcTerm;
import org.gbif.dwc.terms.DwcTerm;
import org.gbif.dwc.terms.UnknownTerm;
import org.gbif.ecat.voc.Rank;
import org.gbif.ecat.voc.Sex;
import org.gbif.ecat.voc.TypeStatus;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.validation.BindException;

/**
 *
 * @author ben
 *
 */
public class FieldSetMapper extends  NonOwnedFieldSetMapper<TypeAndSpecimen> {

    /**
     *
     */
    public FieldSetMapper() {
        super(TypeAndSpecimen.class);
    }

    /**
    *
    */
    private Logger logger = LoggerFactory.getLogger(FieldSetMapper.class);


    @Override
    public final void mapField(final TypeAndSpecimen object, final String fieldName,
            final String value) throws BindException {
    	super.mapField(object, fieldName, value);
    	
        ConceptTerm term = getTermFactory().findTerm(fieldName);
        if (term instanceof DcTerm) {
            DcTerm dcTerm = (DcTerm) term;
            switch (dcTerm) {
            case bibliographicCitation:
                object.setBibliographicCitation(value);
                break;
            case source:
                object.setSource(value);
                break;           
            
            default:
                break;
            }
        }
        
        // DwcTerms
        if (term instanceof DwcTerm) {
            DwcTerm dwcTerm = (DwcTerm) term;

            switch (dwcTerm) {
            case catalogNumber:
            	object.setCatalogNumber(value);
            	break;
            case collectionCode:
            	object.setCollectionCode(value);
            	break;
            case decimalLatitude:
            	object.setDecimalLatitude(Double.valueOf(value));
            	break;
            case decimalLongitude:
            	object.setDecimalLongitude(Double.valueOf(value));
            	break;
            case institutionCode:
            	object.setInstitutionCode(value);
            	break;
            case locality:
            	object.setLocality(value);
            	break;
            case occurrenceID:
            	object.setIdentifier(value);
            	break;	
            case recordedBy:
            	object.setRecordedBy(value);
            	break;
            case scientificName:
            	object.setScientificName(value);
            	break;
            case sex:
            	object.setSex(Sex.valueOf(value));
            	break;
            case taxonRank:
            	object.setTaxonRank(Rank.valueOf(value));
            	break;
            case typeStatus:
            	object.setTypeStatus(TypeStatus.valueOf(value));
            	break;
            case verbatimEventDate:
            	object.setVerbatimEventDate(value);
            	break;
            case verbatimLatitude:
            	object.setVerbatimLatitude(value);
            	break;
            case verbatimLongitude:
            	object.setVerbatimLongitude(value);
            	break;	
            default:
            	break;
            }
        }
        
     // Unknown Terms
        if (term instanceof UnknownTerm) {
            UnknownTerm unknownTerm = (UnknownTerm) term;
            if (unknownTerm.qualifiedName().equals(
                    "http://rs.gbif.org/terms/1.0/typeDesignatedBy")) {
                object.setTypeDesignatedBy(value);
            } else if (unknownTerm.qualifiedName().equals(
                    "http://rs.gbif.org/terms/1.0/typeDesignationType")) {
                object.setTypeDesignationType(TypeDesignationType.valueOf(value));
            } else if (unknownTerm.qualifiedName().equals(
                    "http://rs.gbif.org/terms/1.0/verbatimLabel")) {
                object.setVerbatimLabel(value);
            } 
        }
    }
}
