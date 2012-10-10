package org.emonocot.job.dwc.taxon;

import java.text.ParseException;
import java.util.HashMap;
import java.util.Map;

import org.emonocot.api.ReferenceService;
import org.emonocot.api.TaxonService;
import org.emonocot.harvest.common.TaxonRelationship;
import org.emonocot.harvest.common.TaxonRelationshipResolver;
import org.emonocot.harvest.common.TaxonRelationshipResolverImpl;
import org.emonocot.job.dwc.DarwinCoreFieldSetMapper;
import org.emonocot.model.Annotation;
import org.emonocot.model.Reference;
import org.emonocot.model.Taxon;
import org.emonocot.model.constants.AnnotationCode;
import org.emonocot.model.constants.AnnotationType;
import org.emonocot.model.constants.RecordType;
import org.gbif.dwc.terms.ConceptTerm;
import org.gbif.dwc.terms.DcTerm;
import org.gbif.dwc.terms.DwcTerm;
import org.gbif.dwc.terms.UnknownTerm;
import org.gbif.ecat.voc.NomenclaturalCode;
import org.gbif.ecat.voc.Rank;
import org.gbif.ecat.voc.TaxonomicStatus;
import org.joda.time.DateTime;
import org.joda.time.format.ISODateTimeFormat;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.batch.core.ChunkListener;
import org.springframework.batch.core.JobExecution;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.format.Parser;
import org.springframework.format.datetime.joda.DateTimeParser;
import org.springframework.validation.BindException;
import org.tdwg.voc.TaxonRelationshipTerm;

/**
 *
 * @author ben
 *
 */
