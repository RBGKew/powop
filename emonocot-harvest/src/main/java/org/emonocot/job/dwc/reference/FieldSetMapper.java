package org.emonocot.job.dwc.reference;

import java.text.ParseException;

import org.emonocot.api.TaxonService;
import org.emonocot.job.dwc.DarwinCoreFieldSetMapper;
import org.emonocot.job.dwc.taxon.CannotFindRecordException;
import org.emonocot.model.Reference;
import org.emonocot.model.Taxon;
import org.emonocot.model.constants.ReferenceType;
import org.gbif.dwc.terms.ConceptTerm;
import org.gbif.dwc.terms.DcTerm;
import org.gbif.dwc.terms.DwcTerm;
import org.gbif.dwc.terms.GbifTerm;
import org.gbif.dwc.terms.UnknownTerm;
import org.joda.time.DateTime;
import org.joda.time.format.ISODateTimeFormat;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.format.Parser;
import org.springframework.format.datetime.joda.DateTimeParser;
import org.springframework.validation.BindException;

/**
 *
 * @author ben
 *
 */
public class FieldSetMapper extends
        DarwinCoreFieldSetMapper<Reference> {

    /**
     *
     */
    public FieldSetMapper() {
        super(Reference.class);
    }

    /**
    *
    */
    private Logger logger = LoggerFactory
            .getLogger(FieldSetMapper.class);
    /**
    *
    */
   private Parser<DateTime> dateTimeParser
       = new DateTimeParser(ISODateTimeFormat.dateOptionalTimeParser());

    /**
     *
     */
    private TaxonService taxonService;

    /**
     *
     * @param newTaxonService Set the taxon service
     */
    @Autowired
    public final void setTaxonService(final TaxonService newTaxonService) {
        this.taxonService = newTaxonService;
    }

    @Override
    public final void mapField(final Reference object, final String fieldName,
            final String value) throws BindException {
        ConceptTerm term = getTermFactory().findTerm(fieldName);
        if (term instanceof DcTerm) {
            DcTerm dcTerm = (DcTerm) term;
            switch (dcTerm) {
            case creator:
                object.setCreator(value);
                break;
            case date:
                object.setDate(value);
                break;
            case modified:
                try {
                    object.setModified(dateTimeParser.parse(
                            value, null));
                } catch (ParseException pe) {
                    BindException be = new BindException(object, "target");
                    be.rejectValue("modified", "not.valid", pe.getMessage());
                    throw be;
                }
                break;
            case created:
                try {
                    object.setCreated(dateTimeParser.parse(
                            value, null));
                } catch (ParseException pe) {
                    BindException be = new BindException(object, "target");
                    be.rejectValue("created", "not.valid", pe.getMessage());
                    throw be;
                }
                break;
            case source:
                object.setSource(value);
                break;
            case type:
                try {
                    object.setType(ReferenceType.valueOf(value));
                } catch (IllegalArgumentException pe) {
                    BindException be = new BindException(object, "target");
                    be.rejectValue("type", "not.valid", pe.getMessage());
                    throw be;
                }
                break;
            case title:
                object.setTitle(value);
                break;
            case description:
                object.setDescription(value);
                break;
            case subject:
                object.setSubject(value);
                break;
            case bibliographicCitation:
                object.setBibliographicCitation(value);
                break;
            case identifier:                
                object.setIdentifier(value);
                break;
            default:
                break;
            }
        }
        // DwcTerms
        if (term instanceof DwcTerm) {
            DwcTerm dwcTerm = (DwcTerm) term;
            switch (dwcTerm) {
            case taxonID:
                Taxon taxon = taxonService.find(value);
                if (taxon == null) {
                    throw new CannotFindRecordException(value);
                } else {
                    object.getTaxa().add(taxon);
                }

                break;
            default:
                break;
            }
        }

        // Gbif
        if (term instanceof GbifTerm) {
            GbifTerm gbifTerm = (GbifTerm) term;
            switch (gbifTerm) {
            default:
                break;
            }
        }

    }
}
