package org.emonocot.job.dwc.taxon;

import org.emonocot.harvest.common.TaxonRelationshipResolver;
import org.emonocot.model.taxon.Taxon;
import org.gbif.dwc.terms.ConceptTerm;
import org.gbif.dwc.terms.DcTerm;
import org.gbif.dwc.terms.DwcTerm;
import org.gbif.dwc.terms.TermFactory;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.batch.item.file.mapping.FieldSetMapper;
import org.springframework.batch.item.file.transform.FieldSet;
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
    private TermFactory termFactory = new TermFactory();

    /**
     *
     * @param newFieldNames Set the names of the fields
     */
    public final void setFieldNames(final String[] newFieldNames) {
        this.fieldNames = newFieldNames;
    }


    @Override
    public final Taxon mapFieldSet(final FieldSet fieldSet)
            throws BindException {
       Taxon taxon = new Taxon();
//       for (int i = 0; i < fieldNames.length; i++) {
//           ConceptTerm term = termFactory.findTerm(fieldNames[i]);
//           if (term instanceof DcTerm) {
//               DcTerm dcTerm = (DcTerm) term;
//                switch (dcTerm) {
//                default:
//                    break;
//                }
//           }
//           if (term instanceof DwcTerm) {
//               DwcTerm dwcTerm = (DwcTerm) term;
//               switch(dwcTerm) {
//               case taxonID:
//                 taxon.setIdentifier(fieldSet.readString(i));
//                 super.bind(taxon);
//                 break;
//               case scientificNameID:
//                 // should do something like
//                 // taxon.setNameIdentifier(fieldSet.readString(i))
//               case scientificName:
//                 taxon.setName(fieldSet.readString(i));
//                 break;
//               case scientificNameAuthorship:
//                // should do something like
//                // taxon.setNameAuthorship(fieldSet.readString(i))
//               case taxonRank:
//                // should do something like
//                // Rank rank = conversionService.convert(fieldSet.readString(i));
//                // taxon.setNameRank(rank);
//               case taxonomicStatus:
//                // should do something like
//                // TaxonomicStatus status = conversionService.convert(fieldSet.readString(i));
//                // taxon.setStatus(rank);
//                   default:
//                       break;
//               }
//           }
//       }

        return taxon;
    }

}
