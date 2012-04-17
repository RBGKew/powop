package org.emonocot.job.key;

import java.util.List;

import org.emonocot.api.match.Match;
import org.emonocot.api.match.taxon.TaxonMatcher;
import org.emonocot.harvest.common.AuthorityAware;
import org.emonocot.model.common.Annotation;
import org.emonocot.model.common.AnnotationCode;
import org.emonocot.model.common.AnnotationType;
import org.emonocot.model.common.RecordType;
import org.emonocot.model.taxon.Taxon;
import org.gbif.ecat.model.ParsedName;
import org.gbif.ecat.parser.NameParser;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.batch.item.ItemProcessor;
import org.tdwg.ubif.TaxonName;

/**
 *
 * @author ben
 *
 */
public class TaxonNameProcessor extends AuthorityAware implements
        ItemProcessor<TaxonName, Annotation> {
    /**
     *
     */
    private Logger logger = LoggerFactory.getLogger(TaxonNameProcessor.class);

    /**
    *
    */
    private TaxonMatcher taxonMatcher;

    /**
    *
    */
    private NameParser nameParser;

    /**
     * @param newTaxonMatcher
     *            the matcher to set
     */
    public void setTaxonMatcher(TaxonMatcher newTaxonMatcher) {
        this.taxonMatcher = newTaxonMatcher;
    }

    /**
     * @param newNameParser
     *            the nameParser to set
     */
    public void setNameParser(NameParser newNameParser) {
        this.nameParser = newNameParser;
    }

    /**
     * @param item
     *            a taxon name to match
     * @return a lookup
     * @throws Exception
     *             if there is a problem
     */
    public final Annotation process(final TaxonName item) throws Exception {

        Taxon object = null;
        AnnotationType annotationType = null;
        AnnotationCode code = null;
        String text = null;
        if (item.getRepresentation() == null) {
            return null;
        } else {
            String taxonName = item.getRepresentation().getLabel();
            ParsedName parsedName = nameParser.parse(taxonName);
            List<Match<Taxon>> matches = taxonMatcher.match(parsedName);
            if (matches.size() == 0) {
                annotationType = AnnotationType.Error;
                code = AnnotationCode.Absent;
                text = "No matches found for taxonomic name " + taxonName;
            } else if (matches.size() > 1) {
                annotationType = AnnotationType.Error;
                code = AnnotationCode.BadRecord;
                text = matches.size() + " matches found for taxonomic name "
                        + taxonName;
            } else {
                annotationType = AnnotationType.Info;
                code = AnnotationCode.Present;
                object = matches.get(0).getInternal();
                text = object.getIdentifier() + " matches taxonomic name "
                        + taxonName;
            }

            Annotation annotation = super.createAnnotation(object,
                    RecordType.Taxon, code, annotationType);
            annotation.setValue(item.getId());
            annotation.setText(text);
            logger.debug(text);
            return annotation;
        }
    }

}
