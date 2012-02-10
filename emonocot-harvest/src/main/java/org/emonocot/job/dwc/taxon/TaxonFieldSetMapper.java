package org.emonocot.job.dwc.taxon;

import java.text.ParseException;
import java.util.HashMap;
import java.util.Map;

import org.emonocot.api.ReferenceService;
import org.emonocot.api.TaxonService;
import org.emonocot.harvest.common.TaxonRelationshipResolver;
import org.emonocot.harvest.common.TaxonRelationship;
import org.emonocot.harvest.common.TaxonRelationshipResolverImpl;
import org.emonocot.job.dwc.DarwinCoreFieldSetMapper;
import org.emonocot.model.common.Annotation;
import org.emonocot.model.common.AnnotationCode;
import org.emonocot.model.common.AnnotationType;
import org.emonocot.model.common.RecordType;
import org.emonocot.model.reference.Reference;
import org.emonocot.model.taxon.NomenclaturalCode;
import org.emonocot.model.taxon.Rank;
import org.emonocot.model.taxon.RankConverter;
import org.emonocot.model.taxon.Taxon;
import org.emonocot.model.taxon.TaxonomicStatus;
import org.emonocot.model.taxon.TaxonomicStatusConverter;
import org.gbif.dwc.terms.ConceptTerm;
import org.gbif.dwc.terms.DcTerm;
import org.gbif.dwc.terms.DwcTerm;
import org.gbif.dwc.terms.UnknownTerm;
import org.joda.time.DateTime;
import org.joda.time.format.ISODateTimeFormat;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.batch.core.ChunkListener;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.convert.ConversionException;
import org.springframework.core.convert.converter.Converter;
import org.springframework.format.Parser;
import org.springframework.format.datetime.joda.DateTimeParser;
import org.springframework.validation.BindException;
import org.tdwg.voc.TaxonRelationshipTerm;

/**
 *
 * @author ben
 *
 */
public class TaxonFieldSetMapper extends
        DarwinCoreFieldSetMapper<Taxon> implements ChunkListener {

   /**
    *
    */
    private Logger logger
        = LoggerFactory.getLogger(TaxonFieldSetMapper.class);

    /**
     *
     */
    private Converter<String, Rank> rankConverter = new RankConverter();

    /**
     *
     */
    private Converter<String, TaxonomicStatus> taxonomicStatusConverter
        = new TaxonomicStatusConverter();

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
    public TaxonFieldSetMapper() {
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
                taxon.setNameIdentifier(value);
                break;
            case scientificName:
                taxon.setName(value);
                break;
            case scientificNameAuthorship:
                taxon.setAuthorship(value);
                break;
            case taxonRank:
                try {
                    Rank rank = rankConverter.convert(value
                            .toUpperCase().replaceAll(" ", "_"));
                    taxon.setRank(rank);
                } catch (ConversionException ce) {
                    logger.error(ce.getMessage());
                }
                break;
            case taxonomicStatus:
                TaxonomicStatus status = taxonomicStatusConverter
                        .convert(value);
                taxon.setStatus(status);
                break;
            case parentNameUsageID:
                if (value != null && value.trim().length() != 0) {
                    TaxonRelationship taxonRelationship = new TaxonRelationship(
                            taxon, TaxonRelationshipTerm.IS_CHILD_TAXON_OF);
                    taxonRelationshipResolver.addTaxonRelationship(
                            taxonRelationship, value);
                }
                break;
            case acceptedNameUsageID:
                if (value != null && value.trim().length() != 0) {
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
                taxon.setInfraGenericEpithet(value);
                break;
            case specificEpithet:
                taxon.setSpecificEpithet(value);
                break;
            case infraspecificEpithet:
                taxon.setInfraSpecificEpithet(value);
                break;
            case nameAccordingTo:
                taxon.setAccordingTo(value);
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
                    taxon.setProtologue(protologue);
                }
            default:
                break;
            }
        }
        // Unknown Terms
        if (term instanceof UnknownTerm) {
            UnknownTerm unknownTerm = (UnknownTerm) term;
            if (unknownTerm.qualifiedName().equals(
                    "http://emonocot.org/protologueMicroReference")) {
                taxon.setProtologueMicroReference(value);
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
       boundTaxa = new HashMap<String, Taxon>();
       boundReferences = new HashMap<String, Reference>();
   }
}
