package org.emonocot.job.dwc.taxon;

import java.text.ParseException;
import java.util.Map;

import org.emonocot.harvest.common.TaxonRelationshipResolver;
import org.emonocot.model.taxon.NomenclaturalCode;
import org.emonocot.model.taxon.Rank;
import org.emonocot.model.taxon.RankConverter;
import org.emonocot.model.taxon.Taxon;
import org.emonocot.model.taxon.TaxonomicStatus;
import org.gbif.dwc.terms.ConceptTerm;
import org.gbif.dwc.terms.DcTerm;
import org.gbif.dwc.terms.DwcTerm;
import org.gbif.dwc.terms.TermFactory;
import org.joda.time.DateTime;
import org.joda.time.format.ISODateTimeFormat;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.batch.item.file.mapping.FieldSetMapper;
import org.springframework.batch.item.file.transform.FieldSet;
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
public class TaxonFieldSetMapper extends TaxonRelationshipResolver
    implements FieldSetMapper<Taxon> {

   /**
    *
    */
    private Logger logger
        = LoggerFactory.getLogger(TaxonFieldSetMapper.class);

    /**
     *
     */
    private String[] fieldNames;

    /**
     *
     */
    private Map<String,String> defaultValues;

    /**
     *
     */
    private Converter<String,Rank> rankConverter = new RankConverter();

    /**
     *
     */
    private Parser<DateTime> dateTimeParser = new DateTimeParser(ISODateTimeFormat.dateOptionalTimeParser());

    /**
     *
     */
    private TermFactory termFactory = new TermFactory();

    /**
     *
     * @param newFieldNames Set the names of the fields
     */
    public final void setFieldNames(final String[] newFieldNames) {
        this.fieldNames = newFieldNames;
    }

    /**
    *
    * @param newDefaultValues Set the defaultValues of the fields
    */
   public final void setDefaultValues(final Map<String,String> newDefaultValues) {
       this.defaultValues = newDefaultValues;
   }

   /**
    *
    */
   public final Taxon mapFieldSet(final FieldSet fieldSet)
            throws BindException {
       Taxon taxon = new Taxon();
       for (int i = 0; i < fieldNames.length; i++) {
           ConceptTerm term = termFactory.findTerm(fieldNames[i]);
           if (term instanceof DcTerm) {
               DcTerm dcTerm = (DcTerm) term;
                switch (dcTerm) {
                case modified:
                    try {
                        taxon.setModified(dateTimeParser.parse(fieldSet.readString(i), null));
                    } catch (ParseException pe) {
                        BindException be = new BindException(taxon, "target");
                        be.rejectValue("modified", "not.valid", pe.getMessage());
                        throw be;
                    }
                    break;
                case created:
                    try {
                        taxon.setCreated(dateTimeParser.parse(fieldSet.readString(i), null));
                    } catch (ParseException pe) {
                        BindException be = new BindException(taxon, "target");
                        be.rejectValue("created", "not.valid", pe.getMessage());
                        throw be;
                    }
                case source:
                    taxon.setSource(fieldSet.readString(i));
                default:
                    break;
                }
           }
           // DwcTerms
           if (term instanceof DwcTerm) {
               DwcTerm dwcTerm = (DwcTerm) term;
               switch(dwcTerm) {
               case taxonID:
                 taxon.setIdentifier(fieldSet.readString(i));
                 super.bind(taxon);
                 break;
               case scientificNameID:
                   // should do something like
                   // taxon.setNameIdentifier(fieldSet.readString(i))
                   break;
               case scientificName:
                   taxon.setName(fieldSet.readString(i));
                   break;
               case scientificNameAuthorship:
                  taxon.setAuthorship(fieldSet.readString(i));
                  break;
               case taxonRank:
                  // should do something like
                  try {
                     Rank rank = rankConverter.convert(
                             fieldSet.readString(i).toUpperCase().replaceAll(" ", "_"));
                     taxon.setRank(rank);
                  } catch (ConversionException ce) {
                     logger.error(ce.getMessage());
                     return null;
                  }
                  break;
               case taxonomicStatus:
                  TaxonomicStatus status = TaxonomicStatus.valueOf(fieldSet.readString(i));
                  taxon.setStatus(status);
                   break;
               case parentNameUsageID:
                   // Ignore for now as we're not importing this taxon anyway
                   break;
               case acceptedNameUsageID:
                   // Ignore for now as we're not importing this taxon anyway
                   break;
               case genus:
                   taxon.setGenus(fieldSet.readString(i));
                   break;
               case subgenus:
                   taxon.setInfraGenericEpithet(fieldSet.readString(i));
                   break;
               case specificEpithet:
                   taxon.setSpecificEpithet(fieldSet.readString(i));
                   break;
               case infraspecificEpithet:
                   taxon.setInfraSpecificEpithet(fieldSet.readString(i));
                   break;
               case nameAccordingTo:
                   taxon.setAccordingTo(fieldSet.readString(i));
                   break;
               case kingdom:
                   taxon.setKingdom(fieldSet.readString(i));
                   break;
               case phylum:
                   taxon.setPhylum(fieldSet.readString(i));
                   break;
               case classs:
                   taxon.setClass(fieldSet.readString(i));
                   break;
               case order:
                   taxon.setOrder(fieldSet.readString(i));
                   break;
               case family:
                   taxon.setFamily(fieldSet.readString(i));
                   break;
               case nomenclaturalCode:
                   taxon.setNomenclaturalCode(
                           NomenclaturalCode.valueOf(
                                   fieldSet.readString(i)));
               default:
                   break;
               }
           }
       }

        return taxon;
    }

}
