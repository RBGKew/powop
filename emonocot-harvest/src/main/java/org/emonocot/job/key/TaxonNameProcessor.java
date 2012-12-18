package org.emonocot.job.key;

import java.util.HashSet;
import java.util.List;
import java.util.Set;

import org.emonocot.api.TaxonService;
import org.emonocot.api.match.Match;
import org.emonocot.api.match.taxon.TaxonMatcher;
import org.emonocot.harvest.common.AbstractRecordAnnotator;
import org.emonocot.model.Annotation;
import org.emonocot.model.Taxon;
import org.emonocot.model.constants.AnnotationCode;
import org.emonocot.model.constants.AnnotationType;
import org.emonocot.model.constants.RecordType;
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
public class TaxonNameProcessor extends AbstractRecordAnnotator implements
        ItemProcessor<TaxonName, TaxonName> {
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
     *
     */
    private TaxonService taxonService;

    /**
     * @param newTaxonMatcher
     *            the matcher to set
     */
    public final void setTaxonMatcher(final TaxonMatcher newTaxonMatcher) {
        this.taxonMatcher = newTaxonMatcher;
    }

    /**
     * @param newNameParser
     *            the nameParser to set
     */
    public final void setNameParser(final NameParser newNameParser) {
        this.nameParser = newNameParser;
    }

    /**
     * @param newTaxonService
     *            Set the taxon service
     */
    public final void setTaxonService(final TaxonService newTaxonService) {
        this.taxonService = newTaxonService;
    }

    /**
     * @param item
     *            a taxon name to match
     * @return a lookup
     * @throws Exception
     *             if there is a problem
     */
    public final TaxonName process(final TaxonName item) throws Exception {

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

                item.setDebuglabel(null);
            } else if (matches.size() > 1) {
                annotationType = AnnotationType.Error;
                code = AnnotationCode.BadRecord;
                text = matches.size() + " matches found for taxonomic name "
                        + taxonName;

                item.setDebuglabel(null);
            } else {
                annotationType = AnnotationType.Info;
                code = AnnotationCode.Present;
                object = matches.get(0).getInternal();
                
                text = object.getIdentifier() + " matches taxonomic name "
                        + taxonName;

                item.setDebuglabel(object.getIdentifier());
            }

            Annotation annotation = new Annotation();
            annotation.setJobId(stepExecution.getJobExecutionId());
            annotation.setAnnotatedObj(object);
            annotation.setRecordType(RecordType.Taxon);
            annotation.setCode(code);
            annotation.setType(annotationType);
            annotation.setValue(item.getId());
            annotation.setText(text);
            super.annotate(annotation);
            return item;
        }
    }



}
