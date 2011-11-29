package org.emonocot.job.dwc.taxon;

import java.text.ParseException;

import org.emonocot.job.dwc.DarwinCoreFieldSetMapper;
import org.emonocot.model.taxon.NomenclaturalCode;
import org.emonocot.model.taxon.Rank;
import org.emonocot.model.taxon.RankConverter;
import org.emonocot.model.taxon.Taxon;
import org.emonocot.model.taxon.TaxonomicStatus;
import org.gbif.dwc.terms.ConceptTerm;
import org.gbif.dwc.terms.DcTerm;
import org.gbif.dwc.terms.DwcTerm;
import org.joda.time.DateTime;
import org.joda.time.format.ISODateTimeFormat;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.core.convert.ConversionException;
import org.springframework.core.convert.converter.Converter;
import org.springframework.format.Parser;
import org.springframework.format.datetime.joda.DateTimeParser;
import org.springframework.validation.BindException;

/**
 *
 * @author ben
 *
 */
public class TaxonFieldSetMapper extends DarwinCoreFieldSetMapper<Taxon> {

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
    private Parser<DateTime> dateTimeParser
        = new DateTimeParser(ISODateTimeFormat.dateOptionalTimeParser());

    /**
     *
     */
    public TaxonFieldSetMapper() {
        super(Taxon.class);
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
                super.bind(taxon);
                break;
            case scientificNameID:
                // should do something like
                // taxon.setNameIdentifier(value)
                break;
            case scientificName:
                taxon.setName(value);
                break;
            case scientificNameAuthorship:
                taxon.setAuthorship(value);
                break;
            case taxonRank:
                // should do something like
                try {
                    Rank rank = rankConverter.convert(value
                            .toUpperCase().replaceAll(" ", "_"));
                    taxon.setRank(rank);
                } catch (ConversionException ce) {
                    logger.error(ce.getMessage());
                }
                break;
            case taxonomicStatus:
                TaxonomicStatus status = TaxonomicStatus.valueOf(value);
                taxon.setStatus(status);
                break;
            case parentNameUsageID:
                // Ignore for now as we're not importing this taxon anyway
                break;
            case acceptedNameUsageID:
                // Ignore for now as we're not importing this taxon anyway
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
                taxon.setClass(value);
                break;
            case order:
                taxon.setOrder(value);
                break;
            case family:
                taxon.setFamily(value);
                break;
            case nomenclaturalCode:
                taxon.setNomenclaturalCode(NomenclaturalCode.valueOf(value));
            default:
                break;
            }
        }
    }
}
