package org.emonocot.job.dwc.description;

import java.text.ParseException;

import org.emonocot.job.dwc.DarwinCoreFieldSetMapper;
import org.emonocot.job.dwc.taxon.CannotFindRecordException;
import org.emonocot.model.description.Feature;
import org.emonocot.model.description.TextContent;
import org.emonocot.model.taxon.NomenclaturalCode;
import org.emonocot.model.taxon.Rank;
import org.emonocot.model.taxon.Taxon;
import org.emonocot.model.taxon.TaxonomicStatus;
import org.gbif.dwc.terms.ConceptTerm;
import org.gbif.dwc.terms.DcTerm;
import org.gbif.dwc.terms.DwcTerm;
import org.gbif.dwc.terms.GbifTerm;
import org.joda.time.DateTime;
import org.joda.time.format.ISODateTimeFormat;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.core.convert.ConversionException;
import org.springframework.format.Parser;
import org.springframework.format.datetime.joda.DateTimeParser;
import org.springframework.validation.BindException;

/**
 *
 * @author ben
 *
 */
public class DescriptionFieldSetMapper extends
        DarwinCoreFieldSetMapper<TextContent> {

    /**
     *
     */
    public DescriptionFieldSetMapper() {
        super(TextContent.class);
    }

    /**
    *
    */
    private Logger logger = LoggerFactory
            .getLogger(DescriptionFieldSetMapper.class);

    /**
    *
    */
   private Parser<DateTime> dateTimeParser
       = new DateTimeParser(ISODateTimeFormat.dateOptionalTimeParser());

    @Override
    public void mapField(final TextContent object, final String fieldName,
            final String value) throws BindException {
        ConceptTerm term = getTermFactory().findTerm(fieldName);
        if (term instanceof DcTerm) {
            DcTerm dcTerm = (DcTerm) term;
            switch (dcTerm) {
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
            case description:
                object.setContent(value);
                break;
            case type:
                object.setFeature(Feature.fromString(value));
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
                if(taxon == null) {
                    throw new CannotFindRecordException(value);
                } else {
                    object.setTaxon(taxon);
                }

                break;
            default:
                break;
            }
        }
    }
}