public class FieldSetMapper extends
        DarwinCoreFieldSetMapper<Taxon> implements ChunkListener {

   /**
    *
    */
    private Logger logger
        = LoggerFactory.getLogger(FieldSetMapper.class);
    
    /**
     *
     */
    private boolean resolveRelationships = false;

    /**
     *
     */
    private TaxonRelationshipResolver taxonRelationshipResolver = new TaxonRelationshipResolverImpl();

    /**
     *
     */
    private Parser<DateTime> dateTimeParser
        = new DateTimeParser(ISODateTimeFormat.dateOptionalTimeParser());

    /**
     *
     */
    private Map<String, Taxon> boundTaxa = new HashMap<String, Taxon>();

    /**
     *
     */
    private TaxonService taxonService;

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
     *
     * @param newTaxonService Set the taxon service
     */
    @Autowired
    public final void setTaxonService(final TaxonService newTaxonService) {
        this.taxonService = newTaxonService;
    }

    /**
     * @param newReferenceService Set the reference service
     */
    @Autowired
    public final void setReferenceService(
            final ReferenceService newReferenceService) {
        this.referenceService = newReferenceService;
    }

    /**
     * @param resolver Set the taxon relationship resolver
     */
    @Autowired
    public final void setTaxonRelationshipResolver(
            final TaxonRelationshipResolver resolver) {
        this.taxonRelationshipResolver = resolver;
    }

    /**
     *
     * @param taxon the object to map onto
     * @param fieldName the name of the field
     * @param value the value to map
     * @throws BindException if there is a problem mapping
     *         the value to the object
     */
    @Override
    public final void mapField(final Taxon taxon, final String fieldName,
            final String value) throws BindException {
        ConceptTerm term = getTermFactory().findTerm(fieldName);
        logger.info("Mapping " + fieldName + " " + " " + value + " to "
                + taxon);
        if (term instanceof DcTerm) {
            DcTerm dcTerm = (DcTerm) term;
            switch (dcTerm) {
            case modified:
                try {
                    taxon.setModified(dateTimeParser.parse(
                            value, null));
                } catch (ParseException pe) {
                    BindException be = new BindException(taxon, "target");
                    be.rejectValue("modified", "not.valid", pe.getMessage());
                    throw be;
                }
                break;
            case created:
                try {
                    taxon.setCreated(dateTimeParser.parse(
                            value, null));
                } catch (ParseException pe) {
                    BindException be = new BindException(taxon, "target");
                    be.rejectValue("created", "not.valid", pe.getMessage());
                    throw be;
                }
            case source:
                taxon.setSource(value);
            default:
                break;
            }
        }
        // DwcTerms
        if (term instanceof DwcTerm) {
            DwcTerm dwcTerm = (DwcTerm) term;
            switch (dwcTerm) {
            case taxonID:
                taxon.setIdentifier(value);
                break;
            case scientificNameID:
                taxon.setScientificNameID(value);
                break;
            case scientificName:
                taxon.setScientificName(value);
                break;
            case scientificNameAuthorship:
                taxon.setScientificNameAuthorship(value);
                break;
            case taxonRank:
                try {                    
                    taxon.setTaxonRank(Rank.valueOf(value));
                } catch (IllegalArgumentException iae) {
                    logger.error(iae.getMessage());
                }
                break;
            case taxonomicStatus:
                try {
                    taxon.setTaxonomicStatus(TaxonomicStatus.valueOf(value));
                } catch (IllegalArgumentException iae) {
                    logger.error(iae.getMessage());
                }
                break;
            case parentNameUsageID:
                if (resolveRelationships && value != null && value.trim().length() != 0) {
                    TaxonRelationship taxonRelationship = new TaxonRelationship(
                            taxon, TaxonRelationshipTerm.IS_CHILD_TAXON_OF);
                    taxonRelationshipResolver.addTaxonRelationship(
                            taxonRelationship, value);
                }
                break;
            case acceptedNameUsageID:
                if (resolveRelationships && value != null && value.trim().length() != 0) {
                    TaxonRelationship taxonRelationship = new TaxonRelationship(
                            taxon, TaxonRelationshipTerm.IS_SYNONYM_FOR);
                    taxonRelationshipResolver.addTaxonRelationship(
                            taxonRelationship, value);
                }
                break;
            case genus:
                taxon.setGenus(value);
                break;
            case subgenus:
                taxon.setSubgenus(value);
                break;
            case specificEpithet:
                taxon.setSpecificEpithet(value);
                break;
            case infraspecificEpithet:
                taxon.setInfraspecificEpithet(value);
                break;
            case nameAccordingToID:
            	if (value != null && value.trim().length() > 0) {
                    Reference accordingTo = null;
                    if (boundReferences.containsKey(value)) {
                    	accordingTo = boundReferences.get(value);
                    } else {
                    	accordingTo = referenceService.find(value);
                        if (accordingTo == null) {
                        	accordingTo = new Reference();
                        	accordingTo.setIdentifier(value);
                            Annotation annotation = createAnnotation(
                            		accordingTo, RecordType.Reference,
                                    AnnotationCode.Create, AnnotationType.Info);
                            accordingTo.getAnnotations().add(annotation);
                        }
                        boundReferences.put(accordingTo.getIdentifier(),
                        		accordingTo);
                    }
                    taxon.setNameAccordingTo(accordingTo);
                }
                break;
            case kingdom:
                taxon.setKingdom(value);
                break;
            case phylum:
                taxon.setPhylum(value);
                break;
            case classs:
                taxon.setClazz(value);
                break;
            case order:
                taxon.setOrder(value);
                break;
            case family:
                taxon.setFamily(value);
                break;
            case nomenclaturalCode:
                taxon.setNomenclaturalCode(NomenclaturalCode.valueOf(value));
                break;
            case namePublishedInID:
                if (value != null && value.trim().length() > 0) {
                    Reference protologue = null;
                    if (boundReferences.containsKey(value)) {
                        protologue = boundReferences.get(value);
                    } else {
                        protologue = referenceService.find(value);
                        if (protologue == null) {
                            protologue = new Reference();
                            protologue.setIdentifier(value);
                            Annotation annotation = createAnnotation(
                                    protologue, RecordType.Reference,
                                    AnnotationCode.Create, AnnotationType.Info);
                            protologue.getAnnotations().add(annotation);
                        }
                        boundReferences.put(protologue.getIdentifier(),
                                protologue);
                    }
                    taxon.setNamePublishedIn(protologue);
                }
            default:
                break;
            }
        }
        // Unknown Terms
        if (term instanceof UnknownTerm) {
            UnknownTerm unknownTerm = (UnknownTerm) term;
            if (unknownTerm.qualifiedName().equals(
                    "http://emonocot.org/subfamily")) {
                taxon.setSubfamily(value);
            } else if (unknownTerm.qualifiedName().equals(
                    "http://emonocot.org/tribe")) {
                taxon.setTribe(value);
            } else if (unknownTerm.qualifiedName().equals(
                    "http://emonocot.org/subtribe")) {
                taxon.setSubtribe(value);
            } 
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
       JobExecution jobExecution = getStepExecution().getJobExecution();
       if (jobExecution.getExecutionContext().containsKey("taxon.processing.mode")) {
        String taxonProcessingMode = jobExecution.getExecutionContext()
           .getString("taxon.processing.mode");
         if(taxonProcessingMode.equals("IMPORT_TAXA")) {
             resolveRelationships = true;
         } else {
             resolveRelationships = false;
         }
       }

       boundTaxa = new HashMap<String, Taxon>();
       boundReferences = new HashMap<String, Reference>();
   }
}
